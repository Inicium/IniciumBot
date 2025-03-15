package fr.fonkio.listener.impl;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.inicium.Utils;
import fr.fonkio.music.MusicPlayer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventGuildVoiceLeave extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(EventGuildVoiceLeave.class);

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        AudioChannel voiceChannel = event.getChannelLeft();
        if (voiceChannel == null) {
            return;
        }
        Guild guild = event.getGuild();
        if(!guild.getAudioManager().isConnected()) {
            return;
        }
        if (voiceChannel.equals(guild.getAudioManager().getConnectedChannel())) { //Si c'est le channel du bot
            if(voiceChannel.getMembers().size() == 1) {//Si il ne reste plus que le bot
                logger.info(Utils.getFormattedLogString(guild, "Il ne reste plus que le bot connecté dans le channel " + voiceChannel.getName()));
                MusicPlayer player = Inicium.manager.getPlayer(guild);
                player.getListener().getAudioTrackBlockingQueue().clear();
                player.getAudioPlayer().stopTrack();
                guild.getAudioManager().closeAudioConnection();
                logger.info(Utils.getFormattedLogString(guild, "Bot déconnecté du channel vocal"));
            }
        }
    }
}
