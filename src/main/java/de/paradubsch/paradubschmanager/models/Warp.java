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
@Table(name = "warps")
public class Warp {
    @Id
    @Column(name = "name", columnDefinition = "VARCHAR(96)", nullable = false)
    private String name;

    @Column(name = "createdAt")
    @CreationTimestamp
    private Timestamp creationTimestamp;

    @Column(name = "world", columnDefinition = "VARCHAR(64)", nullable = false)
    private String world;

    @Column(name = "x", nullable = false)
    private double x;

    @Column(name = "y", nullable = false)
    private double y;

    @Column(name = "z", nullable = false)
    private double z;

    @Column(name = "pitch", nullable = false)
    private float pitch;

    @Column(name = "yaw", nullable = false)
    private float yaw;
}
