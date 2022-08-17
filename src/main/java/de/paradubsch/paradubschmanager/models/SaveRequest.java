package de.paradubsch.paradubschmanager.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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

    @OneToOne(fetch = FetchType.EAGER)
    private PlayerData playerRef;

    public static SaveRequest getById(Serializable id) {
        return BaseDatabaseEntity.getById(SaveRequest.class, id);
    }

    public static List<SaveRequest> getAll() {
        return BaseDatabaseEntity.getAll(SaveRequest.class);
    }

    @Override
    public Serializable getIdentifyingColumn() {
        return this.id;
    }
}
