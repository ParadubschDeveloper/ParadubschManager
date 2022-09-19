package de.paradubsch.paradubschmanager.util.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatComponentFactory {
    public static Component specialComponent(String string) {
        if (string.matches("^@ClickableComponent<.*>$")) return clickableComponent(string);
        if (string.matches("^@ObjectDump<.*>$")) return objectDump(string);
        return Component.text(string);
    }

    /**
     * Ported from my JavaScript implementation
     * <a href="https://github.com/Crafter-Y/bracketer/blob/main/script.js">Crafter-Y/bracketer</a>
     */
    private static Component objectDump (String string) {
        String input = string.split("(?<=<)|(?=>)")[1];
        String slString = input.replaceAll("[\n ]", "");
        StringBuilder outString = new StringBuilder();
        int indentCounter = 0;
        int indentRate = 2;
        boolean newline = true;
        boolean stringMode = false;
        for (int i = 0; i < slString.length(); i++) {
            String nextChar;
            try {
                nextChar = slString.charAt(i + 1) + "";
            } catch (StringIndexOutOfBoundsException e) {
                nextChar = "";
            }

            if (newline) {
                for (int j = 0; j < indentCounter * indentRate; j++) {
                    outString.append(" ");
                }
                newline = false;
            }
            outString.append(slString.charAt(i));
            if ((slString.charAt(i) + "").equals("\"")) {
                stringMode = !stringMode;
            }
            if (stringMode) continue;
            if ((slString.charAt(i) + "").equals("{")) {
                indentCounter++;
                newline = true;
                outString.append("\n");
            }
            if ((slString.charAt(i) + "").equals("[")) {
                if (!nextChar.equals("]")) {
                    indentCounter++;
                    newline = true;
                    outString.append("\n");
                }

            }
            if ((slString.charAt(i) + "").equals("}") || (slString.charAt(i) + "").equals("]")) {
                if (!nextChar.equals(",") && !nextChar.equals("=")) {
                    indentCounter--;
                    newline = true;
                    outString.append("\n");
                }

            }
            if ((slString.charAt(i) + "").equals(",")) {
                newline = true;
                outString.append("\n");
            }
            if (!(slString.charAt(i) + "").matches("[{}\\[\\]]") && nextChar.matches("[\\]}]")) {
                indentCounter--;
                newline = true;
                outString.append("\n");
            }

        }
        return Component.text(cleanNullsAndNotSet(outString.toString()));
    }

    private static String cleanNullsAndNotSet(String string) {
        StringBuilder out = new StringBuilder();
        for (String line : string.split("\n|\r|\r\n")) {
            if (line.matches("^.*not_set(,)?$")) continue;
            if (line.matches("^.*null(,)?$")) continue;
            if (line.matches("^.*undefined(,)?$")) continue;
            out.append(line);
            out.append("\n");
        }
        return out.toString();
    }


    private static Component clickableComponent (String string) {
        if (!string.matches("^@ClickableComponent<.*>$")) return Component.text(string);
        String argSec = string.split("(?<=<)|(?=>)")[1];

        String[] kvp = argSec.split("\\|");

        Map<String, String> args = new HashMap<>();
        for (String kv : kvp) {
            String[] keyValue = kv.split("=");
            args.put(keyValue[0], keyValue[1]);
        }

        if (!args.containsKey("Text")) return Component.text(string);
        TextComponent component = (TextComponent) fromLegacy(args.get("Text"));
        return applyArgs(component, args);
    }

    private static Component applyArgs(Component component, Map<String, String> args) {
        if (args.containsKey("ClickCommand")){
            component = component.clickEvent(ClickEvent.runCommand(args.get("ClickCommand")));
        }

        if (args.containsKey("OpenUrl")){
            component = component.clickEvent(ClickEvent.openUrl(args.get("OpenUrl")));
        }

        if (args.containsKey("SuggestCommand")){
            component = component.clickEvent(ClickEvent.suggestCommand(args.get("SuggestCommand")));
        }

        if (args.containsKey("HoverText")){
            Component hoverText = Component.text(ChatColor.translateAlternateColorCodes('&', args.get("HoverText")));
            component = component.hoverEvent(HoverEvent.showText(hoverText));
        }

        List<Component> children = new ArrayList<>(component.children());
        children.replaceAll(component1 -> applyArgs(component1, args));
        return component.children(children);
    }

    /**
     *
     * My thoughts on this:
     * isInitialized := false
     * startComponent := true || if we are in the first component
     *
     * isColor := different each iteration, not mutable
     *
     * esef &a &l &b eaeff &r ef &b &r -> esef | &a &l &b eaeff | &r ef | &b &r
     *  6    7  3  3   4   7  4  7   3
     *
     * &a &l esef &a &l &b ee -> &a &l esef | &a &l &b ee
     *  5  1  2    7  3  3 4
     *
     *
     * 1. isInitialized, startComponent, isColor : set color of start component to color
     * 2. isInitialized, startComponent, !isColor : set text of start component to text, isInitialized = false, startComponent = false
     *
     * 3. isInitialized, !startComponent, isColor : set color of appending component to color
     * 4. isInitialized, !startComponent, !isColor : set text of appending component and append it to component, isInitialized = false
     *
     * 5. !isInitialized, startComponent, isColor : initialize start component with color, isInitialized = true
     * 6. !isInitialized, startComponent, !isColor : initialize start component with text, set color to gray, isInitialized = false, startComponent = false
     *
     * 7. !isInitialized, !startComponent, isColor : initialize appending component with color, isInitialized = true
     * 8. !isInitialized, !startComponent, !isColor : // unreachable
     *
     */
    public static Component fromLegacy(String string) {
        String[] parts = string.split("(?<=&[0-9a-fk-or])|(?=&)");
        TextComponent component = Component.text("");
        TextComponent appendingComponent= Component.text("");;
        boolean isInitialized = false;
        boolean startComponent = true;

        for (String part : parts) {
            boolean isColor = part.matches("^&[0-9a-fk-or]$");

            if (isInitialized && startComponent && isColor) {
                component = colorizeComponent(component, part);
            } else if (isInitialized && startComponent) {
                component = component.content(part);
                isInitialized = false;
                startComponent = false;
            } else if (isInitialized && isColor) {
                appendingComponent = colorizeComponent(appendingComponent, part);
            } else if (isInitialized) {
                appendingComponent = appendingComponent.content(part);
                component = component.append(appendingComponent);
                appendingComponent = Component.text("");
                isInitialized = false;
            } else if (startComponent && isColor) {
                component = colorizeComponent(component, part);
                isInitialized = true;
            } else if (startComponent) {
                component = component.content(part);
                startComponent = false;
            } else if (isColor) {
                appendingComponent = colorizeComponent(appendingComponent, part);
                isInitialized = true;
            }
        }

        return component;
    }

    private static TextComponent colorizeComponent(TextComponent component, String charCode) {
        if (charCode.matches("^&[0-9a-f]$")) {
            return component.color(fromColorCode(charCode));
        } else if (charCode.matches("&k")) {
            return component.decorate(TextDecoration.OBFUSCATED);
        } else if (charCode.matches("&l")) {
            return component.decorate(TextDecoration.BOLD);
        } else if (charCode.matches("&m")) {
            return component.decorate(TextDecoration.STRIKETHROUGH);
        } else if (charCode.matches("&n")) {
            return component.decorate(TextDecoration.UNDERLINED);
        } else if (charCode.matches("&o")) {
            return component.decorate(TextDecoration.ITALIC);
        } else if (charCode.matches("&r")) {
            component = (TextComponent) component.decorations(new HashMap<>());
            component = component.color(fromColorCode("&7"));
        }

        return component;
    }

    public static TextColor fromColorCode(String colorCode) {
        switch (colorCode) {
            case "&0":
                return TextColor.color(0,0,0);
            case "&1":
                return TextColor.color(0,0,170);
            case "&2":
                return TextColor.color(0,170,0);
            case "&3":
                return TextColor.color(0,170,170);
            case "&4":
                return TextColor.color(170,0,0);
            case "&5":
                return TextColor.color(170,0,170);
            case "&6":
                return TextColor.color(255,170,0);
            case "&8":
                return TextColor.color(85,85,85);
            case "&9":
                return TextColor.color(85,85,255);
            case "&a":
                return TextColor.color(85,255,85);
            case "&b":
                return TextColor.color(85,255,255);
            case "&c":
                return TextColor.color(255,85,85);
            case "&d":
                return TextColor.color(255,85,255);
            case "&e":
                return TextColor.color(255,255,85);
            case "&f":
                return TextColor.color(255,255,255);
            default:
                return TextColor.color(170,170,170);
        }
    }

    public static Component assemble(String string) {
        Component component = Component.text("");
        component = component.color(fromColorCode("&7"));
        String[] parts = string.split("(?=@.+<.*>)|(?<=>)");
        for (String s : parts) {
            if (s.matches("^@.+<.*>$")) {
                component = component.append(specialComponent(s));
            } else {
                component = component.append(fromLegacy(s));
            }
        }
        return component;
    }
}