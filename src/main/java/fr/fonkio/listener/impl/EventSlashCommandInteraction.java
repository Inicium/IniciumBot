package fr.fonkio.listener.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.EmbedGenerator;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventSlashCommandInteraction extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild != null) {
            AbstractCommand commandRunner = Inicium.commands.get(event.getName());
            if (commandRunner.isBlacklistable() && Inicium.CONFIGURATION.blackListContains(guild.getId(), event.getTextChannel().getId())) {
                event.replyEmbeds(
                        EmbedGenerator.generate(
                                event.getUser(),
                                "Channel blacklisté",
                                "Ce channel est dans la blacklist pour l'envoi de commande. Merci d'envoyer des commandes dans les channels prévus à cet effet")
                ).setEphemeral(true).queue();
            } else {
                commandRunner.run(event, null);
            }

        }

    }
}
