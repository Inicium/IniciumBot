package fr.fonkio.reply.impl;

import fr.fonkio.reply.AbstractPlaylistReply;
import fr.fonkio.enums.InterractionIdEnum;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class PlaylistRemoveReply extends AbstractPlaylistReply {
    @Override
    protected boolean reply(IReplyCallback event, Guild guild, User user) {
        replyPlaylist(event, StringsConst.COMMAND_PLAYLIST_REMOVE_TITLE, StringsConst.COMMAND_PLAYLIST_REMOVE_SUCCESS, "❌", InterractionIdEnum.PLAYLIST_REMOVE_SELECT_MENU, true);
        return true;
    }
}
