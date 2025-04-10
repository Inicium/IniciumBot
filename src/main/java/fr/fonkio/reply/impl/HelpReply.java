package fr.fonkio.reply.impl;

import fr.fonkio.reply.AbstractReply;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

import java.awt.*;

public class HelpReply extends AbstractReply {

    @Override
    public boolean reply(IReplyCallback event, Guild guild, User user) {

        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(user.getName(), null, user.getAvatarUrl());
        eb.setTitle(StringsConst.COMMAND_HELP_TITLE);
        String prefix = " /";
        eb.addBlankField(false);
        eb.addField(StringsConst.COMMAND_PLAY_EMOTE + prefix+"play [Musique], "+prefix+"p [...]", StringsConst.COMMAND_PLAY_DESC, false);
        eb.addField(StringsConst.COMMAND_SKIP_EMOTE + prefix+"skip, "+prefix+"s", StringsConst.COMMAND_SKIP_DESC, true);
        eb.addField(StringsConst.COMMAND_PAUSE_EMOTE + prefix+"pause", StringsConst.COMMAND_PAUSE_DESC, true);
        eb.addField(StringsConst.COMMAND_RESUME_EMOTE + prefix+"resume", StringsConst.COMMAND_RESUME_DESC, true);
        eb.addField(StringsConst.COMMAND_PLAYSKIP_EMOTE + prefix+"ps [Musique]", StringsConst.COMMAND_PLAYSKIP_DESC, false);
        eb.addField(StringsConst.COMMAND_SEEK_EMOTE + prefix+"seek [Temps]", StringsConst.COMMAND_SEEK_DESC, false);
        eb.addField(StringsConst.COMMAND_DISCONNECT_EMOTE + prefix+"leave, "+prefix+"quit, "+prefix+"disconnect, "+prefix+"dc", StringsConst.COMMAND_DISCONNECT_DESC, false);
        eb.addField(StringsConst.COMMAND_CLEAR_EMOTE + prefix+"clear, "+prefix+"clean, "+prefix+"clr", StringsConst.COMMAND_CLEAR_DESC, true);
        eb.addField(StringsConst.COMMAND_QUEUE_EMOTE + prefix+"queue, "+prefix+"np", StringsConst.COMMAND_QUEUE_DESC, true);
        eb.addField(StringsConst.COMMAND_SHUFFLE_EMOTE + prefix+"shuffle", StringsConst.COMMAND_SHUFFLE_DESC, false);
        eb.addField(StringsConst.COMMAND_MOVEALL_EMOTE + prefix+"moveall [Destination], "+prefix+"mva [...]", StringsConst.COMMAND_MOVEALL_DESC, false);
        eb.addField(StringsConst.COMMAND_PLAYLIST_EMOTE + prefix+"playlist", StringsConst.COMMAND_PLAYLIST_DESC, false);
        eb.addBlankField(false);
        eb.addField("Boutons d'action", "Vous pouvez effectuer des actions avec les boutons d'action", false);
        eb.setFooter("Pour voir les commandes admin : " + prefix+"helpadmin");
        eb.setColor(Color.GREEN);
        eb.setImage("https://github.com/user-attachments/assets/040bffdc-23f0-4b1e-b73a-51fab8991e24");
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
}
