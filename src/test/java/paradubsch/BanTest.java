package paradubsch;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.BanPunishment;
import de.paradubsch.paradubschmanager.models.PunishmentHolder;
import de.paradubsch.paradubschmanager.models.PunishmentUpdate;
import de.paradubsch.paradubschmanager.util.Hibernate;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.command.PluginCommand;
import org.junit.jupiter.api.*;
import paradubsch.util.ChatUtilities;
import paradubsch.util.ComponentConversion;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BanTest {
    private static ServerMock server;
    private static ParadubschManager plugin;
    private static PlayerMock adminPlayer;
    private static PlayerMock targetPlayer;

    @BeforeAll
    public static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(ParadubschManager.class);
        server.getScheduler().performOneTick();
        server.getScheduler().waitAsyncTasksFinished();
        server.getScheduler().waitAsyncEventsFinished();
        adminPlayer = server.addPlayer();
        targetPlayer = server.addPlayer();
        server.getScheduler().waitAsyncTasksFinished();
        server.getScheduler().waitAsyncEventsFinished();
        server.getScheduler().performOneTick();
    }

    @AfterAll
    public static void tearDown() {
        server.getScheduler().waitAsyncTasksFinished();
        server.getScheduler().waitAsyncEventsFinished();
        System.out.println("Disabling plugin");
        server.getPluginManager().disablePlugins();
        System.out.println("Disabling server");
        MockBukkit.unmock();
        System.out.println("Disabled");
    }

    @SuppressWarnings({"deprecation"})
    private void testNoPermissionErrorGot(PlayerMock player) {
        Optional<PluginCommand> opt = server.getPluginManager().getCommands().stream().filter(x -> x.getName().equals("b")).findFirst();
        String nextMsg = player.nextMessage();
        assertNotNull(nextMsg);
        if (opt.isPresent()) {
            PluginCommand command = opt.get();
            assertEquals(command.getPermissionMessage(), nextMsg);
        }
        assertNull(player.nextMessage());
    }

    @Order(1)
    @DisplayName("Test Ban Command without permission")
    @Test
    public void executeCommandsWithoutPermission() {
        adminPlayer.performCommand("paradubschmanager:ban");
        server.getScheduler().waitAsyncTasksFinished();
        testNoPermissionErrorGot(adminPlayer);

        adminPlayer.performCommand("paradubschmanager:ban " + targetPlayer.getName());
        server.getScheduler().waitAsyncTasksFinished();
        testNoPermissionErrorGot(adminPlayer);

        adminPlayer.performCommand("paradubschmanager:ban " + targetPlayer.getName() + " perma test nachricht");
        server.getScheduler().waitAsyncTasksFinished();
        testNoPermissionErrorGot(adminPlayer);

        adminPlayer.performCommand("paradubschmanager:ban list");
        server.getScheduler().waitAsyncTasksFinished();
        testNoPermissionErrorGot(adminPlayer);

        adminPlayer.performCommand("paradubschmanager:ban edit");
        server.getScheduler().waitAsyncTasksFinished();
        testNoPermissionErrorGot(adminPlayer);

        adminPlayer.performCommand("paradubschmanager:ban delete");
        server.getScheduler().waitAsyncTasksFinished();
        testNoPermissionErrorGot(adminPlayer);
    }

    @Order(2)
    @DisplayName("Give ban permissions to a player")
    @Test
    public void giveBanPermissions() {
        adminPlayer.addAttachment(plugin, "paradubsch.ban", true);
        assertTrue(adminPlayer.hasPermission("paradubsch.ban"));
    }

    @Order(3)
    @DisplayName("Ban player temporarily")
    @Test
    public void banTemporarily() {
        String banCause = "temporarily ban";

        adminPlayer.performCommand("paradubschmanager:ban " + targetPlayer.getName() + " 1d " + banCause);

        server.getScheduler().waitAsyncTasksFinished();

        System.out.println("1 expext ban message");
        String nextMsg = ChatUtilities.waitNextMessage(server, adminPlayer);
        Component sent = MessageAdapter.getSendableMessage(adminPlayer, Message.Info.CMD_BAN_PLAYER_BANNED, targetPlayer.getName());
        String sentMsg = ComponentConversion.fromComponent(sent);
        assertEquals(sentMsg, nextMsg);

        PunishmentHolder ph = Hibernate.getPunishmentHolder(targetPlayer);
        assertNotNull(ph);

        assertTrue(ph.isActiveBan());
        assertNotEquals(0, ph.getActiveBanId());
        assertEquals(banCause, ph.getActiveBanReason());
        assertTrue(ph.getActiveBanExpiration().getTime() > Timestamp.from(Instant.now()).getTime());
        assertFalse(ph.isPermaBanned());

        BanPunishment ban = Hibernate.get(BanPunishment.class, ph.getActiveBanId());
        assertNotNull(ban);
        assertEquals(ban.getId(), ph.getActiveBanId());
        assertEquals(ban.getHolderRef().getUuid(), targetPlayer.getUniqueId().toString());
        assertEquals(ban.getReason(), banCause);
        assertEquals(ban.getGivenBy().getUuid(), adminPlayer.getUniqueId().toString());
        assertFalse(ban.isPermanent());
        assertEquals(ban.getExpiration().getTime(), ph.getActiveBanExpiration().getTime());
        assertFalse(ban.isHasUpdate());

    }

    @Order(4)
    @DisplayName("Unban the temporarily banned player")
    @Test
    public void unbanTemporarily() {
        PunishmentHolder ph = Hibernate.getPunishmentHolder(targetPlayer);
        assertNotNull(ph);
        long banId = ph.getActiveBanId();

        String unbanReason = "unbanning reason 123";
        adminPlayer.performCommand("paradubschmanager:ban delete " + targetPlayer.getName() + " " + unbanReason);
        server.getScheduler().waitAsyncTasksFinished();

        System.out.println("2 expext unbanban message");
        String nextMsg = ChatUtilities.waitNextMessage(server, adminPlayer);
        Component sent = MessageAdapter.getSendableMessage(adminPlayer, Message.Info.CMD_BAN_PLAYER_UNBANNED, targetPlayer.getName());
        String sentMsg = ComponentConversion.fromComponent(sent);
        assertEquals(sentMsg, nextMsg);

        ph = Hibernate.getPunishmentHolder(targetPlayer);
        BanPunishment ban = Hibernate.get(BanPunishment.class, banId);
        assertNotNull(ph);
        assertNotNull(ban);

        assertFalse(ph.isActiveBan());
        assertTrue(ph.getActiveBanExpiration().getTime() <= Timestamp.from(Instant.now()).getTime());
        assertEquals(0, ph.getActiveBanId());
        assertNull(ph.getActiveBanReason());
        assertFalse(ph.isPermaBanned());

        assertTrue(ban.isHasUpdate());

        List<PunishmentUpdate> updates = Hibernate.getBanUpdates(ban);

        assertEquals(1, updates.size());
        PunishmentUpdate update = updates.get(0);

        assertEquals(update.getReason(), unbanReason);
        assertFalse(update.isPermanent());

    }

    @Order(5)
    @DisplayName("Ban the player permanently")
    @Test
    public void permaBan() {
        String banCause = "Perma ban hahahahaaha du wurdest perma gebannnt";

        adminPlayer.performCommand("paradubschmanager:ban " + targetPlayer.getName() + " perma " + banCause);

        server.getScheduler().waitAsyncTasksFinished();

        System.out.println("3 expext ban message");
        String nextMsg = ChatUtilities.waitNextMessage(server, adminPlayer);
        Component sent = MessageAdapter.getSendableMessage(adminPlayer, Message.Info.CMD_BAN_PLAYER_BANNED, targetPlayer.getName());
        String sentMsg = ComponentConversion.fromComponent(sent);
        assertEquals(sentMsg, nextMsg);

        PunishmentHolder ph = Hibernate.getPunishmentHolder(targetPlayer);
        assertNotNull(ph);

        assertTrue(ph.isActiveBan());
        assertNotEquals(0, ph.getActiveBanId());
        assertEquals(banCause, ph.getActiveBanReason());
        assertTrue(ph.getActiveBanExpiration().getTime() > Timestamp.from(Instant.now()).getTime());
        assertTrue(ph.isPermaBanned());

        BanPunishment ban = Hibernate.get(BanPunishment.class, ph.getActiveBanId());
        assertNotNull(ban);
        assertEquals(ban.getId(), ph.getActiveBanId());
        assertEquals(ban.getHolderRef().getUuid(), targetPlayer.getUniqueId().toString());
        assertEquals(ban.getReason(), banCause);
        assertEquals(ban.getGivenBy().getUuid(), adminPlayer.getUniqueId().toString());
        assertTrue(ban.isPermanent());
        assertEquals(ban.getExpiration().getTime(), ph.getActiveBanExpiration().getTime());
        assertFalse(ban.isHasUpdate());
    }

    @Order(6)
    @DisplayName("Edit the permanent ban")
    @Test
    public void editPerma() {
        String editReason = "unbanning reason 123";
        adminPlayer.performCommand("paradubschmanager:ban edit " + targetPlayer.getName() + " 1d " + editReason);
        server.getScheduler().waitAsyncTasksFinished();

        System.out.println("4 expext edit ban message");
        String nextMsg = ChatUtilities.waitNextMessage(server, adminPlayer);
        Component sent = MessageAdapter.getSendableMessage(adminPlayer, Message.Info.CMD_BAN_EDITED, targetPlayer.getName());
        String sentMsg = ComponentConversion.fromComponent(sent);
        assertEquals(sentMsg, nextMsg);

        PunishmentHolder ph = Hibernate.getPunishmentHolder(targetPlayer);
        assertNotNull(ph);

        BanPunishment ban = Hibernate.get(BanPunishment.class, ph.getActiveBanId());
        assertNotNull(ban);

        assertTrue(ph.isActiveBan());
        assertTrue(ph.getActiveBanExpiration().getTime() > Timestamp.from(Instant.now()).getTime());
        assertEquals(editReason, ph.getActiveBanReason());
        assertFalse(ph.isPermaBanned());

        assertTrue(ban.isHasUpdate());

        List<PunishmentUpdate> updates = Hibernate.getBanUpdates(ban);

        assertEquals(1, updates.size());
        PunishmentUpdate update = updates.get(0);

        assertEquals(update.getReason(), editReason);
        assertFalse(update.isPermanent());

    }
}
