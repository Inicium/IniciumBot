package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import java.awt.*;

public class CommandHelpadmin extends AbstractCommand {
    @Override
    public boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        if (eventSlash == null) {
            return false;
        }

        Guild guild = eventSlash.getGuild();
        User user = eventSlash.getUser();

        if (guild != null) {
            Member member = guild.getMember(user);
            if (member != null) {
                if (!member.hasPermission(Permission.ADMINISTRATOR)) {
                    permissionCheck(eventSlash, user);
                    return true;
                }
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle(StringsConst.COMMAND_HELPADMIN_TITLE);
                String prefix = "/";
                eb.addBlankField(false);
                eb.addField(prefix+"welcome", StringsConst.COMMAND_WELCOME_DESC, false);
                eb.addField(prefix+"goodbye", StringsConst.COMMAND_GOODBYE_DESC, false);
                eb.addField(prefix+"blacklist", StringsConst.COMMAND_BLACKLIST_DESC, false);
                eb.addField(prefix+"disconnectsong" + ", " + prefix + "dcsong", StringsConst.COMMAND_DISCONNECT_SONG_DESC, false);

                eb.setColor(Color.GREEN);
                eb.setAuthor(user.getName(), null, user.getAvatarUrl());
                eventSlash.replyEmbeds(
                        eb.build()
                ).setEphemeral(true).queue();

            }
        }
        return true;
    }
}
