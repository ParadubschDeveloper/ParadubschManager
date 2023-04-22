package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.craftinglib.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.CancelButton;
import de.paradubsch.paradubschmanager.gui.items.GsTransferConfirmButton;
import de.craftery.craftinglib.messaging.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;

public class GsTransferGui extends BaseGui {
    @Override
    public void init(Language lang) {
        instantiate(lang, Message.Gui.GS_TRANSFER_TITLE, 3);
    }

    @Override
    public void build() {
        this.addItem(GsTransferConfirmButton.class, 2, 5);
        this.addItem(CancelButton.class, 3, 9);
    }
}
