package de.paradubsch.paradubschmanager.models;

import de.craftery.craftinglib.PlayerData;
import de.craftery.craftinglib.util.BaseDatabaseEntity;
import de.craftery.craftinglib.util.HibernateConfigurator;
import de.craftery.craftinglib.util.ListCache;
import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "gs_ban_members")
public class GsBanMember extends BaseDatabaseEntity<GsBanMember, String> {
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
        return BaseDatabaseEntity.getById(GsBanMember.class, gsId + "_" + player.getUniqueId()) == null;
    }

    public static void banPlayer(String gsId, String uuid) {
        cache.invalidate(gsId);
        GsBanMember entity = BaseDatabaseEntity.getById(GsBanMember.class, gsId + "_" + uuid);
        if (entity != null) return;
        entity = new GsBanMember();
        entity.setGsId(gsId);
        entity.setUuid(uuid);
        entity.setId(gsId + "_" + uuid);
        entity.save();
    }

    public static void pardonPlayer(String gsId, String uuid) {
        cache.invalidate(gsId);
        GsBanMember entity = BaseDatabaseEntity.getById(GsBanMember.class, gsId + "_" + uuid);
        if (entity == null) return;
        entity.delete();
    }

    public static List<String> getPlayers(String gsId) {
        if (cache.isValid(gsId)) {
            return cache.get(gsId);
        }

        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            List<GsBanMember> res = session.createQuery("FROM GsBanMember where gsId = :gsId", GsBanMember.class)
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
