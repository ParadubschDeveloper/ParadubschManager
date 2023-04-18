package de.paradubsch.easterevent;

import de.craftery.craftinglib.CraftingLib;
import de.craftery.craftinglib.CraftingPlugin;
import de.craftery.craftinglib.PlayerData;
import de.craftery.craftinglib.util.MessageAdapter;
import de.craftery.craftinglib.util.collectables.Collectable;
import de.craftery.craftinglib.util.collectables.CollectableCollectedEvent;
import de.craftery.craftinglib.util.collectables.CollectedCollectable;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class EasterEvent extends CraftingPlugin implements Listener {
    public static final String EASTER_EVENT_NAME = "easter_event_2023";

    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Enabling EasterEvent plugin...");
        this.getLanguageManager().registerMessageEnum(EasterEventMessage.class);
        CraftingLib.registerCollectableType(EASTER_EVENT_NAME);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onCollectableCollected(CollectableCollectedEvent e) {
        if (e.getCollectable().getType().equals(EASTER_EVENT_NAME)) {
            List<Collectable> allEggs = Collectable.getByType(EASTER_EVENT_NAME);
            int totalAmount = allEggs.size();
            int alreadyCollectedAmount = allEggs.stream()
                    .filter(collectable -> CollectedCollectable.hasCollected(e.getPlayer(), collectable))
                    .toList().size();

            MessageAdapter.sendMessage(e.getPlayer(), EasterEventMessage.COLLECTED_EASTER_EGG,
                    String.valueOf(alreadyCollectedAmount + 1),
                    String.valueOf(totalAmount)
            );

            PlayerData pd = PlayerData.getByPlayer(e.getPlayer());
            pd.setMoney(pd.getMoney() + 25);

            MessageAdapter.sendMessage(e.getPlayer(), EasterEventMessage.REGULAR_GOT_MONEY);

            if (alreadyCollectedAmount + 1 == totalAmount) {
                pd.setMoney(pd.getMoney() + 100);
                MessageAdapter.sendMessage(e.getPlayer(), EasterEventMessage.LAST_EASTER_EGG);
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1.5f, 1f);
            } else {
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 10f, 1f);
            }

            pd.saveOrUpdate();

            e.handle();
        }
    }
}
