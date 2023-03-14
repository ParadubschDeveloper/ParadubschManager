package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.GuiItem;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.lifecycle.jobs.JobLevel;
import de.paradubsch.paradubschmanager.lifecycle.jobs.JobType;
import de.paradubsch.paradubschmanager.models.WorkerPlayer;
import de.craftery.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class FarmerJobButton extends GuiItem {
    @Override
    public void onClick(Player p) {
        WorkerPlayer workerPlayer = WorkerPlayer.getByIdOrCreate(p.getUniqueId().toString());
        workerPlayer.setExperience(0);
        workerPlayer.setJobLevel(JobLevel.ONE);
        workerPlayer.setJob(JobType.FARMER);
        ParadubschManager.getInstance().getJobManager()
                .getProgressPartPercentage().put(p.getUniqueId(), 0);
        workerPlayer.saveOrUpdate();
        String jobName = ParadubschManager.getInstance().getLanguageManager().getString(Message.Constant.FARMER, lang);
        MessageAdapter.sendMessage(p, Message.Info.JOB_CHANGED, jobName);
        p.closeInventory();
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.NETHERITE_HOE);
        this.setDisplayName(Message.Gui.JOB_FARMER_TITLE);
        this.addLore(Message.Gui.JOB_FARMER_LORE);
    }
}
