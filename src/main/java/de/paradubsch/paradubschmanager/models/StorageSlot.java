package de.paradubsch.paradubschmanager.models;

import de.paradubsch.paradubschmanager.config.HibernateConfigurator;
import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.inventory.ItemStack;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "storage_slots")
@SequenceGenerator(name="slotIdSequence",sequenceName="slot_id_sequence", allocationSize = 1)
public class StorageSlot extends BaseDatabaseEntity<StorageSlot, Long>{

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="slotIdSequence")
    @Column(name = "slot_id", nullable = false)
    private long id;

    @Column(name = "storage_id", nullable = false)
    private long storageId;

    @Column(name = "amount", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int amount;

    @Column(name = "item_hash", nullable = false, columnDefinition = "VARCHAR(40)")
    private String itemHash;

    @Override
    public Serializable getIdentifyingColumn() {
        return this.id;
    }

    private ItemStack getItemStack() {
        ItemStack itemStack = ItemData.getById(itemHash).getItem();
        itemStack.setAmount(amount);
        return itemStack;
    }

    public static List<StorageSlot> getSlotsByStorageId(long storageId) {
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            return session.createQuery("FROM StorageSlot where storageId = :storageId", StorageSlot.class)
                    .setParameter("storageId", storageId)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<ItemStack> getItemStacksBySlotIds(List<StorageSlot> slots) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (StorageSlot slot : slots) {
            itemStacks.add(slot.getItemStack());
        }
        return itemStacks;
    }
}
