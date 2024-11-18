package fr.fonkio.reply.impl;

import fr.fonkio.reply.AbstractPlaylistReply;
import fr.fonkio.message.StringsConst;
import fr.fonkio.enums.InterractionIdEnum;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;


public class PlaylistReply extends AbstractPlaylistReply {

    @Override
    public boolean reply(IReplyCallback event, Guild guild, User user) {
        replyPlaylist(event, StringsConst.COMMAND_PLAYLIST_TITLE, StringsConst.COMMAND_PLAYLIST_SUCCESS, "⭐", InterractionIdEnum.PLAYLIST_SELECT_MENU, false);
        return true;
    }
}
