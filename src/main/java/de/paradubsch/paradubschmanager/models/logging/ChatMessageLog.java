package de.paradubsch.paradubschmanager.models.logging;

import de.craftery.util.BaseDatabaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "log_chat_messages")
public class ChatMessageLog extends BaseDatabaseEntity<ChatMessageLog, Long> {
    @Id
    @Column(name = "log_id")
    private long id;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Override
    public Long getIdentifyingColumn() {
        return this.id;
    }

    public static void logMessage(Player player, String message) {
        Long id = LogEntry.createBaseLog(player.getUniqueId().toString(), LogType.CHAT_MESSAGE);
        ChatMessageLog chatMessageLog = new ChatMessageLog();
        chatMessageLog.setId(id);
        chatMessageLog.setMessage(message);
        chatMessageLog.save();
    }
}
