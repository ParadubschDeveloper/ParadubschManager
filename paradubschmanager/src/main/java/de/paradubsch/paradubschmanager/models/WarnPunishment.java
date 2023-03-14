package de.paradubsch.paradubschmanager.models;


import de.craftery.util.BaseDatabaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "punishments")
@SequenceGenerator(name="punishmentSequence",sequenceName="punishment_sequence", allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "punishment_type", discriminatorType = DiscriminatorType.STRING)
public class WarnPunishment extends BaseDatabaseEntity<WarnPunishment, Long> {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="punishmentSequence")
    @Column(name = "punishment_id")
    private long id;

    @Column(name = "holderRef_uuid", columnDefinition = "VARCHAR(36)")
    private String holderRef;

    @Column(name = "createdAt")
    @CreationTimestamp
    private Timestamp creationTimestamp;

    @Column(name = "reason", columnDefinition = "VARCHAR(196)", nullable = false)
    private String reason;

    @Column(name = "givenBy_uuid", columnDefinition = "VARCHAR(36)")
    private String givenBy;

    public static WarnPunishment getById(Long id) {
        return BaseDatabaseEntity.getById(WarnPunishment.class, id);
    }

    @Override
    public Serializable getIdentifyingColumn() {
        return this.id;
    }
}
