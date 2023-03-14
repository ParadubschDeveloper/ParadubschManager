package de.paradubsch.paradubschmanager.models;

import de.craftery.util.BaseDatabaseEntity;
import de.craftery.util.HibernateConfigurator;
import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.inventory.ItemStack;
import org.hibernate.Session;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "saved_kits")
public class SavedKit extends BaseDatabaseEntity<SavedKit, Long> {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="storageIdSequence")
    @Column(name = "storage_id")
    private long id;

    @Column(name = "kit_id", unique = true)
    private int kitId;

    @Override
    public Long getIdentifyingColumn() {
        return this.id;
    }

    @Transient
    private transient @NotNull List<ItemStack> kitItems = new ArrayList<>();

    public static SavedKit getByKitId(int kitId) {
        SavedKit kit;
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();
            kit = session.createQuery("FROM SavedKit where kitId = :kitId", SavedKit.class)
                    .setParameter("kitId", kitId)
                    .getSingleResult();
        } catch (NoResultException e) {
            kit = new SavedKit();
            kit.setKitId(kitId);
            Long storageId = (Long) kit.save();
            kit.setId(storageId);
        } catch (Exception e) {
            e.printStackTrace();
            kit = new SavedKit();
            kit.setKitId(kitId);
            Long storageId = (Long) kit.save();
            kit.setId(storageId);
        }
        List<StorageSlot> slots = StorageSlot.getSlotsByStorageId(kit.getId());
        kit.kitItems = StorageSlot.getItemStacksBySlotIds(slots);

        return kit;
    }

    public static void saveKit(SavedKit kit) {
        List<StorageSlot> slots = StorageSlot.getSlotsByStorageId(kit.getId());
        for (StorageSlot slot : slots) {
            slot.delete();
        }
        for (ItemStack item : kit.kitItems) {
            String hash = ItemData.getItemHash(item);

            StorageSlot slot = new StorageSlot();
            slot.setStorageId(kit.getId());
            slot.setItemHash(hash);
            slot.setAmount(item.getAmount());
            slot.save();
            ItemData itemData = new ItemData();
            itemData.setItem(item);
            itemData.setItemHash(hash);
            itemData.saveOrUpdate();
        }
    }

}
