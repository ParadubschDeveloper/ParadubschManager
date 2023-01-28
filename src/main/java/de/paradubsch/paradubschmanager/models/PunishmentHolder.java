package de.paradubsch.paradubschmanager.models;

import de.craftery.util.BaseDatabaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "punishment_holder")
public class PunishmentHolder extends BaseDatabaseEntity<PunishmentHolder, String> {
    @Id
    @Column(name = "uuid", columnDefinition = "VARCHAR(36)")
    private String uuid;

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

    public static PunishmentHolder getById(Serializable id) {
        return BaseDatabaseEntity.getById(PunishmentHolder.class, id);
    }

    public static PunishmentHolder getByPlayerOrCreate(Player player) {
        PunishmentHolder holder = PunishmentHolder.getById(player.getUniqueId().toString());
        if (holder == null) {
            holder = new PunishmentHolder();
            holder.setUuid(player.getUniqueId().toString());
            holder.save();
        }
        return holder;
    }

    public static PunishmentHolder getByPlayerDataOrCreate(PlayerData playerData) {
        PunishmentHolder holder = PunishmentHolder.getById(playerData.getUuid());
        if (holder == null) {
            holder = new PunishmentHolder();
            holder.setUuid(playerData.getUuid());
            holder.save();
        }
        return holder;
    }

    @Override
    public Serializable getIdentifyingColumn() {
        return this.uuid;
    }
}
