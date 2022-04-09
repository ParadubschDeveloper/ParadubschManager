package de.paradubsch.paradubschmanager.util;

import de.paradubsch.paradubschmanager.config.HibernateConfigurator;
import de.paradubsch.paradubschmanager.models.PlayerData;
import org.bukkit.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Hibernate {
    public static void cachePlayerName(Player p) {
        new Thread(() -> {
            Transaction transaction = null;
            try (Session session = HibernateConfigurator.getSessionFactory().openSession()) {

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
        }).start();
    }

    public static PlayerData getPlayerData(Player p) {
        Transaction transaction = null;
        try (Session session = HibernateConfigurator.getSessionFactory().openSession()) {

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

    public static void save (Object o) {
        Transaction transaction = null;
        try (Session session = HibernateConfigurator.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.saveOrUpdate(o);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
