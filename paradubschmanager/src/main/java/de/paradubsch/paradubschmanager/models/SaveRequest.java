package de.paradubsch.paradubschmanager.models;

import de.craftery.PlayerData;
import de.craftery.util.BaseDatabaseEntity;
import de.craftery.util.HibernateConfigurator;
import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "saveRequests")
@SequenceGenerator(name="saveRequestSequence",sequenceName="save_request_sequence", allocationSize = 1)
public class SaveRequest extends BaseDatabaseEntity<SaveRequest, Integer> implements Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="saveRequestSequence")
    @Column(name = "save_id")
    private int id;

    @Column(name = "loc_x", columnDefinition = "BIGINT")
    private long x;

    @Column(name = "loc_y", columnDefinition = "BIGINT")
    private long y;

    @Column(name = "loc_z", columnDefinition = "BIGINT")
    private long z;

    @Column(name = "loc_world", columnDefinition = "VARCHAR(128)")
    private String world;

    @Column(name = "playerRef_uuid", columnDefinition = "VARCHAR(36)")
    private String playerRef;

    public static SaveRequest getById(Serializable id) {
        return BaseDatabaseEntity.getById(SaveRequest.class, id);
    }

    public static List<SaveRequest> getAll() {
        return BaseDatabaseEntity.getAll(SaveRequest.class);
    }

    public String getRefName() {
        return PlayerData.getById(playerRef).getName();
    }

    public static SaveRequest getByPlayer(Player p) {
        Transaction transaction = null;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            transaction = session.beginTransaction();

            PlayerData playerData = PlayerData.getByPlayer(p);
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

    @Override
    public Serializable getIdentifyingColumn() {
        return this.id;
    }
}
