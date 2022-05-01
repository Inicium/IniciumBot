package fr.fonkio.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import fr.fonkio.message.MusicPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AudioListener extends AudioEventAdapter {

    private final BlockingQueue<AudioTrack> tracks = new LinkedBlockingQueue<>();
    private final MusicPlayer player;

    public AudioListener(MusicPlayer player) {
        this.player = player;
    }

    public BlockingQueue<AudioTrack> getTracks() {
        return tracks;
    }

    public int getTrackSize() {
        return tracks.size();
    }

    public void shuffle() {
        List<AudioTrack> queue = new ArrayList<>(tracks);
        Collections.shuffle(queue);
        tracks.clear();
        tracks.addAll(queue);
    }

    public void nextTrack() {
        if(tracks.isEmpty()) {
            if(player.getGuild().getAudioManager().getConnectedChannel() != null) {
                player.getAudioPlayer().destroy();
            }
            return;
        }
        player.getAudioPlayer().startTrack(tracks.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext) {
            nextTrack();
        }
    }

    public void queue(AudioTrack track) {
        if (!player.getAudioPlayer().startTrack(track, true)) {
            tracks.add(track);
        }
    }

    public List<AudioTrack> getQueue() {

        List<AudioTrack> queue = new LinkedList<>();
        queue.add(player.getAudioPlayer().getPlayingTrack());
        queue.addAll(tracks);

        return queue;
    }
}

