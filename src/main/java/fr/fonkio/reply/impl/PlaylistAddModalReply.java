package fr.fonkio.reply.impl;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.inicium.Utils;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.reply.AbstractReply;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

public class PlaylistAddModalReply extends AbstractReply {
    @Override
    protected boolean reply(IReplyCallback event, Guild guild, User user) {
        if (!(event instanceof ModalInteractionEvent)) {
            return false;
        }
        ModalInteractionEvent modalEvent = (ModalInteractionEvent) event;
        String url = "";
        String label = "";
        for(ModalMapping modalMapping : modalEvent.getValues()) {
            switch (modalMapping.getCustomId()) {
                case "url":
                    url = modalMapping.getAsString();
                    break;
                case "label":
                    label = modalMapping.getAsString();
                    break;
            }
        }

        try {
            Inicium.CONFIGURATION.addToPlaylist(guild.getId(), url, label);
            event.replyEmbeds(
                            EmbedGenerator.generate(
                                    event.getUser(),
                                    StringsConst.COMMAND_PLAYLIST_TITLE,
                                    String.format(StringsConst.MODAL_PLAYLIST_ADD_SUCCESS,
                                            label)
                            )
                    ).setEphemeral(true)
                    .setComponents(ActionRow.of(Utils.getPlaylistButtons(null)))
                    .queue();
        } catch (IllegalStateException exception) {
            event.replyEmbeds(EmbedGenerator.generate(event.getUser(), StringsConst.COMMAND_PLAYLIST_TITLE, exception.getMessage())).setEphemeral(true).queue();
        }
        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return true;
    }
}
