package fr.fonkio.reply.impl;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.inicium.Utils;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.reply.AbstractStringSelectMenuReply;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class PlaylistRemoveStringSelectMenuReply extends AbstractStringSelectMenuReply {
    @Override
    protected boolean reply(IReplyCallback event, Guild guild, User user) {
        if (!(event instanceof StringSelectInteractionEvent)) {
            return false;
        }
        StringSelectInteractionEvent stringSelectEvent = (StringSelectInteractionEvent) event;
        Inicium.CONFIGURATION.removeFromPlaylist(guild.getId(), stringSelectEvent.getValues());
        event.replyEmbeds(
                        EmbedGenerator.generate(
                                event.getUser(),
                                StringsConst.COMMAND_PLAYLIST_TITLE,
                                StringsConst.SELECT_PLAYLIST_REMOVE_SUCCESS
                        )
                ).setEphemeral(true)
                .addActionRow(Utils.getPlaylistButtons(null))
                .queue();
        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return true;
    }
}
