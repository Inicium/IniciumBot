package fr.fonkio.reply.impl;

import fr.fonkio.reply.AbstractReply;
import fr.fonkio.enums.InterractionIdEnum;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.enums.ConfigurationGuildEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.List;

public class BlacklistReply extends AbstractReply {

    @Override
    public boolean reply(IReplyCallback event, Guild guild, User user) {
        if (event == null) {
            return false;
        }

        Member member = guild.getMember(user);
        if (member != null && !member.hasPermission(Permission.ADMINISTRATOR)) {
            permissionCheck(event, user);
        } else {
            List<SelectOption> optionList = getSelectOptionsChannelList(guild, ConfigurationGuildEnum.BLACK_LIST);
            event.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_BLACKLIST_TITLE, StringsConst.COMMAND_BLACKLIST_SUCCESS))
                    .addActionRow(
                            StringSelectMenu.create(InterractionIdEnum.BLACKLIST_SELECT_MENU.getMainId())
                                    .setMinValues(0)
                                    .setMaxValues(optionList.size()-1)
                                    .addOptions(optionList).build()
                    )
                    .setEphemeral(true).queue();
        }
        return true;


    }

    @Override
    public boolean isBlacklistable() {
        return false;
    }
}
