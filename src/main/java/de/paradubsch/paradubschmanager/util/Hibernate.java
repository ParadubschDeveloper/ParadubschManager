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

import de.paradubsch.paradubschmanager.config.HibernateConfigurator;
import de.paradubsch.paradubschmanager.models.*;
import lombok.Cleanup;
import org.bukkit.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


/**
 * Deprecated. View File Header for more information.
 */
@Deprecated
public class Hibernate {
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
}