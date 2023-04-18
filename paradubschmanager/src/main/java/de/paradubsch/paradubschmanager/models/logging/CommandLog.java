package de.paradubsch.paradubschmanager.models.logging;

import de.craftery.craftinglib.util.BaseDatabaseEntity;
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
@Table(name = "log_commands")
public class CommandLog extends BaseDatabaseEntity<CommandLog, Long> {
    @Id
    @Column(name = "log_id")
    private long id;

    @Column(name = "command", columnDefinition = "VARCHAR(256)", nullable = false)
    private String command;

    @Override
    public Long getIdentifyingColumn() {
        return this.id;
    }

    public static void logCommandExecution(Player player, String command) {
        Long id = LogEntry.createBaseLog(player.getUniqueId().toString(), LogType.COMMAND);
        CommandLog chatMessageLog = new CommandLog();
        chatMessageLog.setId(id);
        chatMessageLog.setCommand(command);
        chatMessageLog.save();
    }
}
