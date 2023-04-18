package de.paradubsch.paradubschmanager.util;

import de.craftery.craftinglib.messaging.lang.Language;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.craftery.craftinglib.messaging.lang.LanguageManager;
import de.paradubsch.paradubschmanager.util.lang.Message;

import java.sql.Timestamp;
import java.time.Instant;

public class TimeCalculations {
    public static Timestamp parseExpiration(String exp) {
        if (exp.matches("^[Pp]erma(nent)?$")) {
            return Timestamp.from(Instant.ofEpochMilli(System.currentTimeMillis() + 946728000000L));
        } else if (exp.matches("^\\d+(.|,)?\\d*[ydhm]$")) {
            String[] split = exp.split("(?=[ydhm])");
            float amount = Float.parseFloat(split[0]);
            long msTimestamp;
            switch (split[1]) {
                case "y":
                    msTimestamp = (long) (amount * 365 * 24 * 60 * 60 * 1000L) + System.currentTimeMillis();
                    break;
                case "d":
                    msTimestamp = (long) (amount * 24 * 60 * 60 * 1000L) + System.currentTimeMillis();
                    break;
                case "h":
                    msTimestamp = (long) (amount * 60 * 60 * 1000L) + System.currentTimeMillis();
                    break;
                case "m":
                    msTimestamp = (long) (amount * 60 * 1000L) + System.currentTimeMillis();
                    break;
                default:
                    return null;
            }
            return Timestamp.from(Instant.ofEpochMilli(msTimestamp));
        } else {
            return null;
        }
    }

    public static String timeMsToExpiration(long ms, Language lang) {
        LanguageManager lm = ParadubschManager.getInstance().getLanguageManager();
        if (ms > 915170400000L) {
            return lm.getString(Message.Constant.PERMANENT, lang);
        } else {
            StringBuilder returner = new StringBuilder();
            long days = ms / (24 * 60 * 60 * 1000L);
            long hours = (ms % (24 * 60 * 60 * 1000L)) / (60 * 60 * 1000L);
            long minutes = (ms % (60 * 60 * 1000L)) / (60 * 1000L);
            long seconds = (ms % (60 * 1000L)) / 1000L;

            if (days != 0L) {
                if (days == 1L) {
                    returner.append("1 ").append(lm.getString(Message.Constant.DAY_SINGULAR, lang));
                } else {
                    returner.append(days).append(" ").append(lm.getString(Message.Constant.DAY_PLURAL, lang));
                }
            }

            if (hours != 0L) {
                if (days != 0L) returner.append(", ");
                if (hours == 1L) {
                    returner.append("1 ").append(lm.getString(Message.Constant.HOUR_SINGULAR, lang));
                } else {
                    returner.append(hours).append(" ").append(lm.getString(Message.Constant.HOUR_PLURAL, lang));
                }
            }

            if (minutes != 0L) {
                if (hours != 0L) returner.append(", ");
                if (minutes == 1L) {
                    returner.append("1 ").append(lm.getString(Message.Constant.MINUTE_SINGULAR, lang));
                } else {
                    returner.append(minutes).append(" ").append(lm.getString(Message.Constant.MINUTE_PLURAL, lang));
                }
            }

            if (seconds != 0L) {
                if (minutes != 0L) returner.append(", ");
                if (seconds == 1L) {
                    returner.append("1 ").append(lm.getString(Message.Constant.SECOND_SINGULAR, lang));
                } else {
                    returner.append(seconds).append(" ").append(lm.getString(Message.Constant.SECOND_PLURAL, lang));
                }
            }

            return returner.toString();
        }
    }

    public static String timeStampToExpiration(Timestamp timestamp, Language lang) {
        long ms = timestamp.getTime() - System.currentTimeMillis() + 500L;
        return timeMsToExpiration(ms, lang);
    }
}
