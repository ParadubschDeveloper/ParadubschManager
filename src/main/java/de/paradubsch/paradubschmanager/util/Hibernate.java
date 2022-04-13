package de.paradubsch.paradubschmanager.util;

import de.paradubsch.paradubschmanager.config.HibernateConfigurator;
import de.paradubsch.paradubschmanager.models.PlayerData;
import lombok.Cleanup;
import org.bukkit.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


public class Hibernate {
    public static void cachePlayerName(Player p) {
        new Thread(() -> {
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
        }).start();
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

    public static void save (Object o) {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

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
}
