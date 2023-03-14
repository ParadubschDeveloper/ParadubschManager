package de.paradubsch.paradubschmanager.models.logging;

import de.craftery.util.BaseDatabaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "logs")
@SequenceGenerator(name="logIdSequence",sequenceName="log_id_sequence", allocationSize = 1)
public class LogEntry extends BaseDatabaseEntity<LogEntry, Long> {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="logIdSequence")
    @Column(name = "log_id", updatable = false)
    private long id;

    @Column(name = "target", columnDefinition = "VARCHAR(36)", nullable = false, updatable = false)
    private String playerRef;

    @Column(name = "creation_time")
    @CreationTimestamp
    private Timestamp creationTimestamp;

    @Column(name = "log_type", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private LogType logType;

    @Override
    public Long getIdentifyingColumn() {
        return this.id;
    }

    public static Long createBaseLog(String playerRef, LogType logType) {
        LogEntry logEntry = new LogEntry();
        logEntry.setPlayerRef(playerRef);
        logEntry.setLogType(logType);
        return (Long) logEntry.save();
    }
}
