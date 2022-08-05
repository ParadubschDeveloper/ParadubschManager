package de.paradubsch.paradubschmanager.persistance.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Timestamp;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class MutePunishment extends WarnPunishment {

    @Column(name = "permanent", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean permanent = false;

    @Column
    private Timestamp expiration;

    @Column(name = "has_update", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean hasUpdate = false;

}
