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

public class CommandResume extends AbstractCommand {

    public CommandResume() {
        blacklistable = true;
    }

    @Override
    public boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        GenericInteractionCreateEvent event = eventSlash != null ? eventSlash : eventButton;
        InteractionHook hook = eventSlash != null ? eventSlash.deferReply().complete() : eventButton.getHook();
        Guild guild = event.getGuild();
        User user = event.getUser();

        if(guild != null) {
            if (canNotSendCommand(user, guild, hook)) {
                return true;
            }
            if(!guild.getAudioManager().isConnected()) {
                Inicium.manager.getPlayer(guild).getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_RESUME_TITLE, StringsConst.MESSAGE_NO_MUSIC_IN_PROGRESS, user, false, hook);
            } else if(Inicium.manager.getPlayer(guild).isPause()) {
                Inicium.manager.getPlayer(guild).setPause(false);
                Inicium.manager.getPlayer(guild).getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_RESUME_TITLE, "", user, true, hook);
            } else {
                Inicium.manager.getPlayer(guild).getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_RESUME_TITLE,StringsConst.COMMAND_RESUME_ALREDY_PLAYING, user, true, hook);
            }
        }
        return true;
    }
}
