package fr.fonkio.reply.impl;

import fr.fonkio.reply.AbstractReply;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.inicium.Utils;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;


public class SeekReply extends AbstractReply {

    @Override
    public boolean reply(IReplyCallback event, Guild guild, User user) {
        if (!(event instanceof SlashCommandInteractionEvent)) {
            return false;
        }
        SlashCommandInteractionEvent slashCommandEvent = (SlashCommandInteractionEvent) event;

        InteractionHook hook = slashCommandEvent.deferReply().complete();
        if (Utils.checkUserAndBotNoPermission(user, guild, hook)) {
            return true;
        }

        OptionMapping timeOption = slashCommandEvent.getOption("time");
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
        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return true;
    }
}
