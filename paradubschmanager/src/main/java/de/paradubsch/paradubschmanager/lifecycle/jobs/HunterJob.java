package de.paradubsch.paradubschmanager.lifecycle.jobs;

import de.craftery.PlayerData;
import de.paradubsch.paradubschmanager.models.WorkerPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public class HunterJob {
    public static void onKill(WorkerPlayer worker, Entity killedEntity, Player player) {
        int earn = 0;
        boolean progress = false;
        switch (killedEntity.getType()) {
            case CAVE_SPIDER:
            case CHICKEN:
            case COW:
            case CREEPER:
            case DROWNED:
            case ENDERMITE:
            case GUARDIAN:
            case HORSE:
            case HUSK:
            case LLAMA:
            case MAGMA_CUBE:
            case MULE:
            case PIG:
            case PIGLIN:
            case ZOMBIFIED_PIGLIN:
            case PIGLIN_BRUTE:
            case SHEEP:
            case SHULKER:
            case SILVERFISH:
            case SKELETON:
            case SLIME:
            case SPIDER:
            case SQUID:
            case STRAY:
            case VEX:
            case VILLAGER:
            case VINDICATOR:
            case WITHER_SKELETON:
            case WOLF:
            case ZOGLIN:
            case ZOMBIE:
            case ZOMBIE_VILLAGER: {
                worker.setExperience(worker.getExperience() + 1);
                earn = 1;
                progress = true;
                break;
            }
            case BAT:
            case BEE:
            case BLAZE:
            case COD:
            case DONKEY:
            case EVOKER:
            case GHAST:
            case GLOW_SQUID:
            case GOAT:
            case HOGLIN:
            case MUSHROOM_COW:
            case PARROT:
            case PHANTOM:
            case PILLAGER:
            case POLAR_BEAR:
            case PUFFERFISH:
            case RABBIT:
            case SALMON:
            case STRIDER:
            case TRADER_LLAMA:
            case TROPICAL_FISH:
            case WANDERING_TRADER:
            case WITCH: {
                worker.setExperience(worker.getExperience() + 2);
                earn = 2;
                progress = true;
                break;
            }
            case CAT:
            case DOLPHIN:
            case FOX:
            case OCELOT:
            case PANDA:
            case RAVAGER:
            case SKELETON_HORSE:
            case TURTLE:
            case ZOMBIE_HORSE:
            case SNOWMAN: {
                worker.setExperience(worker.getExperience() + 5);
                earn = 5;
                progress = true;
                break;
            }
            case AXOLOTL:
            case ELDER_GUARDIAN:
            case IRON_GOLEM: {
                worker.setExperience(worker.getExperience() + 10);
                earn = 10;
                progress = true;
                break;
            }
            case WITHER: {
                worker.setExperience(worker.getExperience() + 20);
                earn = 20;
                progress = true;
                break;
            }
            case ENDER_DRAGON: {
                worker.setExperience(worker.getExperience() + 50);
                earn = 30;
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

        JobManager.sendActionBar(player, "§a" + worker.getJobLevel().getName() + " " +
                worker.getExperience() + "§7/§a" +
                250L * worker.getJobLevel().getDifficulty() + " " + nextLevel.getName() + ChatColor.GOLD + " +" + realEarn + "€");
        worker.cacheChanges();
        worker.scheduleFlushing(20*20);
        pd.cacheChanges();
        pd.scheduleFlushing(20*20);
    }
}
