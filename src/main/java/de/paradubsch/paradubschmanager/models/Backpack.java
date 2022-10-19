package de.paradubsch.paradubschmanager.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "backpacks")
@SequenceGenerator(name="storageIdSequence",sequenceName="storage_id_sequence", allocationSize = 1)
public class Backpack extends BaseDatabaseEntity<Backpack, String> {
    @Id
    @Column(name = "uuid", columnDefinition = "VARCHAR(36)", nullable = false)
    private String playerRef;

    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="storageIdSequence")
    @Column(name = "storage_id")
    private long id;

    @Column(name = "max_slots", columnDefinition = "BIGINT DEFAULT 27")
    private long maxSlots = 27;

    @Override
    public String getIdentifyingColumn() {
        return this.playerRef;
    }
}
