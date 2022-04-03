package fr.fonkio;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.RichPresence;
import org.jetbrains.annotations.NotNull;

public class IniciumActivity implements Activity {
    @Override
    public boolean isRich() {
        return true;
    }
    @Override
    public String getUrl() {
        return null;
    }
    @Override
    public @NotNull ActivityType getType() {
        return ActivityType.LISTENING;
    }
    @Override
    public Timestamps getTimestamps() {
        return new Timestamps(System.currentTimeMillis(), System.currentTimeMillis()+10000000L);
    }
    @Override
    public @NotNull String getName() {
        return "IniciumBot \uD83C\uDF0C                                                                                 \nCréé par Fonkio";
    }
    @Override
    public Emoji getEmoji() {
        return new Activity.Emoji("milky_way");
    }
    @Override
    public RichPresence asRichPresence() {
        return null;
    }
}
