/**
 * Deprecated:
 * I want to get rid of these methods and implement these in BaseDatabaseEntity or in the Entitys itself if needed.
 *
 * This will avoid code duplication and will improve the readability of the code.
 * Also it will avoid mistakes using my CachingManager. (Own inmemory 2nd level cache)
 *
 * Example 1:
 *   Old:
 *   Hibernate.delete(object);
 *
 *   New:
 *   object.delete();
 * Example 2:
 *   Old:
 *   Hibernate.getWarp(id);
 *
 *   New:
 *   Warp.getById(Warp.class, id); //TODO: I hope I or somebody else can get rid of the Warp.class needed in here
 *   // StackOverflow Question: Java: Get static child Class : https://stackoverflow.com/questions/73283417/java-get-static-child-class
 *
 * Example 3:
 *   Old:
 *   Hibernate.save(object);
 *
 *   New:
 *   object.save();
 */

package de.paradubsch.paradubschmanager.util;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.config.HibernateConfigurator;
import de.paradubsch.paradubschmanager.models.*;
import lombok.Cleanup;
import org.bukkit.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Deprecated. View File Header for more information.
 */
@Deprecated
public class Hibernate {
    /**
     * Deprecated. View File Header for more information.
     */
    @Deprecated
    public static void cachePlayerName(Player p) {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            PlayerData playerData = session.get(PlayerData.class, p.getUniqueId().toString());
            if (playerData == null) {
                session.save(new PlayerData(p));
                ParadubschManager.getInstance().getCachingManager().cacheEntity(PlayerData.class, new PlayerData(p), p.getUniqueId().toString());

            } else if (!playerData.getName().equals(p.getName())) {
                playerData.setName(p.getName());
                session.saveOrUpdate(playerData);
                ParadubschManager.getInstance().getCachingManager().cacheEntity(PlayerData.class, playerData, p.getUniqueId().toString());
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Deprecated. View File Header for more information.
     */
    @Deprecated
    public static PlayerData getPlayerData(@NotNull Player p) {
        PlayerData cached = ParadubschManager.getInstance().getCachingManager().getEntity(PlayerData.class, p.getUniqueId().toString());
        if (cached != null) {
            return cached;
        }

        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            PlayerData playerData = session.get(PlayerData.class, p.getUniqueId().toString());
            transaction.commit();
            if (playerData == null) {
                return new PlayerData(p);
            } else {
                ParadubschManager.getInstance().getCachingManager().cacheEntity(PlayerData.class, playerData, p.getUniqueId().toString());
                return playerData;
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return new PlayerData(p);
        }
    }

    /**
     * Deprecated. View File Header for more information.
     */
    @Deprecated
    public static PlayerData getPlayerData(UUID uuid) {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            PlayerData playerData = session.get(PlayerData.class, uuid.toString());
            transaction.commit();
            if (playerData == null) {
                playerData = new PlayerData();
                playerData.setUuid(uuid.toString());
            }
            return playerData;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            PlayerData playerData = new PlayerData();
            playerData.setUuid(uuid.toString());
            return playerData;
        }
    }

    /**
     * Deprecated. View File Header for more information.
     */
    @Deprecated
    public static List<Home> getHomes(Player p) {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            PlayerData playerData = session.get(PlayerData.class, p.getUniqueId().toString());
            List<Home> homes = playerData.getHomes();
            org.hibernate.Hibernate.initialize(homes);
            transaction.commit();
            return homes;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Deprecated. View File Header for more information.
     */
    @Deprecated
    public static <T extends BaseDatabaseEntity<?, ?>> void save (T o) {
        if (o == null) {
            return;
        }
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            session.saveOrUpdate(o);
            ParadubschManager.getInstance().getCachingManager().cacheEntity(o.getClass(), o, o.getIdentifyingColumn());
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    /**
     * Deprecated. View File Header for more information.
     */
    @Deprecated
    public static PlayerData getPlayerData(String playerName) {
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

    /**
     * Deprecated. View File Header for more information.
     */
    @Deprecated
    public static List<PlayerData> getMoneyTop () {
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

    /**
     * Deprecated. View File Header for more information.
     */
    @Deprecated
    public static SaveRequest getSaveRequest(Player p) {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            PlayerData playerData = session.get(PlayerData.class, p.getUniqueId().toString());
            SaveRequest saveRequest = SaveRequest.getById(playerData.getOpenSaveRequest());
            transaction.commit();
            return saveRequest;

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            return null;
        }
    }

    /**
     * Deprecated. View File Header for more information.
     */
    @Deprecated
    public static PunishmentHolder getPunishmentHolder(PlayerData pd) {
        Transaction transaction = null;
        PunishmentHolder punishmentHolder;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            punishmentHolder = session.get(PunishmentHolder.class, pd.getUuid());
            if (punishmentHolder == null) {
                punishmentHolder = new PunishmentHolder();
                punishmentHolder.setPlayerRef(pd);
                punishmentHolder.setUuid(pd.getUuid());
            }
            transaction.commit();
            return punishmentHolder;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            punishmentHolder = new PunishmentHolder();
            punishmentHolder.setPlayerRef(pd);
            punishmentHolder.setUuid(pd.getUuid());
            return null;
        }
    }

    /**
     * Deprecated. View File Header for more information.
     */
    @Deprecated
    public static PunishmentHolder getPunishmentHolder(Player player) {
        Transaction transaction = null;
        PunishmentHolder punishmentHolder;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            punishmentHolder = session.get(PunishmentHolder.class, player.getUniqueId().toString());
            if (punishmentHolder == null) {
                punishmentHolder = new PunishmentHolder();
                punishmentHolder.setPlayerRef(Hibernate.getPlayerData(player));
                punishmentHolder.setUuid(player.getUniqueId().toString());
            }
            transaction.commit();
            return punishmentHolder;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            punishmentHolder = new PunishmentHolder();
            punishmentHolder.setPlayerRef(Hibernate.getPlayerData(player));
            punishmentHolder.setUuid(player.getUniqueId().toString());
            return null;
        }
    }

    /**
     * Deprecated. View File Header for more information.
     */
    @Deprecated
    public static List<PunishmentUpdate> getBanUpdates(BanPunishment ban) {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            BanPunishment playerData = session.get(BanPunishment.class, ban.getIdentifyingColumn());
            List<PunishmentUpdate> updates = playerData.getUpdates();
            org.hibernate.Hibernate.initialize(updates);
            transaction.commit();
            return updates;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Deprecated. View File Header for more information.
     */
    @Deprecated
    public static <T, I extends Serializable> T get (Class<T> clazz, I id) {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            T t = session.get(clazz, id);
            transaction.commit();
            return t;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }
}
