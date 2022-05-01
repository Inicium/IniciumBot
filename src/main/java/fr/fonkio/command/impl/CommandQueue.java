package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class CommandQueue extends AbstractCommand {

    public CommandQueue() {
        blacklistable = true;
    }

    @Override
    public boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        GenericInteractionCreateEvent event = eventSlash != null ? eventSlash : eventButton;
        Guild guild = event.getGuild();
        User user = event.getUser();

        if(guild != null) {
            if(!guild.getAudioManager().isConnected()) {
                Inicium.manager.getPlayer(guild).getPlayerMessage().newMessage(StringsConst.COMMAND_QUEUE_TITLE, StringsConst.MESSAGE_NO_MUSIC_IN_PROGRESS, user, false, event);
            } else {
                Inicium.manager.getPlayer(guild).getPlayerMessage().newMessage(StringsConst.COMMAND_QUEUE_TITLE, StringsConst.COMMAND_QUEUE_SUCCESS, user, true, event);
            }
        }

        return true;
    }
}
