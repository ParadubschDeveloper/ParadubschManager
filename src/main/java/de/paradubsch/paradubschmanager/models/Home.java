package de.paradubsch.paradubschmanager.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "homes")
@SequenceGenerator(name="homesSequence",sequenceName="homes_sequence", allocationSize = 1)
public class Home extends BaseDatabaseEntity<Home, Long> {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="homesSequence")
    @Column(name = "home_id")
    private long id;

    @Column(name = "home_name", columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "home_x", columnDefinition = "BIGINT")
    private long x;

    @Column(name = "home_y", columnDefinition = "BIGINT")
    private long y;

    @Column(name = "home_z", columnDefinition = "BIGINT")
    private long z;

    @Column(name = "home_world", columnDefinition = "VARCHAR(128)")
    private String world;

    @ManyToOne(fetch = FetchType.EAGER)
    private PlayerData playerRef;

    public static Home getById(Serializable id) {
        return BaseDatabaseEntity.getById(Home.class, id);
    }

    @Override
    public Serializable getIdentifyingColumn() {
        return this.id;
    }
}
