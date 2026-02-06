package fr.fonkio.reply.impl;

import fr.fonkio.enums.InterractionIdEnum;
import fr.fonkio.reply.AbstractReply;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.enums.ConfigurationGuildEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.selections.SelectOption;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

import java.util.List;

public class WelcomeReply extends AbstractReply {
    @Override
    public boolean reply(IReplyCallback event, Guild guild, User user) {
        Member member = guild.getMember(user);
        if (member == null) {
            return false;
        }

        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            permissionCheck(event, user);
            return true;
        }

        List<SelectOption> optionList = getSelectOptionsChannelList(guild, ConfigurationGuildEnum.WELCOME_CHANNEL);
        event.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_WELCOME_TITLE, StringsConst.COMMAND_WELCOME_SUCCESS))
                .setComponents(
                        ActionRow.of(
                                StringSelectMenu.create(InterractionIdEnum.WELCOME_SELECT_MENU.getMainId())
                                        .setMinValues(0)
                                        .setMaxValues(1)
                                        .addOptions(optionList).build()
                        )
                ).setEphemeral(true).queue();
        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return false;
    }
}
