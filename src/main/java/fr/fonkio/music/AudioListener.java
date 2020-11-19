package fr.fonkio.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

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

    public void nexTrack() {
        if(tracks.isEmpty()) {
            if(player.getGuild().getAudioManager().getConnectedChannel() != null) {
                player.getAudioPlayer().startTrack(null, false);
            }
            return;
        }
        player.getAudioPlayer().startTrack(tracks.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext) {
            nexTrack();
        }
    }

    public void queue(AudioTrack track) {
        if (!player.getAudioPlayer().startTrack(track, true)) {
            tracks.offer(track);
        }
    }

    public List<AudioTrack> getQueue() {

        List<AudioTrack> queue = new LinkedList<AudioTrack>();
        queue.add(player.getAudioPlayer().getPlayingTrack());
        queue.addAll(tracks);
		/*for(Iterator<AudioTrack> it = tracks.iterator(); it.hasNext();) {
			AudioTrack at = it.next();
		}*/

        return queue;
    }
}

