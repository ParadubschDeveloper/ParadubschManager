package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.craftinglib.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.*;
import de.craftery.craftinglib.messaging.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;

public class JobGui extends BaseGui {
    @Override
    public void init(Language lang) {
        instantiate(lang, Message.Gui.JOB_GUI_TITLE, 3);
    }

    @Override
    public void build() {
        this.addItem(MiningJobButton.class, 2, 2);
        this.addItem(HunterJobButton.class, 2, 4);
        this.addItem(LumberjackJobButton.class, 2, 5);
        this.addItem(FarmerJobButton.class, 2, 6);
        this.addItem(CollectorJobButton.class, 2, 8);
        this.addItem(CancelButton.class, 3, 9);
    }
}
