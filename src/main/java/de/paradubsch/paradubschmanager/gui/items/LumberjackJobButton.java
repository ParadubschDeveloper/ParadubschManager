package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.GuiItem;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.lifecycle.jobs.JobLevel;
import de.paradubsch.paradubschmanager.lifecycle.jobs.JobType;
import de.paradubsch.paradubschmanager.models.WorkerPlayer;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class LumberjackJobButton extends GuiItem {
    @Override
    public void onClick(Player p) {
        WorkerPlayer workerPlayer = WorkerPlayer.getByIdOrCreate(p.getUniqueId().toString());
        workerPlayer.setExperience(0);
        workerPlayer.setJob(JobType.LUMBERJACK);
        workerPlayer.setJobLevel(JobLevel.ONE);
        ParadubschManager.getInstance().getJobManager()
                .getProgressPartPercentage().put(p.getUniqueId(), 0);
        workerPlayer.saveOrUpdate();
        Language lang = MessageAdapter.getSenderLang(p);
        String jobName = ParadubschManager.getInstance().getLanguageManager().getString(Message.Constant.LUMBERJACK, lang);
        MessageAdapter.sendMessage(p, Message.Info.JOB_CHANGED, jobName);
        p.closeInventory();
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.DIAMOND_AXE);
        this.setDisplayName(Message.Gui.JOB_LUMBERJACK_TITLE);
        this.addLore(Message.Gui.JOB_LUMBERJACK_LORE);
    }
}
