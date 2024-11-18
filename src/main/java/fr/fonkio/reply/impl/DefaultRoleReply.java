package fr.fonkio.reply.impl;

import fr.fonkio.enums.InterractionIdEnum;
import fr.fonkio.reply.AbstractReply;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.List;

public class DefaultRoleReply extends AbstractReply {

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

        List<SelectOption> optionList = getSelectOptionsRoleList(guild);
        event.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_DEFAULT_ROLE_TITLE, StringsConst.COMMAND_DEFAULT_ROLE_SUCCESS))
                .addActionRow(
                        StringSelectMenu.create(InterractionIdEnum.DEFAULT_ROLE_SELECT_MENU.getMainId())
                                .setMinValues(0)
                                .setMaxValues(1)
                                .addOptions(optionList).build()
                )
                .setEphemeral(true).queue();
        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return false;
    }
}
