package fr.fonkio.listener.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.entities.Guild;
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
                                StringsConst.MESSAGE_BLACKLISTED_CHANNEL_TITLE,
                                StringsConst.MESSAGE_BLACKLISTED)
                ).setEphemeral(true).queue();
            } else {
                boolean execution = commandRunner.run(event, null);
                if (!execution) {
                    event.replyEmbeds(EmbedGenerator.generate(event.getUser(), StringsConst.MESSAGE_ERROR_TITLE, StringsConst.MESSAGE_ERROR)).queue();
                }
            }

        }

    }
}
