package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.*;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;

public class SaveConfirmGui extends BaseGui {
    @Override
    public void init(Language lang) {
        instantiate(lang, Message.Gui.SAVE_CONFIRM_TITLE, 3);
    }

    @Override
    public void build() {
        this.addItem(WorldInfoButton.class, 2, 2);
        this.addItem(SaverequestPlayerInfoButton.class, 2, 4);
        this.addItem(SaverequestRegionSizeInfoButton.class, 2, 6);
        this.addItem(SaveConfirmButton.class, 2, 9);

        this.addItem(CancelButton.class, 3, 9);
    }
}
