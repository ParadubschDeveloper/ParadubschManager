package de.paradubsch.paradubschmanager.lifecycle.bazaar;

import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class Bazaar {
    public static List<BazaarItemData> getBazaarConfigItems() {
        ConfigurationSection sec = ConfigurationManager.getConfig().getConfigurationSection("bazaar");
        List<BazaarItemData> items = new ArrayList<>();
        if (sec == null) return items;
        for (String key : sec.getKeys(false)) {
            String matString = ConfigurationManager.getString("bazaar." + key + ".item");
            Material mat = Material.getMaterial(matString);
            if (mat == null) continue;
            int amount = ConfigurationManager.getInt("bazaar." + key + ".qty");
            if (amount == 0) continue;
            int offer = ConfigurationManager.getInt("bazaar." + key + ".offer");
            if (offer == 0) continue;
            int buy = ConfigurationManager.getInt("bazaar." + key + ".buy");
            if (buy == 0) continue;
            BazaarItemData item = new BazaarItemData();
            item.setMaterial(mat);
            item.setAmount(amount);
            item.setOffer(offer);
            item.setBuy(buy);
            item.setIndexKey(key);
            items.add(item);
        }
        return items;
    }
}
