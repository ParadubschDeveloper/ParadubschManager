package de.craftery.craftinglib.messaging.parser;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.intellij.lang.annotations.Language;

import java.util.*;

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

        if (!args.containsKey("Text") && !args.containsKey("Translatable")) return Component.text(string);

        Component component = base("");
        if (args.containsKey("Text")) {
            component = fromLegacy(args.get("Text"));
        }
        if (args.containsKey("Translatable")) {
            Material mat = Material.getMaterial(args.get("Translatable"));
            if (mat == null) {
                component = base("");
            } else {
                component = Component.translatable(new ItemStack(mat).translationKey());
            }
        }

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

    public static Component fromLegacy(String string) {
        List<String> parts = Arrays.stream(string.split("(?<=&[0-9a-fk-or])|(?=&[0-9a-fk-or])")).toList();
        // match before and after '&' escape code, e.g.: '&a'
        List<String> colorCorrectedParts = new ArrayList<>();

        boolean hasColorBefore = false;
        for (String part : parts) {
            if (part.matches("^&[0-9a-fk-or]$")) {
                if (hasColorBefore) {
                    colorCorrectedParts.set(colorCorrectedParts.size() - 1, colorCorrectedParts.get(colorCorrectedParts.size() - 1) + part);
                } else {
                    colorCorrectedParts.add(part);
                    hasColorBefore = true;
                }
            } else {
                colorCorrectedParts.add(part);
                hasColorBefore = false;
            }
        }
        // Now multiple color codes are combined into one string, e.g.: ["&a", "&b"] -> ["&a&b"]

        TextComponent component = null;
        TextComponent appendingComponent = null;

        if (colorCorrectedParts.size() == 0) return Component.text(string);

        for (String part : colorCorrectedParts) {
            boolean isColor = part.matches("^(&[0-9a-fk-or])+$");
            if (isColor) {
                if (appendingComponent != null) {
                    if (component == null) {
                        component = Objects.requireNonNullElseGet(appendingComponent, () -> base(""));
                    } else {
                        component = component.append(appendingComponent);
                    }
                }

                appendingComponent = base("");
                appendingComponent = colorizeComponent(appendingComponent, part);
            } else {
                if (appendingComponent == null) {
                    appendingComponent = base(part).color(NamedTextColor.GRAY);
                } else {
                    appendingComponent = appendingComponent.content(part);
                }
            }
        }
        if (component == null) {
            return appendingComponent;
        } else {
            component = component.append(appendingComponent);
        }
        return component;
    }

    public static TextComponent base(String text) {
        TextComponent comp = Component.text(text);

        comp = comp.decoration(TextDecoration.OBFUSCATED, false);
        comp = comp.decoration(TextDecoration.BOLD, false);
        comp = comp.decoration(TextDecoration.STRIKETHROUGH, false);
        comp = comp.decoration(TextDecoration.UNDERLINED, false);
        comp = comp.decoration(TextDecoration.ITALIC, false);
        return comp;
    }

    private static TextComponent colorizeComponent(TextComponent component, String colorCodes) {
        String[] colors = colorCodes.split("(?<=&[0-9a-fk-or])|(?=&[0-9a-fk-or])");
        boolean isObfuscated = false;
        boolean isBold = false;
        boolean isStrikethrough = false;
        boolean isUnderlined = false;
        boolean isItalic = false;
        for (String color : colors) {
            if (color.matches("^&[0-9a-f]$")) {
                component = component.color(fromColorCode(color));
            } else if (color.matches("&k")) {
                component = component.decorate(TextDecoration.OBFUSCATED);
                isObfuscated = true;
            } else if (color.matches("&l")) {
                component = component.decorate(TextDecoration.BOLD);
                isBold = true;
            } else if (color.matches("&m")) {
                component = component.decorate(TextDecoration.STRIKETHROUGH);
                isStrikethrough = true;
            } else if (color.matches("&n")) {
                component = component.decorate(TextDecoration.UNDERLINED);
                isUnderlined = true;
            } else if (color.matches("&o")) {
                component = component.decorate(TextDecoration.ITALIC);
                isItalic = true;
            } else if (color.matches("&r")) {
                component = (TextComponent) component.decorations(new HashMap<>());
                component = component.color(NamedTextColor.GRAY);
            }
        }

        if (!isObfuscated) {
            component = component.decoration(TextDecoration.OBFUSCATED, false);
        }
        if (!isBold) {
            component = component.decoration(TextDecoration.BOLD, false);
        }
        if (!isStrikethrough) {
            component = component.decoration(TextDecoration.STRIKETHROUGH, false);
        }
        if (!isUnderlined) {
            component = component.decoration(TextDecoration.UNDERLINED, false);
        }
        if (!isItalic) {
            component = component.decoration(TextDecoration.ITALIC, false);
        }

        return component;
    }

    public static TextColor fromColorCode(String colorCode) {
        return switch (colorCode) {
            case "&0" -> TextColor.color(0, 0, 0);
            case "&1" -> TextColor.color(0, 0, 170);
            case "&2" -> TextColor.color(0, 170, 0);
            case "&3" -> TextColor.color(0, 170, 170);
            case "&4" -> TextColor.color(170, 0, 0);
            case "&5" -> TextColor.color(170, 0, 170);
            case "&6" -> TextColor.color(255, 170, 0);
            case "&8" -> TextColor.color(85, 85, 85);
            case "&9" -> TextColor.color(85, 85, 255);
            case "&a" -> TextColor.color(85, 255, 85);
            case "&b" -> TextColor.color(85, 255, 255);
            case "&c" -> TextColor.color(255, 85, 85);
            case "&d" -> TextColor.color(255, 85, 255);
            case "&e" -> TextColor.color(255, 255, 85);
            case "&f" -> TextColor.color(255, 255, 255);
            default -> TextColor.color(170, 170, 170);
        };
    }

    private static List<Token> lexMessage(String message) {
        Fragment frag = new Fragment(message);

        List<Token> tokens = new ArrayList<>();

        @Language("RegExp") String componentRegex = "^@[a-zA-Z]+<[^><]*>";
        while (!frag.isEmpty()) {
            if (frag.testRegex(componentRegex)) {
                tokens.add(new Token(TokenType.COMPONENT, frag.consumeRegex()));
            } else if (frag.testIsRangeWithoutRegex(componentRegex)) {
                tokens.add(new Token(TokenType.TEXT, frag.consume()));
            } else {
                tokens.add(new Token(TokenType.TEXT, frag.getContents()));
                break;
            }
        }
        return tokens;
    }

    public static Component assemble(String string) {
        List<Token> tokens = lexMessage(string);

        System.out.println(tokens);

        Component component = null;

        for (Token token : tokens) {
            switch (token.getType()) {
                case TEXT -> {
                    if (component == null) {
                        component = fromLegacy(token.getContent());
                    } else {
                        component = component.append(fromLegacy(token.getContent()));
                    }
                }
                case COMPONENT -> {
                    if (component == null) {
                        component = specialComponent(token.getContent());
                    } else {
                        component = component.append(specialComponent(token.getContent()));
                    }
                }
            }
        }

        return component;
    }
}