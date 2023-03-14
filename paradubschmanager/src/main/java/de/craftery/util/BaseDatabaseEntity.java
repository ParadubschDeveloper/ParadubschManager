package de.craftery.util;

import de.craftery.CraftPlugin;
import lombok.Cleanup;
import org.bukkit.scheduler.BukkitRunnable;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDatabaseEntity<P extends BaseDatabaseEntity<?, ?>, ID extends Serializable> implements Cloneable {
    public abstract Serializable getIdentifyingColumn();
    public Serializable save() {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Serializable returner = session.save(this);
            CraftPlugin.getInstance().getCachingManager().cacheEntity(this.getClass(), this, this.getIdentifyingColumn());
            transaction.commit();
            return returner;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            return null;
        }
    }

    public void saveOrUpdate() {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.saveOrUpdate(this);
            CraftPlugin.getInstance().getCachingManager().cacheEntity(this.getClass(), this, this.getIdentifyingColumn());
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void delete() {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.delete(this);
            CraftPlugin.getInstance().getCachingManager().deleteEntity(this.getClass(), this.getIdentifyingColumn());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    protected static <T extends BaseDatabaseEntity<?, ?>> T getById(Class<T> clazz, Serializable id) {
        if (id == null) {
            return null;
        }

        T cached = CraftPlugin.getInstance().getCachingManager().getEntity(clazz, id);
        if (cached != null) {
            return cached;
        }

        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            T returner = session.get(clazz, id);
            CraftPlugin.getInstance().getCachingManager().cacheEntity(clazz, returner, id);
            transaction.commit();
            return returner;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            return null;
        }
    }
    protected static <T extends BaseDatabaseEntity<?, ?>> List<T> getAll(Class<T> clazz) {
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(clazz);
            Root<T> rootEntry = cq.from(clazz);
            CriteriaQuery<T> all = cq.select(rootEntry);

            TypedQuery<T> allQuery = session.createQuery(all);
            return allQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void cacheChanges() {
        CraftPlugin.getInstance().getCachingManager().cacheEntity(this.getClass(), this, this.getIdentifyingColumn());
    }

    private long flushTimeout = 0;
    private boolean flushingScheduled = false;
    public void scheduleFlushing(long ticks) {
        flushTimeout = ticks;
        if (!flushingScheduled) {
            flushingScheduled = true;

            var clazz = this.getClass();
            new BukkitRunnable() {
                @Override
                public void run() {
                    flushTimeout--;
                    if (flushTimeout <= 0) {
                        flushingScheduled = false;
                        var obj = CraftPlugin.getInstance().getCachingManager().getEntity(clazz, getIdentifyingColumn());
                        obj.saveOrUpdate();
                        cancel();
                    }
                }
            }.runTaskTimer(CraftPlugin.getInstance(), 0, 1);
        }
    }

    @Override
    public BaseDatabaseEntity<?, ?> clone() {
        try {
            return (BaseDatabaseEntity<?, ?>) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
