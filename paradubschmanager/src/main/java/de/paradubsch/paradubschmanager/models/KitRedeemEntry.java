package de.paradubsch.paradubschmanager.models;

import de.craftery.util.BaseDatabaseEntity;
import de.craftery.util.HibernateConfigurator;
import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.hibernate.Session;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "kit_redeems")
public class KitRedeemEntry extends BaseDatabaseEntity<KitRedeemEntry, Long> {
    @Id
    @Column(name = "hash", columnDefinition = "VARCHAR(40)")
    private String hash;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @Override
    public String getIdentifyingColumn() {
        return this.hash;
    }

    public static long checkLastRedeemed(Player player, int kitId) {
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();
            KitRedeemEntry res = session.createQuery("FROM KitRedeemEntry WHERE hash = :hash", KitRedeemEntry.class)
                    .setParameter("hash", player.getUniqueId().toString() + kitId)
                    .getSingleResult();
            return res.getTimestamp().getTime();
        } catch (NoResultException e) {
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void redeem(Player player, int kitId) {
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();
            KitRedeemEntry res = session.createQuery("FROM KitRedeemEntry WHERE hash = :hash", KitRedeemEntry.class)
                    .setParameter("hash", player.getUniqueId().toString() + kitId)
                    .getSingleResult();
            res.setTimestamp(new Timestamp(System.currentTimeMillis()));
            res.saveOrUpdate();
        } catch (NoResultException e) {
            KitRedeemEntry entry = new KitRedeemEntry();
            entry.setHash(player.getUniqueId().toString() + kitId);
            entry.setTimestamp(new Timestamp(System.currentTimeMillis()));
            entry.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
