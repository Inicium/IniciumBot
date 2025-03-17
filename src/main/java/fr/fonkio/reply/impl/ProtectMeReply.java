package fr.fonkio.reply.impl;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.reply.AbstractReply;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class ProtectMeReply extends AbstractReply {

    @Override
    protected boolean reply(IReplyCallback event, Guild guild, User user) {
        Member member = guild.getMember(user);

        if (member == null) {
            return false;
        }

        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            permissionCheck(event, user);
            return true;
        }

        String message;
        if (Inicium.protectedUserSet.contains(user)) {
            message = StringsConst.COMMAND_PROTECTME_UNPROTECTED_SUCCESS;
            Inicium.protectedUserSet.remove(user);
        } else {
            message = StringsConst.COMMAND_PROTECTME_PROTECTED_SUCCESS;
            Inicium.protectedUserSet.add(user);
        }
        event.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_PROTECTME_TITLE, message)).setEphemeral(true).queue();
        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return false;
    }
}
