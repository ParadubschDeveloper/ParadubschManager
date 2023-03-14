package de.paradubsch.paradubschmanager.models.logging;

import de.craftery.util.BaseDatabaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "log_kit_redeems")
public class KitRedeemLog extends BaseDatabaseEntity<KitRedeemLog, Long> {
    @Id
    @Column(name = "log_id")
    private long id;

    @Column(name = "kit_id", nullable = false)
    private int kitId;

    @Override
    public Long getIdentifyingColumn() {
        return this.id;
    }

    public static void logKitRedeem(Player player, Integer kitId) {
        Long id = LogEntry.createBaseLog(player.getUniqueId().toString(), LogType.KIT_REDEEM);
        KitRedeemLog chatMessageLog = new KitRedeemLog();
        chatMessageLog.setId(id);
        chatMessageLog.setKitId(kitId);
        chatMessageLog.save();
    }
}
