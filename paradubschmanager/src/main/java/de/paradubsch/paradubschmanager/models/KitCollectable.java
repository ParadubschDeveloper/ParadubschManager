package de.paradubsch.paradubschmanager.models;

import de.craftery.util.BaseDatabaseEntity;
import de.craftery.util.HibernateConfigurator;
import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "kit_collectables")
public class KitCollectable extends BaseDatabaseEntity<KitCollectable, Long> {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="storageIdSequence")
    @Column(name = "storage_id")
    private long id;

    @Column(name = "kit_id", nullable = false)
    private int kitId;

    @Column(name = "player", columnDefinition = "VARCHAR(36)")
    private String player;

    @Override
    public Long getIdentifyingColumn() {
        return this.id;
    }

    @Transient
    private transient @NotNull List<ItemStack> items = new ArrayList<>();

    @Transient
    private transient List<StorageSlot> slots;

    public static void renewKit(Player p, int kitId) {
        KitCollectable kit;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();
            kit = session.createQuery("FROM KitCollectable where kitId = :kitId AND player = :uuid", KitCollectable.class)
                    .setParameter("kitId", kitId)
                    .setParameter("uuid", p.getUniqueId().toString())
                    .getSingleResult();
        } catch (NoResultException e) {
            kit = new KitCollectable();
            kit.setKitId(kitId);
            kit.setPlayer(p.getUniqueId().toString());
            Long storageId = (Long) kit.save();
            kit.setId(storageId);
        } catch (Exception e) {
            e.printStackTrace();
            kit = new KitCollectable();
            kit.setKitId(kitId);
            kit.setPlayer(p.getUniqueId().toString());
            Long storageId = (Long) kit.save();
            kit.setId(storageId);
        }
        final long storageId = kit.getId();

        List<StorageSlot> slots = StorageSlot.getSlotsByStorageId(storageId);

        for (StorageSlot slot : slots) {
            slot.delete();
        }

        SavedKit.getByKitId(kitId).getKitItems().forEach(item -> {
            String hash = ItemData.getItemHash(item);

            StorageSlot slot = new StorageSlot();
            slot.setStorageId(storageId);
            slot.setItemHash(hash);
            slot.setAmount(item.getAmount());
            slot.save();
            ItemData itemData = new ItemData();
            itemData.setItem(item);
            itemData.setItemHash(hash);
            itemData.saveOrUpdate();
        });
    }

    public static KitCollectable getContents(Player p, int kitId) {
        KitCollectable kit;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();
            kit = session.createQuery("FROM KitCollectable where kitId = :kitId AND player = :uuid", KitCollectable.class)
                    .setParameter("kitId", kitId)
                    .setParameter("uuid", p.getUniqueId().toString())
                    .getSingleResult();
        } catch (NoResultException e) {
            return new KitCollectable();
        } catch (Exception e) {
            e.printStackTrace();
            return new KitCollectable();
        }
        kit.slots = StorageSlot.getSlotsByStorageId(kit.getId());
        kit.items = StorageSlot.getItemStacksBySlotIds(kit.slots);
        return kit;
    }

    public void redeemItem(ItemStack item) {
        String hash = ItemData.getItemHash(item);
        for (StorageSlot slot : this.slots) {
            if (slot.getItemHash().equals(hash) && slot.getAmount() == item.getAmount()) {
                slot.delete();
            }
        }
    }
}
