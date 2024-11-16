package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.inicium.Utils;
import fr.fonkio.message.MusicPlayer;
import fr.fonkio.message.StringsConst;
import fr.fonkio.enums.ConfigurationBotEnum;
import fr.fonkio.enums.ConfigurationGuildEnum;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.Timer;
import java.util.TimerTask;

public class CommandDisconnect extends AbstractCommand {

    @Override
    public boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        GenericInteractionCreateEvent event = eventSlash != null ? eventSlash : eventButton;
        InteractionHook hook = eventSlash != null ? eventSlash.deferReply().complete() : eventButton.deferReply().complete();

        Guild guild = event.getGuild();
        User user = event.getUser();

        if (guild != null) {
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
                Inicium.manager.getPlayer(guild).getPlayerMessage().updatePlayerMessage(StringsConst.COMMAND_DISCONNECT_TITLE,StringsConst.COMMAND_DISCONNECT_SUCCESS, user, false, hook);


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
        }
        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return true;
    }
}
