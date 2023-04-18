package de.craftery.craftinglib.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import de.craftery.craftinglib.InternalMessages;
import de.craftery.craftinglib.util.features.Feature;
import de.craftery.craftinglib.util.Expect;
import de.craftery.craftinglib.util.MessageAdapter;
import lombok.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Data
public abstract class CraftingCommand implements CommandExecutor, TabCompleter {
    private final String name;
    private final List<String> otherIdentifiers = new ArrayList<>();
    private String description = "";
    private String permission = "";
    private String identifier = "";
    private String activeIdentifier = "";
    private String activeAlias = "";

    protected CraftingCommand(String name) {
        this.name = name;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
       CraftingPlayer player = new CraftingPlayer(sender);

        this.setActiveIdentifier(command.getLabel());
        this.setActiveAlias(label);

        if (!isRightPlayerType(player)) return true;
        if (!isFeatureDependent(player)) return true;
        if (!checkArgs(player, args)) return true;

        return execute(player, args);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        this.setActiveIdentifier(command.getLabel());
        this.setActiveAlias(label);

        return tabComplete(new CraftingPlayer(sender), args);
    }

    private boolean checkArgs(CraftingPlayer player, String[] args) {
        try {
            Annotation[] annotations = this.getClass().getMethod("execute", CraftingPlayer.class, String[].class).getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof CheckArg) {
                    CheckArg argsAnnotation = (CheckArg) annotation;

                    if (args.length < argsAnnotation.index() + 1) {
                        reportInsufficientArgumentCount(player, argsAnnotation.type(), argsAnnotation.index());
                        return false;
                    }

                    if (argsAnnotation.checkType()) {
                        if (!checkArgType(player, argsAnnotation.type(), args[argsAnnotation.index()])) return false;
                    }
                }
            }
        } catch (NoSuchMethodException ignored) {
            return true;
        }
        return true;
    }

    private boolean checkArgType(CraftingPlayer player, ArgType type, String arg) {
        switch (type) {
            case NOT_PROVIDED: {
                return true;
            }
            case PLAYER_NAME: {
                if (!Expect.playerString(arg)) {
                    player.sendMessage(InternalMessages.PLAYER_NOT_PROVIDED);
                    return false;
                }
                break;
            }
        }
        return true;
    }

    private void reportInsufficientArgumentCount(CraftingPlayer player, ArgType type, int index) {
        switch (type) {
            case NOT_PROVIDED: {
                player.sendMessage(InternalMessages.NOT_ENOUGH_ARGUMENTS, (index + 1) + "");
                break;
            }
            case PLAYER_NAME: {
                player.sendMessage(InternalMessages.PLAYER_NOT_PROVIDED);
                break;
            }
        }
    }

    private boolean isFeatureDependent(CraftingPlayer player) {
        FeatureDependent featureDependent = hasFeatureAnnotation();
        if (featureDependent == null) return true;

        for (Class<? extends Feature> feature : featureDependent.features()) {
            try {
                Feature instance = feature.getDeclaredConstructor().newInstance();
                if (!instance.isAvailable()) {
                    player.sendMessage(InternalMessages.FEATURE_NOT_AVAILABLE, instance.getFeatureName());
                    return false;
                }
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                MessageAdapter.sendConsoleError(e);
            }
        }
        return true;
    }

    private @Nullable FeatureDependent hasFeatureAnnotation() {
        try {
            return this.getClass().getMethod("execute", CraftingPlayer.class, String[].class).getAnnotation(FeatureDependent.class);
        } catch (NoSuchMethodException ignored) {
            return null;
        }
    }

    private boolean isRightPlayerType(CraftingPlayer player) {
        if (isPlayerOnlyPresent() && !player.isPlayer()) {
            player.sendMessage(InternalMessages.CMD_ONLY_FOR_PLAYERS);
            return false;
        } else if (isConsoleOnlyPresent() && player.isPlayer()) {
            player.sendMessage(InternalMessages.CMD_ONLY_FOR_CONSOLE);
            return false;
        }
        return true;
    }

    private boolean isPlayerOnlyPresent() {
        try {
            return this.getClass().getMethod("execute", CraftingPlayer.class, String[].class).isAnnotationPresent(PlayerOnly.class);
        } catch (NoSuchMethodException ignored) {
            return false;
        }
    }

    private boolean isConsoleOnlyPresent() {
        try {
            return this.getClass().getMethod("execute", CraftingPlayer.class, String[].class).isAnnotationPresent(ConsoleOnly.class);
        } catch (NoSuchMethodException ignored) {
            return false;
        }
    }

    public @Nullable LiteralCommandNode<?> registerCommandHelper() {
        return null;
    }

    public abstract boolean execute(CraftingPlayer player, String[] args);
    public abstract List<String> tabComplete(CraftingPlayer player, String[] args);
}
