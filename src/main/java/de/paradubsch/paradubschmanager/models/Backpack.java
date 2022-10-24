package de.paradubsch.paradubschmanager.models;

import de.paradubsch.paradubschmanager.config.HibernateConfigurator;
import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "backpacks")
@SequenceGenerator(name="storageIdSequence",sequenceName="storage_id_sequence", allocationSize = 1)
public class Backpack extends BaseDatabaseEntity<Backpack, Long> {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="storageIdSequence")
    @Column(name = "storage_id")
    private long id;

    @Column(name = "uuid", columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
    private String playerRef;

    @Column(name = "max_slots", columnDefinition = "BIGINT DEFAULT 27")
    private long maxSlots = 27;

    @Transient
    private transient List<ItemStack> items;

    @Transient
    private transient List<StorageSlot> slots;

    @Override
    public String getIdentifyingColumn() {
        return this.playerRef;
    }

    private static Backpack getByUuid(String playerRef) {
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            return session.createQuery("FROM Backpack where playerRef = :uuid", Backpack.class)
                    .setParameter("uuid", playerRef)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param player Player to get the backpack for
     * @return The backpack of the player
     */
    public static Backpack getByPlayer(Player player) {
        Backpack bp = getByUuid(player.getUniqueId().toString());
        if (bp == null) {
            bp = new Backpack();
            bp.setPlayerRef(player.getUniqueId().toString());
            bp.save();
            bp = getByUuid(player.getUniqueId().toString());
            if (bp == null) {
                throw new RuntimeException("Could not create backpack for player " + player.getName());
            }
        }
        bp.slots = StorageSlot.getSlotsByStorageId(bp.getId());
        bp.items = StorageSlot.getItemStacksBySlotIds(bp.slots);
        return bp;
    }

    /**
     *
     * @param player Player to get the backpack for
     * @param backpack Backpack to set for the player
     */
    public static void storeByPlayer(Player player, Backpack backpack) {
        if (backpack.items.size() > backpack.maxSlots) {
            Bukkit.getLogger().warning("Player " + player.getName() + " tried to store " + backpack.items.size() + " items in a backpack with " + backpack.maxSlots + " slots.");
        }
        List<StorageSlot> oldSlots = backpack.slots;
        for (ItemStack item : backpack.items) {
            int amount = item.getAmount();
            String hash = ItemData.getItemHash(item);

            boolean found = false;
            List<StorageSlot> iterable = new ArrayList<>(oldSlots);
            for (StorageSlot slot : iterable) {
                if (slot.getItemHash().equals(hash)) {
                    if (slot.getAmount() != amount) {
                        slot.setAmount(amount);
                        slot.saveOrUpdate();
                    }
                    oldSlots.remove(slot);
                    found = true;
                    break;
                }
            }

            if (!found) {
                StorageSlot slot = new StorageSlot();
                slot.setStorageId(backpack.getId());
                slot.setItemHash(hash);
                slot.setAmount(amount);
                slot.save();
                ItemData itemData = new ItemData();
                itemData.setItem(item);
                itemData.setItemHash(hash);
                itemData.saveOrUpdate();
            }
        }
        for (StorageSlot slot : oldSlots) {
            slot.delete();
        }
    }
}
