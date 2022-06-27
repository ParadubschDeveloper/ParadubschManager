package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.CancelButton;
import de.paradubsch.paradubschmanager.gui.items.SaveConfirmButton;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;

public class SaveConfirmGui extends BaseGui {
    @Override
    public void init(Language lang) {
        instantiate(lang, Message.Gui.SAVE_CONFIRM_TITLE, 3);
    }

    @Override
    public void build() {
        this.addItem(SaveConfirmButton.class, 2, 9);

        this.addItem(CancelButton.class, 3, 9);
    }
}
