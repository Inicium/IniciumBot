package fr.fonkio;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.RichPresence;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IniciumActivity implements Activity {

    @Override
    public boolean isRich() {
        return false;
    }

    @Override
    public String getUrl() {
        return null;
    }

    @NotNull
    @Override
    public ActivityType getType() {
        return ActivityType.CUSTOM_STATUS;
    }

    @Override
    public Timestamps getTimestamps() {
        return new Timestamps(System.currentTimeMillis(), System.currentTimeMillis()+10000000L);
    }
    @Nullable
    @Override
    public EmojiUnion getEmoji() {
        return Emoji.fromFormatted("milky_way");
    }

    @NotNull
    @Override
    public Activity withState(@Nullable String state) {
        return new IniciumActivity();
    }

    @Override
    public @NotNull String getName() {
        return "/play [musique]";
    }

    @Nullable
    @Override
    public String getState() {
        return null;
    }

    @Override
    public RichPresence asRichPresence() {
        return null;
    }
}
