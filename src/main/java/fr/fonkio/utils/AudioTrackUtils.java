package fr.fonkio.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.fonkio.inicium.Utils;

public class AudioTrackUtils {

    public static String getTitle(AudioTrack audioTrack) {
        String url = audioTrack.getInfo().uri;
        if (SunoUtils.isSunoUrl(url)) {
            return SunoUtils.getTitleFromUrl(url);
        }
        return audioTrack.getInfo().title;
    }

    public static String getDuration(AudioTrack audioTrack) {
        String duration;
        if(audioTrack.getInfo().isStream) {
            duration = "STREAM";
        } else {
            duration = Utils.convertLongToString(audioTrack.getDuration());
        }
        return duration;
    }

    public static String getAuthor(AudioTrack audioTrack) {
        String url = audioTrack.getInfo().uri;
        if (SunoUtils.isSunoUrl(url)) {
            return SunoUtils.getAuthorFromUrl(url);
        }
        return audioTrack.getInfo().title;
    }
}
