package fr.fonkio.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AudioListener extends AudioEventAdapter {

    private final BlockingQueue<AudioTrack> audioTrackBlockingQueue = new LinkedBlockingQueue<>();
    private final MusicPlayer musicPlayer;

    public AudioListener(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public BlockingQueue<AudioTrack> getAudioTrackBlockingQueue() {
        return audioTrackBlockingQueue;
    }

    public void shuffle() {
        List<AudioTrack> queue = new ArrayList<>(audioTrackBlockingQueue);
        Collections.shuffle(queue);
        audioTrackBlockingQueue.clear();
        audioTrackBlockingQueue.addAll(queue);
    }

    public void nextTrack() {
        if(audioTrackBlockingQueue.isEmpty()) {
            if(musicPlayer.getGuild().getAudioManager().getConnectedChannel() != null) {
                musicPlayer.getAudioPlayer().destroy();
            }
            return;
        }
        musicPlayer.getAudioPlayer().startTrack(audioTrackBlockingQueue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext) {
            nextTrack();
        }
    }

    public void queue(AudioTrack track) {
        if (!musicPlayer.getAudioPlayer().startTrack(track, true)) {
            audioTrackBlockingQueue.add(track);
        }
    }

    public List<AudioTrack> getQueue() {

        List<AudioTrack> queue = new LinkedList<>();
        queue.add(musicPlayer.getAudioPlayer().getPlayingTrack());
        queue.addAll(audioTrackBlockingQueue);

        return queue;
    }
}

