package fr.fonkio.reply.impl;

import fr.fonkio.reply.AbstractReply;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

import java.awt.*;

public class HelpAdminReply extends AbstractReply {
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

        EmbedBuilder eb = getHelpadminEmbedBuilder();
        eb.setAuthor(user.getName(), null, user.getAvatarUrl());
        event.replyEmbeds(
                eb.build()
        ).setEphemeral(true).queue();

        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return false;
    }

    private EmbedBuilder getHelpadminEmbedBuilder() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(StringsConst.COMMAND_HELPADMIN_TITLE);
        String prefix = "/";
        eb.addBlankField(false);
        eb.addField(prefix+"welcome", StringsConst.COMMAND_WELCOME_DESC, false);
        eb.addField(prefix+"goodbye", StringsConst.COMMAND_GOODBYE_DESC, false);
        eb.addField(prefix+"blacklist", StringsConst.COMMAND_BLACKLIST_DESC, false);
        eb.addField(prefix+"disconnectsong" + ", " + prefix + "dcsong", StringsConst.COMMAND_DISCONNECT_SONG_DESC, false);

        eb.setColor(Color.GREEN);
        return eb;
    }
}
