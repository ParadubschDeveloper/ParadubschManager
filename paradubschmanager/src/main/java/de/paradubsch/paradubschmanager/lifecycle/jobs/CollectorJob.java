package de.paradubsch.paradubschmanager.lifecycle.jobs;

import com.jeff_media.customblockdata.CustomBlockData;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.craftery.craftinglib.PlayerData;
import de.paradubsch.paradubschmanager.models.WorkerPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CollectorJob {
    public static void onBlockBreak(WorkerPlayer worker, BlockBreakEvent event) {
        PersistentDataContainer customBlockData = new CustomBlockData(event.getBlock(), ParadubschManager.getInstance());
        if (customBlockData.has(JobManager.NOT_ORIGINAL, PersistentDataType.BYTE)) {
            return;
        }

        int earn = 0;
        boolean progress = false;
        switch (event.getBlock().getType()) {
            case DANDELION:
            case POPPY:
            case AZURE_BLUET:
            case OXEYE_DAISY:
            case CRIMSON_FUNGUS:
            case WARPED_FUNGUS:
            case SUNFLOWER:
            case ROSE_BUSH:
            case PEONY:
            case LILAC: {
                worker.setExperience(worker.getExperience() + 1);
                earn = 1;
                progress = true;
                break;
            }
            case CORNFLOWER:
            case BLUE_ORCHID:
            case ALLIUM:
            case RED_TULIP:
            case ORANGE_TULIP:
            case WHITE_TULIP:
            case PINK_TULIP:
            case LILY_OF_THE_VALLEY:
            case BROWN_MUSHROOM:
            case RED_MUSHROOM: {
                worker.setExperience(worker.getExperience() + 2);
                earn = 2;
                progress = true;
                break;
            }
            case WITHER_ROSE: {
                worker.setExperience(worker.getExperience() + 3);
                earn = 3;
                progress = true;
                break;
            }
            case SPORE_BLOSSOM: {
                worker.setExperience(worker.getExperience() + 5);
                earn = 5;
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
