package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.MusicPlayer;
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
                Inicium.manager.getPlayer(guild).getPlayerMessage().newMessage("\uD83D\uDDD1 Clear","La liste est déjà vide ...", user, true, event);
                return true;
            }
            player.getListener().getTracks().clear();
            Inicium.manager.getPlayer(guild).getPlayerMessage().newMessage("\uD83D\uDDD1 Clear","La liste a été effacée !", user, true, event);
        }

        return true;
    }
}
