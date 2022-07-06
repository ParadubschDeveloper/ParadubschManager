package de.paradubsch.paradubschmanager.models;


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
@Table(name = "punishments")
@SequenceGenerator(name="punishmentSequence",sequenceName="punishment_sequence")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "punishment_type", discriminatorType = DiscriminatorType.STRING)
public class WarnPunishment {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="punishmentSequence")
    @Column(name = "punishment_id")
    private long id;

    @ManyToOne
    private PunishmentHolder holderRef;

    @Column(name = "createdAt")
    @CreationTimestamp
    private Timestamp creationTimestamp;

    @Column(name = "reason", columnDefinition = "VARCHAR(196)", nullable = false)
    private String reason;

    @ManyToOne(fetch = FetchType.EAGER)
    private PlayerData givenBy;
}
