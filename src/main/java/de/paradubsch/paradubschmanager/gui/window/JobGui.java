package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.CancelButton;
import de.paradubsch.paradubschmanager.gui.items.MiningJobButton;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;

public class JobGui extends BaseGui {
    @Override
    public void init(Language lang) {
        instantiate(lang, Message.Gui.JOB_GUI_TITLE, 3);
    }

    @Override
    public void build() {
        this.addItem(MiningJobButton.class, 2, 5);
        this.addItem(CancelButton.class, 3, 9);
    }
}
