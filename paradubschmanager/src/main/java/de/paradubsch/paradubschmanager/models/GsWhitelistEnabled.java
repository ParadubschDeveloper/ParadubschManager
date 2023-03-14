package de.paradubsch.paradubschmanager.models;

import de.craftery.util.BaseDatabaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "gs_whitelist_enabled")
@AllArgsConstructor
public class GsWhitelistEnabled extends BaseDatabaseEntity<GsWhitelistEnabled, String> {
    @Id
    @Column(name = "gs_id", columnDefinition = "VARCHAR(127)")
    private String id;

    protected GsWhitelistEnabled() {}

    @Override
    public String getIdentifyingColumn() {
        return this.id;
    }

    public static boolean check(String gsId) {
        return BaseDatabaseEntity.getById(GsWhitelistEnabled.class, gsId) != null;
    }

    public static void enable(String gsId) {
        if (check(gsId)) return;
        GsWhitelistEnabled gsWhitelistEnabled = new GsWhitelistEnabled(gsId);
        gsWhitelistEnabled.save();
    }

    public static void disable(String gsId) {
        GsWhitelistEnabled gsWhitelistEnabled = BaseDatabaseEntity.getById(GsWhitelistEnabled.class, gsId);
        if (gsWhitelistEnabled == null) return;
        gsWhitelistEnabled.delete();
    }
}
