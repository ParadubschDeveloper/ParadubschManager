package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.CancelButton;
import de.paradubsch.paradubschmanager.gui.items.GsDeleteConfirmButton;
import de.craftery.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;

public class GsDeleteGui extends BaseGui {
    @Override
    public void init(Language lang) {
        instantiate(lang, Message.Gui.GS_DELETE_TITLE, 3);
    }

    @Override
    public void build() {
        this.addItem(GsDeleteConfirmButton.class, 2, 5);
        this.addItem(CancelButton.class, 3, 9);
    }
}
