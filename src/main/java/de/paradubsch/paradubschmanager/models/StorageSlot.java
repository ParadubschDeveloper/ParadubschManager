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
@Table(name = "storage_slots")
@SequenceGenerator(name="slotIdSequence",sequenceName="slot_id_sequence", allocationSize = 1)
public class StorageSlot {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="slotIdSequence")
    @Column(name = "slot_id", nullable = false)
    private long id;

    @Column(name = "storage_id", nullable = false)
    private long storageId;

    @Column(name = "amount", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int amount;

    @Column(name = "item_hash", nullable = false, columnDefinition = "VARCHAR(40)")
    private String itemHash;
}
