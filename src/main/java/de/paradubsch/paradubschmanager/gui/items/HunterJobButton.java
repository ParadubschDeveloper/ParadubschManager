package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.GuiItem;
import de.craftery.util.lang.Language;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.lifecycle.jobs.JobLevel;
import de.paradubsch.paradubschmanager.lifecycle.jobs.JobType;
import de.paradubsch.paradubschmanager.models.WorkerPlayer;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class HunterJobButton extends GuiItem {
    @Override
    public void onClick(Player p) {
        WorkerPlayer workerPlayer = WorkerPlayer.getByIdOrCreate(p.getUniqueId().toString());
        workerPlayer.setExperience(0);
        workerPlayer.setJob(JobType.HUNTER);
        workerPlayer.setJobLevel(JobLevel.ONE);
        workerPlayer.saveOrUpdate();
        Language lang = MessageAdapter.getSenderLang(p);
        String jobName = ParadubschManager.getInstance().getLanguageManager().getString(Message.Constant.HUNTER, lang);
        MessageAdapter.sendMessage(p, Message.Info.JOB_CHANGED, jobName);
        p.closeInventory();
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.STONE_SWORD);
        this.setDisplayName(Message.Gui.JOB_HUNTER_TITLE);
        this.addLore(Message.Gui.JOB_HUNTER_LORE);
    }
}