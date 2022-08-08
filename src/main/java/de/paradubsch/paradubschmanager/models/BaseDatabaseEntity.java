package de.paradubsch.paradubschmanager.models;

import de.paradubsch.paradubschmanager.config.HibernateConfigurator;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDatabaseEntity<P extends BaseDatabaseEntity<?, ?>, ID extends Serializable> {
    public Serializable save() {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Serializable returner = session.save(this);
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
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    protected static <T extends BaseDatabaseEntity<?, ?>> T getById(Class<T> clazz, Serializable id) {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            T returner = session.get(clazz, id);
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
}
