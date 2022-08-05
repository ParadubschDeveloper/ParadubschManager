package de.paradubsch.paradubschmanager.persistance.model;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "punishments_updates")
@SequenceGenerator(name="punishmentUpdateSequence",sequenceName="punishment_update_sequence", allocationSize = 1)
public class PunishmentUpdate {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="punishmentUpdateSequence")
    @Column(name = "update_id")
    private long id;

    @ManyToOne
    private WarnPunishment punishmentRef;

    @Column(name = "createdAt")
    @CreationTimestamp
    private Timestamp creationTimestamp;

    @Column(name = "reason", columnDefinition = "VARCHAR(196)", nullable = false)
    private String reason;

    @ManyToOne(fetch = FetchType.EAGER)
    private PlayerData givenBy;

    @Column(name = "permanent", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean permanent = false;

    @Column
    private Timestamp expiration;
}
