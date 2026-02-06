package fr.fonkio.reply;

import fr.fonkio.enums.InterractionIdEnum;
import fr.fonkio.inicium.Utils;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.selections.SelectOption;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

import java.util.List;

public abstract class AbstractPlaylistReply extends AbstractReply {

    @Override
    public boolean isBlacklistable() {
        return true;
    }

    protected void replyPlaylist(IReplyCallback eventToAnswer, String title, String message, String emoji, InterractionIdEnum selectMenu, boolean isMultipleChoice) {
        if (eventToAnswer == null) {
            return;
        }
        Guild guild = eventToAnswer.getGuild();
        if (guild == null) {
            return;
        }
        User user = eventToAnswer.getUser();

        List<SelectOption> favoriteOptions = getSelectOptionsPlaylist(eventToAnswer.getGuild(), emoji);
        if (favoriteOptions.isEmpty()) {
            eventToAnswer.replyEmbeds(EmbedGenerator.generate(user, title, StringsConst.COMMAND_PLAYLIST_EMPTY_ERROR))
                    .setEphemeral(true).queue();

        } else {
            eventToAnswer.replyEmbeds(EmbedGenerator.generate(user, title, message))
                    .setComponents(
                            ActionRow.of(
                                    StringSelectMenu.create(selectMenu.getMainId())
                                            .setMinValues(1)
                                            .setMaxValues(isMultipleChoice ? favoriteOptions.size() : 1)
                                            .addOptions(favoriteOptions).build()
                            ),
                            ActionRow.of(
                                    Utils.getPlaylistButtons(selectMenu)
                            )
                    )
                    .setEphemeral(true).queue();
        }
    }


}
