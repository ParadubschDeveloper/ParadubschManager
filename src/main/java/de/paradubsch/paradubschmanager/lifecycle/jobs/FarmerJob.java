package de.paradubsch.paradubschmanager.lifecycle.jobs;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.models.WorkerPlayer;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class FarmerJob {
    public static void onBlockBreak(WorkerPlayer worker, BlockBreakEvent event) {
        BlockData materialData = event.getBlock().getState().getBlockData();
        if (materialData instanceof Ageable) {
            Ageable ageable = (Ageable) materialData;
            if (ageable.getAge() != ageable.getMaximumAge()) {
                return;
            }
        } else {
            return;
        }

        int earnPercent = 0;
        boolean progress = false;
        switch (event.getBlock().getType()) {
            case WHEAT:
            case BEETROOTS:
            case CARROTS:
            case POTATOES:
            case NETHER_WART: {
                worker.setExperience(worker.getExperience() + 1);
                earnPercent = 10;
                progress = true;
                break;
            }
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
        if (worker.getExperience() >= 500L * worker.getJobLevel().getDifficulty()) {
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
                            worker.getExperience() + "§7/§a" + 500L * worker.getJobLevel().getDifficulty() + " " +
                            nextLevel.getName() +
                            " " + JobManager.getPercentageBar(displayPercentage) + " " +
                            "§6 +" + moneyToGet + "€");

            pd.cacheChanges();
            pd.scheduleFlushing(20*20);
        } else {
            JobManager.sendActionBar(event.getPlayer(),
                    "§a" + worker.getJobLevel().getName() + " " +
                            worker.getExperience() + "§7/§a" + 500L * worker.getJobLevel().getDifficulty() + " " +
                            nextLevel.getName() +
                            " " + JobManager.getPercentageBar(displayPercentage));
        }

        worker.cacheChanges();
        worker.scheduleFlushing(20*20);

    }
}
