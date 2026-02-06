package fr.fonkio.reply.impl;

import fr.fonkio.reply.AbstractReply;
import fr.fonkio.enums.InterractionIdEnum;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.enums.ConfigurationGuildEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class DisconnectSongReply extends AbstractReply {
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

        Button button;
        boolean isActive = "true".equals(Inicium.CONFIGURATION.getGuildConfig(guild.getId(), ConfigurationGuildEnum.DC_SONG));
        if (isActive) {
            button = Button.danger(InterractionIdEnum.DISCONNECT_SONG_BUTTON.getMainId(), "Désactiver");
        } else {
            button = Button.success(InterractionIdEnum.DISCONNECT_SONG_BUTTON.getMainId(), "Activer");
        }
        event.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_DISCONNECT_SONG_TITLE, String.format(StringsConst.COMMAND_DISCONNECT_SONG_SUCCESS, isActive ? "active" : "inactive")))
                .setComponents(ActionRow.of(button))
                .setEphemeral(true).queue();
        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return false;
    }

}
