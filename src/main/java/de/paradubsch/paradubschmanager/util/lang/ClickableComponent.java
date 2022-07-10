package de.paradubsch.paradubschmanager.util.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class ClickableComponent {
    public static Component from(String string) {
        if (!string.matches("^@ClickableComponent<.*>$")) return Component.text(string);
        String argSec = string.split("(?<=<)|(?=>)")[1];

        String[] kvp = argSec.split("\\|");

        Map<String, String> args = new HashMap<>();
        for (String kv : kvp) {
            String[] keyValue = kv.split("=");
            args.put(keyValue[0], keyValue[1]);
        }

        if (!args.containsKey("Text")) return Component.text(string);

        Component component = Component.text(ChatColor.GRAY + args.get("Text"));

        if (args.containsKey("ClickCommand")){
            component = component.clickEvent(ClickEvent.runCommand(args.get("ClickCommand")));
        }

        if (args.containsKey("SuggestCommand")){
            component = component.clickEvent(ClickEvent.suggestCommand(args.get("SuggestCommand")));
        }

        if (args.containsKey("HoverText")){
            component = component.hoverEvent(HoverEvent.showText(Component.text(args.get("HoverText"))));
        }

        return component;
    }
}
