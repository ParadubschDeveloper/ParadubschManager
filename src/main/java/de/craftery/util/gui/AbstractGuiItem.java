package de.craftery.util.gui;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractGuiItem extends GuiItem {
    @Getter
    @Setter
    private Object identifier;
}
