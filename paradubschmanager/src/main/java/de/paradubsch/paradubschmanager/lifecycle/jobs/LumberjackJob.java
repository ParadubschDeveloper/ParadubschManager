package de.paradubsch.paradubschmanager.lifecycle.jobs;

import com.jeff_media.customblockdata.CustomBlockData;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.craftery.PlayerData;
import de.paradubsch.paradubschmanager.models.WorkerPlayer;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class LumberjackJob {
    public static void onBlockBreak(WorkerPlayer worker, BlockBreakEvent event) {
        PersistentDataContainer customBlockData = new CustomBlockData(event.getBlock(), ParadubschManager.getInstance());
        if (customBlockData.has(JobManager.NOT_ORIGINAL, PersistentDataType.BYTE)) {
            return;
        }

        int earnPercent = 0;
        boolean progress = false;
        switch (event.getBlock().getType()) {
            case OAK_LOG:
            case BIRCH_LOG:
            case WARPED_STEM:
            case CRIMSON_STEM: {
                worker.setExperience(worker.getExperience() + 2);
                earnPercent = 40;
                progress = true;
                break;
            }
            case ACACIA_LOG: {
                worker.setExperience(worker.getExperience() + 3);
                earnPercent = 60;
                progress = true;
                break;
            }
            case SPRUCE_LOG:
            case JUNGLE_LOG:
            case DARK_OAK_LOG:
                worker.setExperience(worker.getExperience() + 1);
                earnPercent = 30;
                progress = true;
                break;
        }
        if (!progress) {
            return;
        }

        Integer oldEarnPercent =
                ParadubschManager.getInstance().getJobManager()
                        .getProgressPartPercentage()
                        .get(UUID.fromString(worker.getUuid()));
        if (oldEarnPercent == null) {
            oldEarnPercent = 0;
        }
        int newEarnPercent = earnPercent + oldEarnPercent;
        int displayPercentage = newEarnPercent;
        boolean getMoney = false;
        if (newEarnPercent > 100) {
            displayPercentage = displayPercentage - 100;
            getMoney = true;
        }
        ParadubschManager.getInstance().getJobManager()
                .getProgressPartPercentage().put(UUID.fromString(worker.getUuid()), displayPercentage);

        JobLevel nextLevel = worker.getJobLevel().nextLevel();
        if (worker.getExperience() >= 1000L * worker.getJobLevel().getDifficulty()) {
            if (nextLevel != JobLevel.MAX) {
                worker.setJobLevel(nextLevel);
                nextLevel = nextLevel.nextLevel();
            }
        }

        int moneyToGet = 0;
        if (getMoney) {
            moneyToGet = (int) Math.ceil(worker.getJobLevel().getMultiplier());
        }
        if (nextLevel == JobLevel.MAX) {
            moneyToGet++;
        }

        if (getMoney) {
            PlayerData pd = PlayerData.getById(worker.getUuid());
            pd.setMoney(pd.getMoney() + moneyToGet);

            JobManager.sendActionBar(event.getPlayer(),
                    "§a" + worker.getJobLevel().getName() + " " +
                            worker.getExperience() + "§7/§a" + 1000L * worker.getJobLevel().getDifficulty() + " " +
                            nextLevel.getName() +
                            " " + JobManager.getPercentageBar(displayPercentage) + " " +
                            "§6 +" + moneyToGet + "€");

            pd.cacheChanges();
            pd.scheduleFlushing(20*20);
        } else {
            JobManager.sendActionBar(event.getPlayer(),
                    "§a" + worker.getJobLevel().getName() + " " +
                            worker.getExperience() + "§7/§a" + 1000L * worker.getJobLevel().getDifficulty() + " " +
                            nextLevel.getName() +
                            " " + JobManager.getPercentageBar(displayPercentage));
        }

        worker.cacheChanges();
        worker.scheduleFlushing(20*20);

    }
}
