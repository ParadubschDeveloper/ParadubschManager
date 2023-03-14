package de.paradubsch.paradubschmanager.models;

import de.craftery.PlayerData;
import de.craftery.util.BaseDatabaseEntity;
import de.craftery.util.HibernateConfigurator;
import de.craftery.util.ListCache;
import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "gs_whitelist_members")
public class GsWhitelistMember extends BaseDatabaseEntity<GsWhitelistMember, String> {
    @Id
    @Column(name = "gs_id_player_uuid", columnDefinition = "VARCHAR(196)")
    private String id;

    @Column(name = "gs_id", columnDefinition = "VARCHAR(127)")
    private String gsId;

    @Column(name = "uuid", columnDefinition = "VARCHAR(36)")
    private String uuid;

    @Override
    public String getIdentifyingColumn() {
        return this.id;
    }

    private static final ListCache<String, String> cache = new ListCache<>();

    public static boolean canJoin(String gsId, Player player) {
        return BaseDatabaseEntity.getById(GsWhitelistMember.class, gsId + "_" + player.getUniqueId()) != null;
    }

    public static void addPlayer(String gsId, String uuid) {
        cache.invalidate(gsId);
        GsWhitelistMember entity = BaseDatabaseEntity.getById(GsWhitelistMember.class, gsId + "_" + uuid);
        if (entity != null) return;
        entity = new GsWhitelistMember();
        entity.setGsId(gsId);
        entity.setUuid(uuid);
        entity.setId(gsId + "_" + uuid);
        entity.save();
    }

    public static void removePlayer(String gsId, String uuid) {
        cache.invalidate(gsId);
        GsWhitelistMember entity = BaseDatabaseEntity.getById(GsWhitelistMember.class, gsId + "_" + uuid);
        if (entity == null) return;
        entity.delete();
    }

    public static List<String> getPlayers(String gsId) {
        if (cache.isValid(gsId)) {
            return cache.get(gsId);
        }

        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            List<GsWhitelistMember> res = session.createQuery("FROM GsWhitelistMember where gsId = :gsId", GsWhitelistMember.class)
                    .setParameter("gsId", gsId)
                    .getResultList();
            List<String> names = res.stream().map(member -> PlayerData.getById(member.uuid).getName()).collect(Collectors.toList());
            cache.cache(gsId, names);
            return names;
        } catch (NoResultException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
