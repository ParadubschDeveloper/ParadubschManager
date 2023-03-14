package de.paradubsch.paradubschmanager.lifecycle.jobs;

import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.models.WorkerPlayer;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;


public class MiningJob {
    public static void onBlockBreak(WorkerPlayer worker, BlockBreakEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null &&
                event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getEnchants().keySet().stream().anyMatch(ench -> ench.equals(Enchantment.SILK_TOUCH))) {
            return;
        }

        int earn = 0;
        boolean progress = false;
        switch (event.getBlock().getType()) {
            case NETHER_GOLD_ORE:
            case NETHER_QUARTZ_ORE:
            case COAL_ORE:
            case DEEPSLATE_COAL_ORE:
            case IRON_ORE:
            case DEEPSLATE_IRON_ORE:
            case COPPER_ORE:
            case DEEPSLATE_COPPER_ORE: {
                worker.setExperience(worker.getExperience() + 1);
                earn = 1;
                progress = true;
                break;
            }
            case GOLD_ORE:
            case DEEPSLATE_GOLD_ORE:
            case REDSTONE_ORE:
            case DEEPSLATE_REDSTONE_ORE:
            case LAPIS_ORE:
            case DEEPSLATE_LAPIS_ORE:{
                worker.setExperience(worker.getExperience() + 2);
                earn = 2;
                progress = true;
                break;
            }
            case DIAMOND_ORE:
            case DEEPSLATE_DIAMOND_ORE:
            case EMERALD_ORE:
            case DEEPSLATE_EMERALD_ORE: {
                worker.setExperience(worker.getExperience() + 5);
                earn = 5;
                progress = true;
                break;
            }
            case ANCIENT_DEBRIS: {
                worker.setExperience(worker.getExperience() + 15);
                earn = 15;
                progress = true;
                break;
            }

        }
        if (!progress) {
            return;
        }
        int realEarn = (int) (earn * worker.getJobLevel().getMultiplier());

        PlayerData pd = PlayerData.getById(worker.getUuid());
        pd.setMoney(pd.getMoney() + realEarn);

        JobLevel nextLevel = worker.getJobLevel().nextLevel();
        if (worker.getExperience() >= 250L * worker.getJobLevel().getDifficulty()) {
            if (nextLevel != JobLevel.MAX) {
                worker.setJobLevel(nextLevel);
                nextLevel = nextLevel.nextLevel();
            }
        }

        JobManager.sendActionBar(event.getPlayer(), "§a" + worker.getJobLevel().getName() + " " +
                worker.getExperience() + "§7/§a" +
                250L * worker.getJobLevel().getDifficulty() + " " + nextLevel.getName() + ChatColor.GOLD + " +" + realEarn + "€");
        worker.cacheChanges();
        worker.scheduleFlushing(20*20);
        pd.cacheChanges();
        pd.scheduleFlushing(20*20);
    }
}
