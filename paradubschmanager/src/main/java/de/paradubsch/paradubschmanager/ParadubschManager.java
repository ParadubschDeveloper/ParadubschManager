package de.paradubsch.paradubschmanager;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.craftery.CraftingPlugin;
import de.paradubsch.paradubschmanager.commands.*;
import de.paradubsch.paradubschmanager.config.ConfigurationHelper;
import de.craftery.util.HibernateConfigurator;
import de.paradubsch.paradubschmanager.config.WebserverManager;
import de.paradubsch.paradubschmanager.lifecycle.*;
import de.paradubsch.paradubschmanager.lifecycle.jobs.JobManager;
import de.paradubsch.paradubschmanager.lifecycle.playtime.PlaytimeManager;
import de.paradubsch.paradubschmanager.lifecycle.stairsit.StairSitManager;
import de.paradubsch.paradubschmanager.models.*;
import de.paradubsch.paradubschmanager.models.logging.ChatMessageLog;
import de.paradubsch.paradubschmanager.models.logging.CommandLog;
import de.paradubsch.paradubschmanager.models.logging.KitRedeemLog;
import de.paradubsch.paradubschmanager.models.logging.LogEntry;
import de.paradubsch.paradubschmanager.util.lang.Message;
import lombok.Getter;
import lombok.Setter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.*;

public final class ParadubschManager extends CraftingPlugin {
    private static ParadubschManager instance;

    @Getter
    private PlaytimeManager playtimeManager;

    @Getter
    private WorldGuardPlugin worldGuardPlugin;

    @Getter
    private WorldEditPlugin worldEditPlugin;

    private LuckPerms luckPermsApi;

    @Setter
    @Getter
    private List<UUID> vanishedPlayers = new ArrayList<>();

    @Getter
    private final Map<UUID, UUID> replyCandidates = new HashMap<>();

    @Getter
    private final List<TpaRequest> tpaRequests = new ArrayList<>();

    @Getter
    private JobManager jobManager;

    @Getter
    private final Map<UUID, Long> rtpTimeouts = new HashMap<>();

    @Getter
    private final Map<UUID, Long> gsBackupTimeouts = new HashMap<>();

    @Getter
    private final Map<UUID, Long> timevoteTimeouts = new HashMap<>();

    private WebserverManager webServer;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("==== Paradubsch ====");
        Bukkit.getConsoleSender().sendMessage("Authors: Crafter_Y, Blintastisch_, Byte, DieButzenscheibe");
        Bukkit.getConsoleSender().sendMessage("Version: 1.1");
        Bukkit.getConsoleSender().sendMessage("==== Paradubsch ====");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !> Initializing");

        instance = this;
        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !>> Initializing CraftPlugin");
        super.onEnable();
        ConfigurationHelper.addSpecificConfurations();
        this.addHibernateEntities();

        registerEvents();

        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !>> Registering commands");
        this.registerCommands();

        jobManager = new JobManager();

        worldGuardPlugin = initializeWorldGuardPlugin();
        worldEditPlugin = initializeWorldEditPlugin();

        this.getLanguageManager().registerMessageEnum(Message.Info.class);
        this.getLanguageManager().registerMessageEnum(Message.Error.class);
        this.getLanguageManager().registerMessageEnum(Message.Constant.class);
        this.getLanguageManager().registerMessageEnum(Message.Gui.class);

        playtimeManager = new PlaytimeManager();

        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !>> Deleting old backups");
        WebserverManager.clearSchematicFiles();

        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !>> Starting Web Service");
        this.webServer = new WebserverManager().startWebserver();

        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !> Initialization done");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        worldGuardPlugin = null;
        worldEditPlugin = null;
        playtimeManager = null;
        luckPermsApi = null;

        jobManager = null;

