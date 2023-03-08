package de.craftery.util;

import de.craftery.InternalMessages;
import de.craftery.util.features.Feature;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.Float;
import java.lang.reflect.InvocationTargetException;

public class Expect {
    public static Boolean playerString(@Nullable String player) {
        if (player == null) return false;
        return player.matches("[a-zA-Z0-9_]{3,16}");
    }

    public static Boolean argLen(@NotNull Integer len, @Nullable String[] args) {
        if (args == null) {
            return len == 0;
        }
        return args.length == len;
    }

    public static Boolean minArgs(@NotNull Integer len, @Nullable String[] args) {
        if (args == null) {
            return len == 0;
        }
        return args.length >= len;
    }

    public static Boolean playerSender (@NotNull CommandSender sender) {
        if (!(sender instanceof Player)) {
            MessageAdapter.sendMessage(sender, InternalMessages.CMD_ONLY_FOR_PLAYERS);
        }

        return sender instanceof Player;
    }

    @SafeVarargs
    @Deprecated
    public static boolean featuresEnabled(@NotNull CommandSender sender, Class<? extends Feature>... features) {
        for (Class<? extends Feature> feature : features) {
            try {
                Feature instance = feature.getDeclaredConstructor().newInstance();
                if (!instance.isAvailable()) {
                    MessageAdapter.sendMessage(sender, InternalMessages.FEATURE_NOT_AVAILABLE, instance.getFeatureName());
                    return false;
                }
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                MessageAdapter.sendConsoleError(e);
            }
        }

        return true;
    }

    public static Boolean colorCode(String code) {
        return code.matches("^(&[0-9a-fk-or])+$");
    }

    public static @Nullable Long parseLong(String string) {
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static @Nullable Integer parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static @Nullable Float floatRange(String value, float min, float max) {
        try {
            float f = Float.parseFloat(value);
            if (f < min || f > max) {
                return null;
            }
            return f;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
