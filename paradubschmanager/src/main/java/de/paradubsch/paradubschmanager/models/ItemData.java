package de.paradubsch.paradubschmanager.models;

import de.craftery.craftinglib.util.BaseDatabaseEntity;
import de.craftery.craftinglib.util.ItemStackConverter;
import de.craftery.craftinglib.util.TestMocks;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "item_data")
public class ItemData extends BaseDatabaseEntity<ItemData, String> {

    @Id
    @Column(name = "item_hash", nullable = false, columnDefinition = "VARCHAR(40)")
    private String itemHash;

    @Column(name = "data")
    @Convert(converter = ItemStackConverter.class)
    private ItemStack item;

    @Override
    public String getIdentifyingColumn() {
        return this.itemHash;
    }

    public static ItemData getById(String hash) {
        return getById(ItemData.class, hash);
    }

    public static String getItemHash(@NotNull ItemStack item) {
        ItemStack clone = item.clone();
        clone.setAmount(1);
        byte[] bytes = TestMocks.serializeItemStack(clone);

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(bytes);
            BigInteger no = new BigInteger(1, messageDigest);

            StringBuilder hashText = new StringBuilder(no.toString(16));

            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
