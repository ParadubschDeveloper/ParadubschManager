package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.CancelButton;
import de.paradubsch.paradubschmanager.gui.items.GsClaimButton;
import de.craftery.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;

public class ClaimGui extends BaseGui {
    @Override
    public void init(Language lang) {
        instantiate(lang, Message.Gui.CLAIM_TITLE, 3);
    }

    @Override
    public void build() {
        this.addItem(GsClaimButton.class, 2, 5);

        this.addItem(CancelButton.class, 3, 9);
    }
}
