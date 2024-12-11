package fr.fonkio.reply.impl;

import fr.fonkio.enums.InterractionIdEnum;
import fr.fonkio.reply.AbstractReply;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.enums.ConfigurationGuildEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.List;

public class GoodbyeReply extends AbstractReply {

    @Override
    public boolean reply(IReplyCallback event, Guild guild, User user) {

        Member member = guild.getMember(user);
        if (member != null) {
            if (!member.hasPermission(Permission.ADMINISTRATOR)) {
                permissionCheck(event, user);
                return true;
            }

            List<SelectOption> optionList = getSelectOptionsChannelList(guild, ConfigurationGuildEnum.QUIT_CHANNEL);
            event.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_GOODBYE_TITLE, StringsConst.COMMAND_GOODBYE_SUCCESS))
                    .addActionRow(
                            StringSelectMenu.create(InterractionIdEnum.GOODBYE_SELECT_MENU.getMainId())
                                    .setMinValues(0)
                                    .setMaxValues(1)
                                    .addOptions(optionList).build()
                    )
                    .setEphemeral(true)
                    .queue();
        }

        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return false;
    }
}