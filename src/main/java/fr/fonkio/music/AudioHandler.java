package fr.fonkio.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class AudioHandler implements AudioSendHandler {

    private final AudioPlayer audioPlayer;
    private AudioFrame lastFrame;

    public AudioHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;

    }

    @Override
    public boolean canProvide() {
        if (lastFrame == null) {
            lastFrame = audioPlayer.provide();
        }
        return lastFrame != null;

    }

    @Override
    public ByteBuffer provide20MsAudio() {
        byte[] data = canProvide() ? lastFrame.getData() : null;
        lastFrame = null;
        return data != null ? ByteBuffer.wrap(data) : null;
    }

    @Override
    public boolean isOpus() {
        return true;
    }

}

