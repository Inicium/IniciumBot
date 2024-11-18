package fr.fonkio.reply;

import fr.fonkio.music.PlayExecutor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public abstract class AbstractPlayReply extends AbstractReply {
    @Override
    protected boolean reply(IReplyCallback event, Guild guild, User user) {
        if (!(event instanceof SlashCommandInteractionEvent)) {
            return false;
        }
        SlashCommandInteractionEvent slashCommandEvent = (SlashCommandInteractionEvent) event;
        InteractionHook hook = slashCommandEvent.deferReply().complete();

        OptionMapping musiqueOption = slashCommandEvent.getOption("musique");

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
