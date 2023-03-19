package de.paradubsch.paradubschmanager.models;

import de.craftery.PlayerData;
import de.craftery.util.BaseDatabaseEntity;
import de.craftery.util.HibernateConfigurator;
import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "backpacks")
@SequenceGenerator(name="storageIdSequence",sequenceName="storage_id_sequence", allocationSize = 1)
public class Backpack extends BaseDatabaseEntity<Backpack, Long> {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="storageIdSequence")
    @Column(name = "storage_id")
    private long id;

    @Column(name = "uuid", columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
    private String playerRef;

    @Column(name = "max_pages", columnDefinition = "BIGINT DEFAULT 1")
    private long maxPages = 1;

    @Transient
    private transient List<ItemStack> items;

    @Transient
    private transient List<StorageSlot> slots;

    @Override
    public Long getIdentifyingColumn() {
        return this.id;
    }

    private static Backpack getByUuid(String playerRef) {
        try {
            @Cleanup Session session = HibernateConfigurator.getSessionFactory().openSession();

            //HQL
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

    public static Backpack getByPlayer(PlayerData player) {
        return getByPlayer(player.getUuid(), player.getName());
    }
    public static Backpack getByPlayer(Player player) {
        return getByPlayer(player.getUniqueId().toString(), player.getName());
    }

    /**
     *
     * @param uuid String to get the backpack for
     * @return The backpack of the player
     */
    private static Backpack getByPlayer(String uuid, String name) {
        Backpack bp = getByUuid(uuid);
        if (bp == null) {
            bp = new Backpack();
            bp.setPlayerRef(uuid);
            bp.save();
            bp = getByUuid(uuid);
            if (bp == null) {
                throw new RuntimeException("Could not create backpack for player " + name);
            }
        }
        bp.slots = StorageSlot.getSlotsByStorageId(bp.getId());
        bp.items = StorageSlot.getItemStacksBySlotIds(bp.slots);
        return bp;
    }

    public static void storeByPlayer(Player player, Backpack backpack) {
        store(player.getName(), backpack);
    }

    public static void storeByPlayer(PlayerData player, Backpack backpack) {
        store(player.getName(), backpack);
    }

    /**
     *
     * @param playerName String name of the player. So in case of a failiue it can be logged
     * @param backpack Backpack to set for the player
     */
    private static void store(String playerName, Backpack backpack) {
        if (backpack.items.size() > backpack.maxPages * 27) {
            Bukkit.getLogger().warning("Player " + playerName + " tried to store " + backpack.items.size() + " items in a backpack with " + backpack.maxPages + " pages.");
        }
        List<StorageSlot> oldSlots = backpack.slots;
        for (ItemStack item : backpack.items) {
            if (item == null)
                continue;
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
