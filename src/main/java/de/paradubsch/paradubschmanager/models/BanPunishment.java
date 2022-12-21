package de.paradubsch.paradubschmanager.models;

import de.craftery.util.BaseDatabaseEntity;
import de.craftery.util.HibernateConfigurator;
import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class BanPunishment extends WarnPunishment {

    @Column(name = "permanent", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean permanent = false;

    @Column
    private Timestamp expiration;

    @Column(name = "has_update", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean hasUpdate = false;

    public static BanPunishment getByIdO(Serializable id) {
        return BaseDatabaseEntity.getById(BanPunishment.class, id);
    }

    public List<PunishmentUpdate> getBanUpdates() {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            BanPunishment playerData = session.get(BanPunishment.class, this.getIdentifyingColumn());
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
}
