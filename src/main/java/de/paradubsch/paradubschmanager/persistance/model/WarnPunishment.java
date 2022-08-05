package de.paradubsch.paradubschmanager.persistance.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

@Data
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "punishments")
@SequenceGenerator(name="punishmentSequence",sequenceName="punishment_sequence", allocationSize = 1)
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

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "punishmentRef", fetch = FetchType.LAZY)
    @Cascade(value = SAVE_UPDATE)
    private List<PunishmentUpdate> updates;
}
