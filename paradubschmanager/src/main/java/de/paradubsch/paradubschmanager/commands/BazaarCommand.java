package de.paradubsch.paradubschmanager.commands;

import de.craftery.util.features.HeadDatabaseFeature;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.gui.items.BazaarPlaceSellOrderButton;
import de.paradubsch.paradubschmanager.gui.window.BazaarMainGui;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.Bazaar;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.BazaarItemData;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.craftery.util.Expect;
import de.craftery.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BazaarCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;
        Player player = (Player) sender;

        if (args.length == 0) {
            if (!Expect.featuresEnabled(sender, HeadDatabaseFeature.class)) return true;
            GuiManager.entryGui(BazaarMainGui.class, player);
        } else if(args[0].equalsIgnoreCase("sell")) {
            if (!Expect.minArgs(2, args)) {
                MessageAdapter.sendMessage(player, Message.Error.CMD_BAZAAR_PRICE_NOT_PROVIDED);
                return true;
            }

            Integer price = Expect.parseInt(args[1]);
            if (price == null) {
                MessageAdapter.sendMessage(player, Message.Error.CMD_BAZAAR_PRICE_NOT_PROVIDED);
                return true;
            }
            sellItem(player, price);
        }

        return true;
    }

    private static void sellItem(Player player, Integer price) {
        ItemStack item = player.getInventory().getItemInMainHand();

        Optional<BazaarItemData> bazaarItemDataOptional = Bazaar.getBazaarConfigItems().stream().filter(x -> x.getMaterial() == item.getType()).findFirst();
        if (bazaarItemDataOptional.isEmpty()) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_BAZAAR_CANNOT_SELL_THIS_ITEM);
            return;
        }
        
        BazaarItemData data = bazaarItemDataOptional.get();

        if (item.getAmount() < data.getAmount()) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_BAZAAR_NOT_ENOUGH_ITEMS, data.getAmount() + "", item.getType().name());
            return;
        }

        if (price > data.getOffer() || price <= data.getBuy()) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_BAZAAR_INVALID_PRICE,  data.getBuy() + "", data.getOffer() + "");
            return;
        }

        int taxes = (int) Math.floor(price * 0.1f);

        PlayerData pd = PlayerData.getByPlayer(player);
        if (pd.getMoney() < taxes) {
            MessageAdapter.sendMessage(player, Message.Error.NOT_ENOUGH_MONEY);
            return;
        }
        pd.setMoney(pd.getMoney() - taxes);
        pd.saveOrUpdate();

        if (item.getAmount() == data.getAmount()) {
            player.getInventory().setItemInMainHand(null);
        } else {
            item.setAmount(item.getAmount() - data.getAmount());
            player.getInventory().setItemInMainHand(item);
        }

        BazaarPlaceSellOrderButton.sellItem(player, pd, data, price);
        MessageAdapter.sendMessage(player, Message.Info.CMD_BAZAAR_PAYED_TAXES, taxes + "");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.add("sell");
            return l;
        }
        return l;
    }
}
