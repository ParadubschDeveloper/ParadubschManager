package de.craftery.craftinglib;

import de.craftery.craftinglib.messaging.parser.ChatComponentFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChatComponentTest {

    @Test
    @DisplayName("Test if the String to Component works")
    public void verify() {
        Component test1 = ChatComponentFactory.base("Hello World").color(NamedTextColor.GRAY);
        Component result1 = ChatComponentFactory.assemble("Hello World");

        assertEquals(test1, result1);

        Component test2 = ChatComponentFactory.base("[").color(NamedTextColor.DARK_GRAY).decorate(TextDecoration.BOLD)
                .append(ChatComponentFactory.base("Server").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD))
                .append(ChatComponentFactory.base("] ").color(NamedTextColor.DARK_GRAY).decorate(TextDecoration.BOLD))
                .append(ChatComponentFactory.base("").color(NamedTextColor.GRAY));
        Component result2 = ChatComponentFactory.assemble("&8&l[&6&lServer&8&l] &r");

        assertEquals(test2, result2);
    }
}
