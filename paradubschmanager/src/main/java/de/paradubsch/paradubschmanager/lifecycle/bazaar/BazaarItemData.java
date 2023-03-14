package de.paradubsch.paradubschmanager.lifecycle.bazaar;

import lombok.Data;
import org.bukkit.Material;

@Data
public class BazaarItemData {
    private Material material;
    private String indexKey;
    private int amount;
    private int offer;
    private int buy;
}
