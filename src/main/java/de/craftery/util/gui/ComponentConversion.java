package de.craftery.util.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class ComponentConversion {
    public static String fromComponent(Component component) {
        System.out.println(component);
        StringBuilder returner = new StringBuilder();

        if (!(component instanceof TextComponent)) {
            return "";
        }
        TextComponent textComponent = (TextComponent) component;

        returner.append(textComponent.content());
        textComponent.children().forEach(child -> returner.append(fromComponent(child)));
        return returner.toString();
    }
}
