package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.CancelButton;
import de.paradubsch.paradubschmanager.gui.items.RtpButton;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;

public class RtpGui extends BaseGui {
    @Override
    public void init(Language lang) {
        instantiate(lang, Message.Gui.RTP_TITLE, 3);
    }

    @Override
    public void build() {
        this.addAbstractItem(RtpButton.class, 2, 2, "Farmwelt", Material.GRASS_BLOCK);
        this.addAbstractItem(RtpButton.class, 2, 4, "world", Material.IRON_PICKAXE);
        this.addAbstractItem(RtpButton.class, 2, 6, "world_the_end", Material.END_STONE);
        this.addAbstractItem(RtpButton.class, 2, 8, "world_nether", Material.NETHERRACK);

        this.addItem(CancelButton.class, 3, 9);
    }
}
