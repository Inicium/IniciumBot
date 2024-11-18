package fr.fonkio.reply.impl;

import fr.fonkio.enums.InterractionIdEnum;
import fr.fonkio.reply.AbstractPlaylistReply;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class PlaylistAddReply extends AbstractPlaylistReply {
    @Override
    protected boolean reply(IReplyCallback event, Guild guild, User user) {
        if (!(event instanceof GenericComponentInteractionCreateEvent)) {
            return false;
        }

        GenericComponentInteractionCreateEvent createEvent = (GenericComponentInteractionCreateEvent) event;
        TextInput label = TextInput.create("label", StringsConst.COMMAND_PLAYLIST_ADD_MODAL_LABEL, TextInputStyle.SHORT)
                .setPlaceholder(StringsConst.COMMAND_PLAYLIST_ADD_MODAL_LABEL_PLACEHOLDER)
                .setMinLength(1)
                .setMaxLength(100)
                .build();

        TextInput url = TextInput.create("url", StringsConst.COMMAND_PLAYLIST_ADD_MODAL_URL, TextInputStyle.PARAGRAPH)
                .setPlaceholder(StringsConst.COMMAND_PLAYLIST_ADD_MODAL_URL_PLACEHOLDER)
                .setMinLength(8)
                .setMaxLength(1000)
                .build();

        Modal modal = Modal.create(InterractionIdEnum.PLAYLIST_ADD_MODAL.getMainId(), StringsConst.COMMAND_PLAYLIST_ADD_MODAL_TITLE)
                .addComponents(ActionRow.of(label), ActionRow.of(url))
                .build();
        createEvent.replyModal(modal).queue();
        return true;
    }
}
