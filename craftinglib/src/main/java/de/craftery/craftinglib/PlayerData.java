package de.craftery.craftinglib;

import de.craftery.craftinglib.command.CraftingPlayer;
import de.craftery.craftinglib.util.BaseDatabaseEntity;
import de.craftery.craftinglib.util.HibernateConfigurator;
import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.hibernate.Session;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "player_data", indexes = @Index(name = "player_data_index", columnList = "name"))
public class PlayerData extends BaseDatabaseEntity<PlayerData, String> {

    @Id
    @Column(name = "uuid", columnDefinition = "VARCHAR(36)")
    private String uuid;

    @Column(name = "name", columnDefinition = "VARCHAR(16)", nullable = false)
    private String name;

    @Column(name = "language_preference", columnDefinition = "VARCHAR(16) DEFAULT 'de'")
    private String language = "de";

    @Column(name = "chat_prefix", columnDefinition = "VARCHAR(256) DEFAULT '&7Spieler'")
    private String chatPrefix = "&7Spieler";

    @Column(name = "name_color", columnDefinition = "VARCHAR(24) DEFAULT '&7'")
    private String nameColor = "&7";

    @Column(name = "default_chat_color", columnDefinition = "VARCHAR(24) DEFAULT '&7'")
    private String defaultChatColor = "&7";

    @Column(name = "playtime", columnDefinition = "BIGINT DEFAULT 0")
    private long playtime = 0L;

    @Column(name ="money", columnDefinition = "BIGINT DEFAULT 150")
    private long money = 150L;

    @Column(name = "max_homes", columnDefinition = "INT DEFAULT 2")
    private int maxHomes = 2;

    @Column(name = "active_save_id")
    private Integer openSaveRequest;

    public PlayerData () {}

    public PlayerData(Player player) {
        this.uuid = player.getUniqueId().toString();
        this.name = player.getName();
    }

    public static PlayerData getById(Serializable id) {
        return BaseDatabaseEntity.getById(PlayerData.class, id);
    }

    public static PlayerData getByPlayer(CraftingPlayer player) {
        return getByPlayer(player.getPlayer());
    }

    public static PlayerData getByPlayer(Player player) {
        return BaseDatabaseEntity.getById(PlayerData.class, player.getUniqueId().toString());
    }

    public static PlayerData getByUuid(UUID uuid) {
        return BaseDatabaseEntity.getById(PlayerData.class, uuid.toString());
    }

    public static PlayerData getByName(String playerName) {
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PlayerData> cq = cb.createQuery(PlayerData.class);
            Root<PlayerData> root = cq.from(PlayerData.class);
            cq.select(root).where(cb.equal(
                    cb.lower(root.get("name")),
                    playerName.toLowerCase()
            ));

            return session.createQuery(cq).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void cachePlayerName(Player p) {
        PlayerData playerData = PlayerData.getById(p.getUniqueId().toString());
        if (playerData == null) {
            new PlayerData(p).save();
        } else if (!playerData.getName().equals(p.getName())) {
            playerData.setName(p.getName());
            playerData.saveOrUpdate();
        }
    }

    public static List<PlayerData> getMoneyTop() {
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PlayerData> cq = cb.createQuery(PlayerData.class);
            Root<PlayerData> root = cq.from(PlayerData.class);
            cq.select(root).orderBy(cb.desc(root.get("money")));

            return session.createQuery(cq).setMaxResults(10).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getIdentifyingColumn() {
        return this.uuid;
    }
}
