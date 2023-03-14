package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.GuiItem;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.lifecycle.jobs.JobLevel;
import de.paradubsch.paradubschmanager.lifecycle.jobs.JobType;
import de.paradubsch.paradubschmanager.models.WorkerPlayer;
import de.craftery.util.MessageAdapter;
import de.craftery.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CollectorJobButton extends GuiItem {
    @Override
    public void onClick(Player p) {
        WorkerPlayer workerPlayer = WorkerPlayer.getByIdOrCreate(p.getUniqueId().toString());
        workerPlayer.setExperience(0);
        workerPlayer.setJob(JobType.COLLECTOR);
        workerPlayer.setJobLevel(JobLevel.ONE);
        workerPlayer.saveOrUpdate();
        Language lang = MessageAdapter.getSenderLang(p);
        String jobName = ParadubschManager.getInstance().getLanguageManager().getString(Message.Constant.COLLECTOR, lang);
        MessageAdapter.sendMessage(p, Message.Info.JOB_CHANGED, jobName);
        p.closeInventory();
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.BUCKET);
        this.setDisplayName(Message.Gui.JOB_COLLECTOR_TITLE);
        this.addLore(Message.Gui.JOB_COLLECTOR_LORE);
    }
}