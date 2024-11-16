package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.enums.SelectMenuIdEnum;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.List;

public class CommandPlaylist extends AbstractCommand {

    @Override
    public boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        if (eventSlash == null && eventButton == null) {
            return false;
        }
        User user;
        String playlistAction = "default";
        IReplyCallback eventToAnswer;
        if (eventSlash != null) {
            eventToAnswer = eventSlash;
            user = eventSlash.getUser();
            OptionMapping playlistActionOptionMapping = eventSlash.getOption("action");
            if (playlistActionOptionMapping != null) {
                playlistAction = playlistActionOptionMapping.getAsString();
            }
        } else {
            eventToAnswer = eventButton;
            user = eventButton.getUser();
        }

        Guild guild = eventToAnswer.getGuild();

        if (guild == null) {
            return false;
        }

        String title;
        String message;
        String emoji;
        SelectMenuIdEnum selectMenu;
        boolean isMultipleChoice;

        switch (playlistAction) {
            case "add":
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

                Modal modal = Modal.create("playlist-add", StringsConst.COMMAND_PLAYLIST_ADD_MODAL_TITLE)
                        .addComponents(ActionRow.of(label), ActionRow.of(url))
                        .build();
                eventSlash.replyModal(modal).queue();
                return true;
            case "remove":
                title = StringsConst.COMMAND_PLAYLIST_REMOVE_TITLE;
                message = StringsConst.COMMAND_PLAYLIST_REMOVE_SUCCESS;
                isMultipleChoice = true;
                emoji = "❌";
                selectMenu = SelectMenuIdEnum.PLAYLIST_REMOVE;
                break;
            default:
                title = StringsConst.COMMAND_PLAYLIST_TITLE;
                message = StringsConst.COMMAND_PLAYLIST_SUCCESS;
                isMultipleChoice = false;
                emoji = "⭐";
                selectMenu = SelectMenuIdEnum.PLAYLIST;
                break;
        }

        List<SelectOption> favoriteOptions = getSelectOptionsPlaylist(eventToAnswer.getGuild(), emoji);
        if (favoriteOptions.isEmpty()) {
            eventToAnswer.replyEmbeds(EmbedGenerator.generate(user, title, StringsConst.COMMAND_PLAYLIST_EMPTY_ERROR))
                    .setEphemeral(true).queue();

        } else {
            eventToAnswer.replyEmbeds(EmbedGenerator.generate(user, title, message))
                    .addActionRow(
                            StringSelectMenu.create(selectMenu.toString())
                                    .setMinValues(1)
                                    .setMaxValues(isMultipleChoice ? favoriteOptions.size() : 1)
                                    .addOptions(favoriteOptions).build()
                    )
                    .setEphemeral(true).queue();
        }

        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return true;
    }
}
