package fr.fonkio.reply.impl;

import fr.fonkio.enums.InterractionIdEnum;
import fr.fonkio.reply.AbstractPlaylistReply;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.modals.Modal;

public class PlaylistAddReply extends AbstractPlaylistReply {
    @Override
    protected boolean reply(IReplyCallback event, Guild guild, User user) {
        if (!(event instanceof GenericComponentInteractionCreateEvent)) {
            return false;
        }

        GenericComponentInteractionCreateEvent createEvent = (GenericComponentInteractionCreateEvent) event;
        TextInput label = TextInput.create("label", TextInputStyle.SHORT)
                .setPlaceholder(StringsConst.COMMAND_PLAYLIST_ADD_MODAL_LABEL_PLACEHOLDER)
                .setMinLength(1)
                .setMaxLength(100)
                .build();

        TextInput url = TextInput.create("url", TextInputStyle.PARAGRAPH)
                .setPlaceholder(StringsConst.COMMAND_PLAYLIST_ADD_MODAL_URL_PLACEHOLDER)
                .setMinLength(8)
                .setMaxLength(1000)
                .build();

        Modal modal = Modal.create(InterractionIdEnum.PLAYLIST_ADD_MODAL.getMainId(), StringsConst.COMMAND_PLAYLIST_ADD_MODAL_TITLE)
                .addComponents(
                        Label.of(StringsConst.COMMAND_PLAYLIST_ADD_MODAL_LABEL, label),
                        Label.of(StringsConst.COMMAND_PLAYLIST_ADD_MODAL_URL, url))
                .build();
        createEvent.replyModal(modal).queue();
        return true;
    }
}