        if (webServer != null) {
            webServer.stopWebserver();
        }
        webServer = null;
        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !> Disabled");
    }

    private void addHibernateEntities() {
        HibernateConfigurator.addEntity(Home.class);
        HibernateConfigurator.addEntity(SaveRequest.class);
        HibernateConfigurator.addEntity(PunishmentHolder.class);
        HibernateConfigurator.addEntity(WarnPunishment.class);
        HibernateConfigurator.addEntity(BanPunishment.class);
        HibernateConfigurator.addEntity(MutePunishment.class);
        HibernateConfigurator.addEntity(PunishmentUpdate.class);
        HibernateConfigurator.addEntity(Warp.class);
        HibernateConfigurator.addEntity(WorkerPlayer.class);
        HibernateConfigurator.addEntity(BazaarOrder.class);
        HibernateConfigurator.addEntity(BazaarCollectable.class);
        HibernateConfigurator.addEntity(Backpack.class);
        HibernateConfigurator.addEntity(StorageSlot.class);
        HibernateConfigurator.addEntity(ItemData.class);
        HibernateConfigurator.addEntity(GsWhitelistEnabled.class);
        HibernateConfigurator.addEntity(GsWhitelistMember.class);
        HibernateConfigurator.addEntity(GsBanMember.class);
        HibernateConfigurator.addEntity(LogEntry.class);
        HibernateConfigurator.addEntity(ChatMessageLog.class);
        HibernateConfigurator.addEntity(SavedKit.class);
        HibernateConfigurator.addEntity(KitRedeemEntry.class);
        HibernateConfigurator.addEntity(KitCollectable.class);
        HibernateConfigurator.addEntity(KitRedeemLog.class);
        HibernateConfigurator.addEntity(CommandLog.class);
    }

    private void registerEvents() {
        new ChatMessageListener();
        new PlayerJoinPrecedure();
        new QuitListener();
        new TabDecorationManager(this);
        new InvseeInventoryGuard();
        new StairSitManager();
        new GsRegionListener();
        new CommandListener();
    }

    private void registerCommands() {
        registerLegacyCommand("msg", new MsgCommand());
        registerLegacyCommand("prefix", new PrefixCommand());
        registerLegacyCommand("lang", new LangCommand());
        registerLegacyCommand("namecolor", new NameColorCommand());
        registerLegacyCommand("defaultchatcolor", new DefaultChatColorCommand());
        registerLegacyCommand("playtime", new PlaytimeCommand());
        registerLegacyCommand("money", new MoneyCommand());
        registerLegacyCommand("sethome", new SethomeCommand());
        registerCommand(new HomeCommand());
        registerLegacyCommand("buyhome", new BuyhomeCommand());
        registerLegacyCommand("homes", new HomesCommand());
        registerLegacyCommand("viewhome", new ViewhomeCommand());
        registerLegacyCommand("delhome", new DelhomeCommand());
        registerLegacyCommand("gs", new GsCommand());
        registerLegacyCommand("save", new SaveCommand());
        registerLegacyCommand("rank", new RankCommand());
        registerCommand(new BanCommand());
        registerLegacyCommand("gm", new GmCommand());
        registerLegacyCommand("warn", new WarnCommand());
        registerLegacyCommand("warp", new WarpCommand());
        registerLegacyCommand("warps", new WarpsCommand());
        registerLegacyCommand("bauwelt", new BauweltCommand());
        registerLegacyCommand("farmwelt", new FarmweltCommand());
        registerLegacyCommand("nether", new NetherCommand());
        registerLegacyCommand("end", new EndCommand());
        registerLegacyCommand("spawn", new SpawnCommand());
        registerLegacyCommand("vanish", new VanishCommand());
        registerLegacyCommand("seen", new SeenCommand());
        registerLegacyCommand("reply", new ReplyCommand());
        registerLegacyCommand("speed", new SpeedCommand());
        registerLegacyCommand("day", new DayCommand());
        registerLegacyCommand("night", new NightCommand());
        registerLegacyCommand("i", new ICommand());
        registerLegacyCommand("run", new RunCommand());
        registerLegacyCommand("tpa", new TpaCommand());
        registerLegacyCommand("tpaccept", new TpacceptCommand());
        registerLegacyCommand("tpacancel", new TpacancelCommand());
        registerLegacyCommand("invsee", new InvseeCommand());
        registerLegacyCommand("job", new JobCommand());
        registerLegacyCommand("bazaar", new BazaarCommand());
        registerLegacyCommand("rtp", new RtpCommand());
        registerLegacyCommand("saverequests", new SaveRequestsCommand());
        registerLegacyCommand("workbench", new WorkbenchCommand());
        registerLegacyCommand("enderchest", new EnderchestCommand());
        registerCommand(new BackpackCommand());
        registerLegacyCommand("feed", new FeedCommand());
        registerLegacyCommand("nightvision", new NightvisonCommand());
        registerCommand(new AnvilCommand());
        registerLegacyCommand("loom", new LoomCommand());
        registerLegacyCommand("smithingtable", new SmithingCommand());
        registerLegacyCommand("stonecutter", new StoneCutterCommand());
        registerLegacyCommand("hat", new HatCommand());
        registerLegacyCommand("mute", new MuteCommand());
        registerLegacyCommand("timevote", new TimeVoteCommand());
        registerLegacyCommand("kit", new KitCommand());
        registerCommand(new CollectablesCommand());
    }
    private WorldGuardPlugin initializeWorldGuardPlugin () {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
        if (!(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }

    private WorldEditPlugin initializeWorldEditPlugin () {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldEdit");
        if (!(plugin instanceof WorldEditPlugin)) {
            return null;
        }
        return (WorldEditPlugin) plugin;
    }

    public static LuckPerms getLuckPermsApi() {
        LuckPerms lp = ParadubschManager.getInstance().luckPermsApi;
        if (lp == null) {
            try {
                ParadubschManager.getInstance().luckPermsApi = LuckPermsProvider.get();
            } catch (IllegalStateException ex) {
                return null;
            }
        }
        return ParadubschManager.getInstance().luckPermsApi;
    }

    public static ParadubschManager getInstance() {
        return instance;
    }

    public ParadubschManager() {
        super();
    }

    @SuppressWarnings("all")
    protected ParadubschManager(JavaPluginLoader loader, PluginDescriptionFile descriptionFile, File dataFolder, File file) {
        super(loader, descriptionFile, dataFolder, file);
    }
}
