package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.MusicPlayer;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class CommandClear extends AbstractCommand {

    public CommandClear() {
        blacklistable = true;
    }

    @Override
    public boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        GenericInteractionCreateEvent event = eventSlash != null ? eventSlash : eventButton;
        Guild guild = event.getGuild();
        User user = event.getUser();
        if (guild != null) {
            if (canNotSendCommand(user, guild, event)) {
                return true;
            }
            MusicPlayer player = Inicium.manager.getPlayer(guild);
            if(player.getListener().getTracks().isEmpty()) {
                player.getPlayerMessage().newMessage(StringsConst.COMMAND_CLEAR_TITLE, StringsConst.COMMAND_CLEAR_ALREADY_EMPTY, user, true, event);
                return true;
            }
            player.getListener().getTracks().clear();
            player.getPlayerMessage().newMessage(StringsConst.COMMAND_CLEAR_TITLE, StringsConst.COMMAND_CLEAR_SUCCESS, user, true, event);
        }

        return true;
    }
}
