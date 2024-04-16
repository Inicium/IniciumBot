package fr.fonkio.message;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.fonkio.inicium.Utils;
import fr.fonkio.music.AudioHandler;
import fr.fonkio.music.AudioListener;
import fr.fonkio.music.PlayerMessage;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MusicPlayer {
    private final Logger logger = LoggerFactory.getLogger(MusicPlayer.class);
    private final AudioPlayer audioPlayer;
    private final AudioListener listener;
    private final Guild guild;
    private PlayerMessage playerMessage;

    public MusicPlayer(AudioPlayer audioPlayer, Guild guild) {
        this.audioPlayer = audioPlayer;
        this.guild = guild;
        this.listener = new AudioListener(this);
        this.playerMessage = new PlayerMessage(this);
        audioPlayer.addListener(listener);
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public AudioListener getListener() {
        return listener;
    }

    public Guild getGuild() {
        return guild;
    }

    public AudioHandler getAudioHandler() {
        return new AudioHandler(audioPlayer);
    }

    public synchronized void playTrack(AudioTrack track) {
        listener.queue(track);
        logger.info(Utils.getFormattedLogString(guild, "Le titre " + track.getInfo().title + " a été ajouté à la file"));
    }

    public synchronized void skipTrack() {
        listener.nextTrack();
        logger.info(Utils.getFormattedLogString(guild, "Skiped"));
    }

    public void shuffle() {
        listener.shuffle();
        logger.info(Utils.getFormattedLogString(guild, "Shuffled"));
    }

    public void seekTrack(String position) throws IllegalArgumentException {
        String [] time = position.split(":");
        Long l = null;
        switch (time.length) {
            case 1:
                l = Long.parseLong(time[0])*1000;
                break;
            case 2:
                l = (Long.parseLong(time[1])*1000)+(Long.parseLong(time[0])*60000);
                break;
            case 3:
                l = (Long.parseLong(time[2])*1000)+(Long.parseLong(time[1])*60000)+(Long.parseLong(time[0])*3600000);
                break;
        }
        if (l == null) {
            throw new IllegalArgumentException();
        }
        AudioTrack track = audioPlayer.getPlayingTrack();
        if (track == null) return;
        track.setPosition(l);
        logger.info(Utils.getFormattedLogString(guild, "Siked to " + position));
    }

    public List<AudioTrack> getQueue() {
        return listener.getQueue();
    }

    public boolean isPause() {
        return audioPlayer.isPaused();
    }

    public void setPause(boolean b) {
        audioPlayer.setPaused(b);
        logger.info(Utils.getFormattedLogString(guild,"Pause = " + b));
    }

    public PlayerMessage getPlayerMessage() {
        return playerMessage;
    }

    public void setPlayerMessage(PlayerMessage playerMessage) {
        this.playerMessage = playerMessage;
    }

}
