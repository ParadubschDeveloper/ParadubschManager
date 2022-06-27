package de.paradubsch.paradubschmanager.models;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "saveRequests")
public class SaveRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
}