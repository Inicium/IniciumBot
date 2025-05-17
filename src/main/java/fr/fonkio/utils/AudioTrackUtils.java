package fr.fonkio.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.fonkio.inicium.Utils;
import org.apache.commons.lang3.StringUtils;

public class AudioTrackUtils {

    public static String getTitle(AudioTrack audioTrack) {
        String url = audioTrack.getInfo().uri;
        if (SunoUtils.isSunoUrl(url)) {
            return SunoUtils.getTitleFromUrl(url);
        }
        return audioTrack.getInfo().title;
    }

    public static String getThumbnailUrl(AudioTrack audioTrack) {
        String url = audioTrack.getInfo().uri;
        if (url.contains("youtube.com")) {
            return "http://i3.ytimg.com/vi/"+ url.split("v=")[1].split("&")[0] + "/maxresdefault.jpg";
        } else if (SunoUtils.isSunoUrl(url)) {
            return SunoUtils.getThumbnailUrlFromUrl(url);
        }
        return "";
    }

    public static String getStyle(AudioTrack audioTrack) {
        String url = audioTrack.getInfo().uri;
        if (SunoUtils.isSunoUrl(url)) {
            return SunoUtils.getStyleFromUrl(url);
        }
        return StringUtils.EMPTY;
    }

    public static String getDuration(AudioTrack audioTrack) {
        String duration;
        if (audioTrack.getInfo().isStream) {
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
        return audioTrack.getInfo().author;
    }
}
