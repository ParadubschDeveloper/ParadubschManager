package de.paradubsch.paradubschmanager.lifecycle.bazaar;

import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import de.paradubsch.paradubschmanager.util.lang.BaseMessageType;
import de.paradubsch.paradubschmanager.util.lang.Message;
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

    public static BaseMessageType translationForMaterial(Material mat) {
        switch (mat) {
            case OAK_LOG:
                return Message.Constant.OAK_LOG;
            case SPRUCE_LOG:
                return Message.Constant.SPRUCE_LOG;
            case BIRCH_LOG:
                return Message.Constant.BIRCH_LOG;
            case JUNGLE_LOG:
                return Message.Constant.JUNGLE_LOG;
            case ACACIA_LOG:
                return Message.Constant.ACACIA_LOG;
            case DARK_OAK_LOG:
                return Message.Constant.DARK_OAK_LOG;
            case DIRT:
                return Message.Constant.DIRT;
            case COBBLESTONE:
                return Message.Constant.COBBLESTONE;
            case STONE:
                return Message.Constant.STONE;
            case BLACKSTONE:
                return Message.Constant.BLACKSTONE;
            case DEEPSLATE:
                return Message.Constant.DEEPSLATE;
            case NETHER_BRICKS:
                return Message.Constant.NETHER_BRICKS;
            case END_STONE:
                return Message.Constant.END_STONE;
            case SAND:
                return Message.Constant.SAND;
            case GRAVEL:
                return Message.Constant.GRAVEL;
            case CLAY:
                return Message.Constant.CLAY;
            case SUGAR_CANE:
                return Message.Constant.SUGAR_CANE;
            case WHEAT:
                return Message.Constant.WHEAT;
            case NETHER_WART:
                return Message.Constant.NETHER_WART;
            case OAK_LEAVES:
                return Message.Constant.OAK_LEAVES;
            case SPRUCE_LEAVES:
                return Message.Constant.SPRUCE_LEAVES;
            case BIRCH_LEAVES:
                return Message.Constant.BIRCH_LEAVES;
            case JUNGLE_LEAVES:
                return Message.Constant.JUNGLE_LEAVES;
            case ACACIA_LEAVES:
                return Message.Constant.ACACIA_LEAVES;
            case DARK_OAK_LEAVES:
                return Message.Constant.DARK_OAK_LEAVES;
            case COCOA_BEANS:
                return Message.Constant.COCOA_BEANS;
            case CACTUS:
                return Message.Constant.CACTUS;
            case CHORUS_FRUIT:
                return Message.Constant.CHORUS_FRUIT;
            case REDSTONE:
                return Message.Constant.REDSTONE;
            case IRON_INGOT:
                return Message.Constant.IRON_INGOT;
            case GOLD_INGOT:
                return Message.Constant.GOLD_INGOT;
            case COAL:
                return Message.Constant.COAL;
            case EMERALD:
                return Message.Constant.EMERALD;
            case LAPIS_LAZULI:
                return Message.Constant.LAPIS_LAZULI;
            case NETHERITE_INGOT:
                return Message.Constant.NETHERITE_INGOT;
            case ROSE_BUSH:
                return Message.Constant.ROSE_BUSH;
            case DANDELION:
                return Message.Constant.DANDELION;
            case POPPY:
                return Message.Constant.POPPY;
            case INK_SAC:
                return Message.Constant.INK_SAC;
            case PRISMARINE_SHARD:
                return Message.Constant.PRISMARINE_SHARD;
            case BONE:
                return Message.Constant.BONE;
            case BLAZE_ROD:
                return Message.Constant.BLAZE_ROD;
            case STRING:
                return Message.Constant.STRING;
            case SHULKER_SHELL:
                return Message.Constant.SHULKER_SHELL;
            default:
                return Message.Constant.BLANK;
        }
    }
}
