package de.paradubsch.paradubschmanager.models;


import de.craftery.util.BaseDatabaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "punishments")
@SequenceGenerator(name="punishmentSequence",sequenceName="punishment_sequence", allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "punishment_type", discriminatorType = DiscriminatorType.STRING)
public class WarnPunishment extends BaseDatabaseEntity<WarnPunishment, Long> {

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

    // TODO: Delete this relation, because it is not working in our Hibernate usecase
    @ManyToOne(fetch = FetchType.EAGER)
    private PlayerData givenBy;

    // TODO: Delete this relation, because it is not working in our Hibernate usecase
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "punishmentRef", fetch = FetchType.LAZY)
    @Cascade(value = SAVE_UPDATE)
    private List<PunishmentUpdate> updates;

    public static WarnPunishment getById(Long id) {
        return BaseDatabaseEntity.getById(WarnPunishment.class, id);
    }

    @Override
    public Serializable getIdentifyingColumn() {
        return this.id;
    }
}
