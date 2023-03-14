package de.paradubsch.paradubschmanager.models;

import de.craftery.util.BaseDatabaseEntity;
import de.paradubsch.paradubschmanager.lifecycle.jobs.JobLevel;
import de.paradubsch.paradubschmanager.lifecycle.jobs.JobType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "job_holder")
public class WorkerPlayer extends BaseDatabaseEntity<WorkerPlayer, String> {
    @Id
    @Column(name = "uuid", columnDefinition = "VARCHAR(36)")
    private String uuid;

    @Column(name = "job", nullable = false)
    @Enumerated(EnumType.STRING)
    private JobType job;

    @Column(name = "job_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private JobLevel jobLevel = JobLevel.ONE;

    @Column(name = "experience", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private long experience = 0;

    public static WorkerPlayer getById(Serializable id) {
        return BaseDatabaseEntity.getById(WorkerPlayer.class, id);
    }
    public static WorkerPlayer getByIdOrCreate(String uuid) {
        WorkerPlayer workee = BaseDatabaseEntity.getById(WorkerPlayer.class, uuid);
        if (workee == null) {
            workee = new WorkerPlayer();
            workee.setUuid(uuid);
            workee.setJob(JobType.UNEMPLOYED);
            workee.setJobLevel(JobLevel.ONE);
            workee.save();
        }
        return workee;
    }

    @Override
    public Serializable getIdentifyingColumn() {
        return this.uuid;
    }
}
