package de.paradubsch.paradubschmanager.commands;

import de.craftery.command.CraftCommand;
import de.craftery.command.CraftPlayer;
import de.craftery.command.FeatureDependent;
import de.craftery.command.PlayerOnly;
import de.craftery.util.features.HeadDatabaseFeature;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.gui.window.BackpackGui;

import java.util.ArrayList;
import java.util.List;

public class BackpackCommand extends CraftCommand {
    public BackpackCommand() {
        super("Backpack Command");
        this.setIdentifier("backpack");
    }

    @Override
    @PlayerOnly
    @FeatureDependent(features = {HeadDatabaseFeature.class})
    public boolean execute(CraftPlayer player, String[] args) {
        GuiManager.entryGui(BackpackGui.class, player);
        return true;
    }

    @Override
    public List<String> tabComplete(CraftPlayer player, String[] args) {
        return new ArrayList<>();
    }
}
