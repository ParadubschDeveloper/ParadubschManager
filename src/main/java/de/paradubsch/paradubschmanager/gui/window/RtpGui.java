package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.CancelButton;
import de.paradubsch.paradubschmanager.gui.items.PlaceholderButton;
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
        this.addItem(PlaceholderButton.class, 1, 1, Material.MAGENTA_STAINED_GLASS_PANE);
        this.addItem(PlaceholderButton.class, 1, 2, Material.MAGENTA_STAINED_GLASS_PANE);
        this.addItem(PlaceholderButton.class, 1, 8, Material.MAGENTA_STAINED_GLASS_PANE);
        this.addItem(PlaceholderButton.class, 1, 9, Material.MAGENTA_STAINED_GLASS_PANE);
        this.addItem(PlaceholderButton.class, 2, 1, Material.MAGENTA_STAINED_GLASS_PANE);
        this.addItem(PlaceholderButton.class, 2, 9, Material.MAGENTA_STAINED_GLASS_PANE);
        this.addItem(PlaceholderButton.class, 3, 1, Material.MAGENTA_STAINED_GLASS_PANE);
        this.addItem(PlaceholderButton.class, 3, 2, Material.MAGENTA_STAINED_GLASS_PANE);
        this.addItem(PlaceholderButton.class, 3, 8, Material.MAGENTA_STAINED_GLASS_PANE);

        this.addItem(PlaceholderButton.class, 1, 5, Material.WHITE_STAINED_GLASS_PANE);
        this.addItem(PlaceholderButton.class, 3, 5, Material.WHITE_STAINED_GLASS_PANE);

        this.addAbstractItem(RtpButton.class, 2, 2, "Farmwelt", "32442", "Farmwelt", "Farmwelt");
        this.addAbstractItem(RtpButton.class, 2, 4, "world", "40774", "Bauwelt", "world");
        this.addAbstractItem(RtpButton.class, 2, 6, "world_the_end", "34506", "End", "world_the_end");
        this.addAbstractItem(RtpButton.class, 2, 8, "world_nether", "3030", "Nether", "world_nether");

        this.addItem(CancelButton.class, 3, 9);
    }
}
