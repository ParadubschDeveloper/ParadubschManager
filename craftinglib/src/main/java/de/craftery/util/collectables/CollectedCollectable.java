package de.craftery.util.collectables;

import de.craftery.util.BaseDatabaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "collected_collectables")
public class CollectedCollectable extends BaseDatabaseEntity<CollectedCollectable, String> {
    @Id
    @Column(name = "id_uuid_pair", columnDefinition = "VARCHAR(196)")
    private String pair;

    @Column(name = "collectable_id", nullable = false)
    private long id;

    @Column(name = "collector_uuid", columnDefinition = "VARCHAR(36)", nullable = false)
    private String uuid;

    @Override
    public String getIdentifyingColumn() {
        return this.pair;
    }
}
