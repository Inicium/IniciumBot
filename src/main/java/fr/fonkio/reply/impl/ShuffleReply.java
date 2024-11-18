package fr.fonkio.reply.impl;

import fr.fonkio.reply.AbstractReply;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.inicium.Utils;
import fr.fonkio.music.MusicPlayer;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class ShuffleReply extends AbstractReply {

    @Override
    public boolean reply(IReplyCallback event, Guild guild, User user) {
        InteractionHook hook = event.deferReply().complete();

        if (Utils.checkUserAndBotNoPermission(user, guild, hook)) {
            return true;
        }

        MusicPlayer player = Inicium.manager.getPlayer(guild);
        if(!guild.getAudioManager().isConnected()) {
            player.getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_SHUFFLE_TITLE, StringsConst.MESSAGE_NO_MUSIC_IN_PROGRESS, user, false, hook);
            return true;
        }

        player.shuffle();
        player.getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_SHUFFLE_TITLE, StringsConst.COMMAND_SHUFFLE_SUCCESS, user, true, hook);
        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return true;
    }
}
