package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.inicium.Inicium;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class CommandResume extends AbstractCommand {

    public CommandResume() {
        blacklistable = true;
    }

    @Override
    public boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        GenericInteractionCreateEvent event = eventSlash != null ? eventSlash : eventButton;
        Guild guild = event.getGuild();
        User user = event.getUser();

        if(guild != null) {
            if (canNotSendCommand(user, guild, event)) {
                return true;
            }
            if(!guild.getAudioManager().isConnected()) {
                Inicium.manager.getPlayer(guild).getPlayerMessage().newMessage("Resume","Il n'y a pas de musique en cours ...", user, false, event);
            } else if(Inicium.manager.getPlayer(guild).isPause()) {
                Inicium.manager.getPlayer(guild).setPause(false);
                Inicium.manager.getPlayer(guild).getPlayerMessage().newMessage("Resume","▶", user, true, event);
            } else {
                Inicium.manager.getPlayer(guild).getPlayerMessage().newMessage("Resume","Déjà en cours de lecture...", user, true, event);
            }
        }
        return true;
    }
}
