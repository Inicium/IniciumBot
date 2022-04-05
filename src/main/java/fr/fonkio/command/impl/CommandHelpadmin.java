package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.utils.ConfigurationEnum;
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
                eb.setTitle("Liste des commandes");
                String prefix = "/";
                eb.addBlankField(false);
                eb.addField(prefix+"welcome", "Définir un channel pour l'affichage d'un message de bienvenue", false);
                eb.addField(prefix+"goodbye", "Définir un channel pour l'affichage d'un message lors d'un départ", false);
                eb.addField(prefix+"blacklist", "Définir les channels où l'on ne peut pas executer de commande", false);

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
