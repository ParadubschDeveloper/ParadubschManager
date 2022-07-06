package de.paradubsch.paradubschmanager.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class MutePunishment extends WarnPunishment {

    @Column(name = "permanent", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean permanent = false;

    @Column(name = "has_update", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean hasUpdate = false;

}
