package fr.fonkio.command;

import fr.fonkio.music.PlayExecutor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public abstract class AbstractPlayCommand extends AbstractCommand{
    @Override
    protected boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        if (eventSlash == null) {
            return false;
        }

        InteractionHook hook = eventSlash.deferReply().complete();

        User user = eventSlash.getUser();
        Guild guild = eventSlash.getGuild();

        OptionMapping musiqueOption = eventSlash.getOption("musique");

        String musiqueParameterString = null;
        if(musiqueOption != null) {
            musiqueParameterString = musiqueOption.getAsString();
        }

        return PlayExecutor.runPlay(user, guild, hook, musiqueParameterString, skipBeforePlay());
    }

    protected abstract boolean skipBeforePlay();

    @Override
    public boolean isBlacklistable() {
        return true;
    }
}
