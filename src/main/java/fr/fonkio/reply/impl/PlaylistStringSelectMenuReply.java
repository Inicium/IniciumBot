package fr.fonkio.reply.impl;

import fr.fonkio.music.PlayExecutor;
import fr.fonkio.reply.AbstractStringSelectMenuReply;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class PlaylistStringSelectMenuReply extends AbstractStringSelectMenuReply {
    @Override
    protected boolean reply(IReplyCallback event, Guild guild, User user) {
        if (!(event instanceof StringSelectInteractionEvent)) {
            return false;
        }
        StringSelectInteractionEvent stringSelectEvent = (StringSelectInteractionEvent) event;
        InteractionHook hook = event.deferReply().complete();
        PlayExecutor.runPlay(event.getUser(), guild, hook, stringSelectEvent.getValues().get(0), false);
        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return true;
    }
}
