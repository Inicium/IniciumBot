package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
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

        InteractionHook hook = eventSlash.deferReply().complete();

        Guild guild = eventSlash.getGuild();
        User user = eventSlash.getUser();

        if(guild != null) {
            if (canNotSendCommand(user, guild, hook)) {
                return true;
            }
            OptionMapping timeOption = eventSlash.getOption("time");
            String timeParameter = "";
            if(timeOption != null) {
                timeParameter = timeOption.getAsString();
            }

            if(!guild.getAudioManager().isConnected()) {
                Inicium.manager.getPlayer(guild).getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_SEEK_TITLE, StringsConst.MESSAGE_NO_MUSIC_IN_PROGRESS, user, false, hook);
                return true;
            }
            try {
                Inicium.manager.getPlayer(guild).seekTrack(timeParameter);
            } catch (IllegalArgumentException e) {
                Inicium.manager.getPlayer(guild).getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_SEEK_TITLE,StringsConst.COMMAND_SEEK_INVALID_FORMAT, user, true, hook);
                return true;
            }
            Inicium.manager.getPlayer(guild).getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_SEEK_TITLE,StringsConst.COMMAND_SEEK_SUCCESS + timeParameter, user, true, hook);
        }

        return true;
    }
}
