package fr.fonkio.reply.impl;

import fr.fonkio.reply.AbstractReply;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.inicium.Utils;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class PauseReply extends AbstractReply {

    @Override
    public boolean reply(IReplyCallback event, Guild guild, User user) {
        InteractionHook hook = event.deferReply().complete();

        if (Utils.checkUserAndBotNoPermission(user, guild, hook)) {
            return true;
        }

        if(!guild.getAudioManager().isConnected()) {
            Inicium.manager.getPlayer(guild).getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_PAUSE_TITLE, StringsConst.MESSAGE_NO_MUSIC_IN_PROGRESS, user, false, hook);
            return true;
        }

        if(Inicium.manager.getPlayer(guild).isPause()) {
            Inicium.manager.getPlayer(guild).getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_PAUSE_TITLE, StringsConst.COMMAND_PAUSE_ALREADY_PAUSED, user, false, hook);
            return true;
        }

        Inicium.manager.getPlayer(guild).setPause(true);
        Inicium.manager.getPlayer(guild).getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_PAUSE_TITLE, "", user, true, hook);

        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return true;
    }
}
