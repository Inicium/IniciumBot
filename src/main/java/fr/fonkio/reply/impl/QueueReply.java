package fr.fonkio.reply.impl;

import fr.fonkio.reply.AbstractReply;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class QueueReply extends AbstractReply {

    @Override
    public boolean reply(IReplyCallback event, Guild guild, User user) {
        InteractionHook hook = event.deferReply().complete();
        if(!guild.getAudioManager().isConnected()) {
            Inicium.manager.getPlayer(guild).getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_QUEUE_TITLE, StringsConst.MESSAGE_NO_MUSIC_IN_PROGRESS, user, false, hook);
        } else {
            Inicium.manager.getPlayer(guild).getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_QUEUE_TITLE, StringsConst.COMMAND_QUEUE_SUCCESS, user, true, hook);
        }
        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return true;
    }
}
