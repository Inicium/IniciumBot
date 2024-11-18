package fr.fonkio.reply.impl;

import fr.fonkio.reply.AbstractReply;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.inicium.Utils;
import fr.fonkio.music.MusicPlayer;
import fr.fonkio.message.StringsConst;
import fr.fonkio.enums.ConfigurationBotEnum;
import fr.fonkio.enums.ConfigurationGuildEnum;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

import java.util.Timer;
import java.util.TimerTask;

public class DisconnectReply extends AbstractReply {

    @Override
    public boolean reply(IReplyCallback event, Guild guild, User user) {
        InteractionHook hook = event.deferReply().complete();

        if (Utils.checkUserAndBotNoPermission(user, guild, hook)) {
            return true;
        }

        if(!guild.getAudioManager().isConnected()) {
            Inicium.manager.getPlayer(guild).getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_DISCONNECT_TITLE, StringsConst.COMMAND_DISCONNECT_ALREADY_DISCONNECTED, user, false, hook);
            return true;
        }

        MusicPlayer player = Inicium.manager.getPlayer(guild);
        player.getListener().getTracks().clear();
        player.getAudioPlayer().stopTrack();

        if("true".equals(Inicium.CONFIGURATION.getGuildConfig(guild.getId(), ConfigurationGuildEnum.DC_SONG))) {
            player.getAudioPlayer().setPaused(false);
            Inicium.manager.loadTrack(guild, Inicium.CONFIGURATION.getGlobalParam(ConfigurationBotEnum.DISCONNECT_SONG), user, hook, false);
            TimerTask task = new TimerTask() {
                public void run() {
                    guild.getAudioManager().closeAudioConnection();
                    player.getListener().getTracks().clear();
                    Inicium.manager.getPlayer(guild).getAudioPlayer().stopTrack();
                }
            };
            Timer timer = new Timer("Timer");

            long delay = 3000L;
            timer.schedule(task, delay);
        } else {
            guild.getAudioManager().closeAudioConnection();
        }
        Inicium.manager.getPlayer(guild).getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_DISCONNECT_TITLE,StringsConst.COMMAND_DISCONNECT_SUCCESS, user, false, hook);
        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return true;
    }
}
