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

import java.util.Timer;
import java.util.TimerTask;

public class CommandDisconnect extends AbstractCommand {

    public CommandDisconnect() {
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
            if(!guild.getAudioManager().isConnected()) {
                return true;
            }
            MusicPlayer player = Inicium.manager.getPlayer(guild);
            player.getListener().getTracks().clear();
            player.getAudioPlayer().stopTrack();

            if(guild.getId().equals("296520788033404929")) {
                player.getAudioPlayer().setPaused(false);
                Inicium.manager.loadTrack(guild, Inicium.CONFIGURATION.getDCsong(), user, event, false);
                Inicium.manager.getPlayer(guild).getPlayerMessage().editMessage(StringsConst.COMMAND_DISCONNECT_TITLE,StringsConst.COMMAND_DISCONNECT_SUCCESS, user, false, event);


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
}
