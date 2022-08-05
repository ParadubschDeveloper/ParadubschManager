package de.paradubsch.paradubschmanager.persistance.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

@Data
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "punishment_holder")
public class PunishmentHolder {
    @Id
    @Column(name = "uuid", columnDefinition = "VARCHAR(36)")
    private String uuid;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne
    private PlayerData playerRef;

    @Column(name = "active_ban", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean activeBan = false;

    @Column(name = "active_mute", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean activeMute = false;

    @Column(name = "active_ban_id")
    private long activeBanId;

    @Column(name = "active_mute_id")
    private long activeMuteId;

    @Column(name = "active_ban_reason", columnDefinition = "VARCHAR(196)")
    private String activeBanReason;

    @Column(name = "active_mute_reason", columnDefinition = "VARCHAR(196)")
    private String activeMuteReason;

    @Column(name = "active_ban_expiration")
    private Timestamp activeBanExpiration;

    @Column(name = "active_mute_expiration")
    private Timestamp activeMuteExpiration;

    @Column(name = "is_perma_banned", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isPermaBanned = false;

    @Column(name = "is_perma_muted", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isPermaMuted = false;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "holderRef", fetch = FetchType.LAZY)
    @Cascade(value = SAVE_UPDATE)
    private List<BanPunishment> bans;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "holderRef", fetch = FetchType.LAZY)
    @Cascade(value = SAVE_UPDATE)
    private List<MutePunishment> mutes;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "holderRef", fetch = FetchType.LAZY)
    @Cascade(value = SAVE_UPDATE)
    private List<WarnPunishment> warns;

}
