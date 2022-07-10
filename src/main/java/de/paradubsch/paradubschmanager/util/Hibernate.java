package de.paradubsch.paradubschmanager.util;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.config.HibernateConfigurator;
import de.paradubsch.paradubschmanager.models.*;
import lombok.Cleanup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Hibernate {
    public static void cachePlayerName(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            Transaction transaction = null;
            try {
                @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

                transaction = session.beginTransaction();

                PlayerData playerData = session.get(PlayerData.class, p.getUniqueId().toString());
                if (playerData == null) {
                    session.save(new PlayerData(p));
                } else if (!playerData.getName().equals(p.getName())) {
                    playerData.setName(p.getName());
                    session.saveOrUpdate(playerData);
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        });
    }

    public static PlayerData getPlayerData(Player p) {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            PlayerData playerData = session.get(PlayerData.class, p.getUniqueId().toString());
            transaction.commit();
            if (playerData == null) {
                return new PlayerData(p);
            } else {
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

    public static void delete(Object home) {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.delete(home);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static void save (Object o) {
        if (o == null) {
            return;
        }
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            session.saveOrUpdate(o);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

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

    public static SaveRequest getSaveRequest(Player p) {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            PlayerData playerData = session.get(PlayerData.class, p.getUniqueId().toString());
            SaveRequest saveRequest = playerData.getOpenSaveRequest();
            org.hibernate.Hibernate.initialize(saveRequest);
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

    public static SaveRequest getSaveRequest(int id) {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            SaveRequest saveRequest = session.get(SaveRequest.class, id);
            org.hibernate.Hibernate.initialize(saveRequest);
            transaction.commit();
            return saveRequest;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

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

    public static <T extends WarnPunishment> Long saveAndReturnPunishment (T o) {
        if (o == null) {
            return null;
        }
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();
            long id = (long) session.save(o);

            transaction.commit();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            return null;
        }
    }

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
