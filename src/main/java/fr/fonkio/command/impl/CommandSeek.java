package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.inicium.Inicium;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;


public class CommandSeek extends AbstractCommand {

    public CommandSeek() {
        blacklistable = true;
    }

    @Override
    public boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        if (eventSlash == null) {
            return false;
        }

        Guild guild = eventSlash.getGuild();
        User user = eventSlash.getUser();

        if(guild != null) {
            if (canNotSendCommand(user, guild, eventSlash)) {
                return true;
            }
            OptionMapping timeOption = eventSlash.getOption("time");
            String timeParameter = "";
            if(timeOption != null) {
                timeParameter = timeOption.getAsString();
            }

            if(!guild.getAudioManager().isConnected()) {
                Inicium.manager.getPlayer(guild).getPlayerMessage().newMessage("Seek","Il n'y a pas de musique en cours ...", user, false, eventSlash);
                return true;
            }
            try {
                Inicium.manager.getPlayer(guild).seekTrack(timeParameter);
            } catch (IllegalArgumentException e) {
                Inicium.manager.getPlayer(guild).getPlayerMessage().newMessage("Seek","Le temps entré n'est pas valide", user, true, eventSlash);
                return true;
            }
            Inicium.manager.getPlayer(guild).getPlayerMessage().newMessage("Seek","La piste a été avancée à " + timeParameter, user, true, eventSlash);
        }

        return true;
    }
}
