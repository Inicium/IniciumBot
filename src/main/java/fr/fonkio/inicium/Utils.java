package fr.fonkio.inicium;

import net.dv8tion.jda.api.entities.Guild;

import java.util.concurrent.TimeUnit;

public class Utils {

    public static String convertLongToString(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public static String getFormattedLogString(Guild guild, String message) {
        if (guild == null) {
            return message;
        }
        String log = "[" + guild.getName();
        if (guild.getOwner() != null) {
            log += " de " + guild.getOwner().getUser().getName();
        }
        log += "] " + message;
        return log;
    }

}
