package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.GuiItem;
import de.paradubsch.paradubschmanager.lifecycle.jobs.JobLevel;
import de.paradubsch.paradubschmanager.lifecycle.jobs.JobType;
import de.paradubsch.paradubschmanager.models.WorkerPlayer;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MiningJobButton extends GuiItem {
    @Override
    public void onClick(Player p) {
        WorkerPlayer workerPlayer = WorkerPlayer.getByIdOrCreate(p.getUniqueId().toString());
        workerPlayer.setExperience(0);
        workerPlayer.setJobLevel(JobLevel.ONE);
        workerPlayer.setJob(JobType.MINER);
        workerPlayer.saveOrUpdate();
        MessageAdapter.sendMessage(p, Message.Info.JOB_CHANGED, "Miner");
        p.closeInventory();
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.IRON_PICKAXE);
        this.setDisplayName(Message.Gui.JOB_MINER_TITLE);
        this.addLore(Message.Gui.JOB_MINER_LORE);
    }
}
