package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.BackButton;
import de.paradubsch.paradubschmanager.gui.items.BazaarClaimCollectableButton;
import de.paradubsch.paradubschmanager.gui.items.CancelButton;
import de.paradubsch.paradubschmanager.models.BazaarCollectable;
import de.craftery.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;

import java.util.List;

public class BazaarCollectGui extends BaseGui {
    @Override
    public void init(Language lang) {
        this.instantiate(lang, Message.Gui.BAZAAR_COLLECT_TITLE, 6);
    }

    @Override
    public void build() {
        List<BazaarCollectable> collectables = BazaarCollectable.getByHolder(this.player.getUniqueId().toString());
        for (int i = 0; i < collectables.size(); i++) {
            BazaarCollectable collectable = collectables.get(i);
            int slot = i + 1;
            if (slot > 45) continue;

            int row = (int) Math.ceil(slot / 9f);
            int col = slot % 9;
            System.out.println("Set collectable");
            this.getKvStore().set(collectable.getMaterial().toString() + "newestCollectable", collectable);
            System.out.println("Add item");
            this.addAbstractItem(BazaarClaimCollectableButton.class, row, col, collectable.getMaterial(), collectable.getMaterial());
        }

        this.addItem(BackButton.class, 6, 8);
        this.addItem(CancelButton.class, 6, 9);
    }
}
