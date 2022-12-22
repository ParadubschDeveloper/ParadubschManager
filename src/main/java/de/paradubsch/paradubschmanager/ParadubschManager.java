package de.paradubsch.paradubschmanager;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.craftery.CraftPlugin;
import de.paradubsch.paradubschmanager.commands.*;
import de.paradubsch.paradubschmanager.config.ConfigurationHelper;
import de.craftery.util.HibernateConfigurator;
import de.paradubsch.paradubschmanager.config.WebserverManager;
import de.paradubsch.paradubschmanager.lifecycle.*;
import de.paradubsch.paradubschmanager.lifecycle.jobs.JobManager;
import de.paradubsch.paradubschmanager.lifecycle.playtime.PlaytimeManager;
import de.paradubsch.paradubschmanager.lifecycle.stairsit.StairSitManager;
import de.paradubsch.paradubschmanager.lifecycle.vote.TimeVote;
import de.paradubsch.paradubschmanager.models.*;
import de.paradubsch.paradubschmanager.util.lang.Message;
import lombok.Getter;
import lombok.Setter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public final class ParadubschManager extends CraftPlugin {
    private static ParadubschManager instance;

    @Getter
    private PlaytimeManager playtimeManager;

    @Getter
    private WorldGuardPlugin worldGuardPlugin;

    @Getter
    private WorldEditPlugin worldEditPlugin;

    private ProtocolManager protocolManager;

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
        protocolManager = ProtocolLibrary.getProtocolManager();

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
        protocolManager = null;
        luckPermsApi = null;

        jobManager = null;

        if (webServer != null) {
            webServer.stopWebserver();
        }
        webServer = null;

        HibernateConfigurator.shutdown();
        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !> Disabled");
    }

    private void addHibernateEntities() {
        HibernateConfigurator.addEntity(PlayerData.class);
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
    }

    private void registerEvents() {
        new ChatMessageListener();
        new PlayerJoinPrecedure();
        new QuitListener();
        new TabDecorationManager(this);
        new InvseeInventoryGuard();
        new StairSitManager();
    }

    private void registerCommands() {
        register("msg", new MsgCommand());
        register("prefix", new PrefixCommand());
        register("lang", new LangCommand());
        register("namecolor", new NameColorCommand());
        register("defaultchatcolor", new DefaultChatColorCommand());
        register("playtime", new PlaytimeCommand());
        register("money", new MoneyCommand());
        register("sethome", new SethomeCommand());
        register("home", new HomeCommand());
        register("buyhome", new BuyhomeCommand());
        register("homes", new HomesCommand());
        register("viewhome", new ViewhomeCommand());
        register("delhome", new DelhomeCommand());
        register("gs", new GsCommand());
        register("save", new SaveCommand());
        register("rank", new RankCommand());
        register("cb", new BanCommand());
        register("gm", new GmCommand());
        register("warn", new WarnCommand());
        register("warp", new WarpCommand());
        register("warps", new WarpsCommand());
        register("bauwelt", new BauweltCommand());
        register("farmwelt", new FarmweltCommand());
        register("nether", new NetherCommand());
        register("end", new EndCommand());
        register("spawn", new SpawnCommand());
        register("vanish", new VanishCommand());
        register("seen", new SeenCommand());
        register("reply", new ReplyCommand());
        register("speed", new SpeedCommand());
        register("day", new DayCommand());
        register("night", new NightCommand());
        register("i", new ICommand());
        register("run", new RunCommand());
        register("tpa", new TpaCommand());
        register("tpaccept", new TpacceptCommand());
        register("tpacancel", new TpacancelCommand());
        register("invsee", new InvseeCommand());
        register("job", new JobCommand());
        register("bazaar", new BazaarCommand());
        register("rtp", new RtpCommand());
        register("saverequests", new SaveRequestsCommand());
        register("workbench", new WorkbenchCommand());
        register("enderchest", new EnderchestCommand());
        register("backpack", new BackpackCommand());
        register("feed", new FeedCommand());
        register("nightvision", new NightvisonCommand());
        register("anvil", new AnvilCommand());
        register("loom", new LoomCommand());
        register("smithingtable", new SmithingCommand());
        register("stonecutter", new StoneCutterCommand());
        register("hat", new HatCommand());
        register("mute", new MuteCommand());
        register("votezeit",new TimeVote());
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

    public static @Nullable ProtocolManager getProtocolManager() {
        ProtocolManager pm = ParadubschManager.getInstance().protocolManager;
        if (pm == null) {
            ParadubschManager.getInstance().protocolManager = ProtocolLibrary.getProtocolManager();
        }
        return ParadubschManager.getInstance().protocolManager;
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
