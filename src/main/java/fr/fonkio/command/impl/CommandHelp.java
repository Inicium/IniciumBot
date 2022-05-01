package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.awt.*;

public class CommandHelp extends AbstractCommand {



    @Override
    public boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        if (eventSlash == null) {
            return false;
        }
        User user = eventSlash.getUser();
        Guild guild = eventSlash.getGuild();
        if (guild != null) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setAuthor(user.getName(), null, user.getAvatarUrl());
            eb.setTitle(StringsConst.COMMAND_HELP_TITLE);
            String prefix = "/";
            eb.addBlankField(false);
            eb.addField(prefix+"play [Musique], "+prefix+"p [...]", StringsConst.COMMAND_PLAY_DESC, false);
            eb.addField(prefix+"skip, "+prefix+"s", StringsConst.COMMAND_SKIP_DESC, true);
            eb.addField(prefix+"pause", StringsConst.COMMAND_PAUSE_DESC, true);
            eb.addField(prefix+"resume", StringsConst.COMMAND_RESUME_DESC, true);
            eb.addField(prefix+"ps [Musique]", StringsConst.COMMAND_PLAYSKIP_DESC, false);
            eb.addField(prefix+"seek [Temps]", StringsConst.COMMAND_SEEK_DESC, false);
            eb.addField(prefix+"leave, "+prefix+"quit, "+prefix+"disconnect, "+prefix+"dc", StringsConst.COMMAND_DISCONNECT_DESC, false);
            eb.addField(prefix+"clear, "+prefix+"clean, "+prefix+"clr", StringsConst.COMMAND_CLEAR_DESC, true);
            eb.addField(prefix+"queue, "+prefix+"np", StringsConst.COMMAND_QUEUE_DESC, true);
            eb.addField(prefix+"shuffle", StringsConst.COMMAND_SHUFFLE_DESC, false);
            eb.addField(prefix+"moveall [Destination], "+prefix+"mva [...]", StringsConst.COMMAND_MOVEALL_DESC, false);
            eb.addBlankField(false);
            eb.addField("Boutons d'action", "Vous pouvez effectuer des actions avec les boutons d'action", false);
            eb.setFooter("Pour voir les commandes admin : " + prefix+"helpadmin");
            eb.setColor(Color.GREEN);
            eb.setImage("https://cdn.discordapp.com/attachments/297407304871968768/854394142761943060/unknown.png");
            eb.setAuthor(user.getName(), null, user.getAvatarUrl());

            eventSlash.replyEmbeds(
                    eb.build()
            ).setEphemeral(true).queue();
        }
        return true;
    }
}
