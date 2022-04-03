package fr.fonkio.listener.impl;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.MusicPlayer;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventGuildVoiceLeave extends ListenerAdapter {

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        Guild guild = event.getGuild();
        AudioChannel voiceChannel = event.getChannelLeft();
        if(!guild.getAudioManager().isConnected()) {
            return;
        }
        if (voiceChannel.equals(guild.getAudioManager().getConnectedChannel())) { //Si c'est le channel du bot
            if(voiceChannel.getMembers().size() == 1) {//Si il ne reste plus que le bot
                MusicPlayer player = Inicium.manager.getPlayer(guild);
                player.getListener().getTracks().clear();
                player.getAudioPlayer().stopTrack();
                guild.getAudioManager().closeAudioConnection();
            }
        }
    }
}
