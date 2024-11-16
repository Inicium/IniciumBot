package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

public class CommandQueue extends AbstractCommand {

    @Override
    public boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        GenericInteractionCreateEvent event = eventSlash != null ? eventSlash : eventButton;
        InteractionHook hook = eventSlash != null ? eventSlash.deferReply().complete() : eventButton.deferReply().complete();
        Guild guild = event.getGuild();
        User user = event.getUser();

        if(guild != null) {
            if(!guild.getAudioManager().isConnected()) {
                Inicium.manager.getPlayer(guild).getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_QUEUE_TITLE, StringsConst.MESSAGE_NO_MUSIC_IN_PROGRESS, user, false, hook);
            } else {
                Inicium.manager.getPlayer(guild).getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_QUEUE_TITLE, StringsConst.COMMAND_QUEUE_SUCCESS, user, true, hook);
            }
        }

        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return true;
    }
}
