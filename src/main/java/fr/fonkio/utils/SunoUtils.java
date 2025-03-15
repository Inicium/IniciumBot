package fr.fonkio.utils;

import fr.fonkio.message.StringsConst;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class SunoUtils {

    public static final String MP3_START_URL = "https://cdn1.suno.ai/";
    public static final String MP3_EXTENTION = ".mp3";

    public static final String AUDIOPIPE_START_URL = "https://audiopipe.suno.ai/?item_id=";

    public static final String SUNO_SONG_START_URL = "https://suno.com/song/";

    public static final String SUNO_PAGE_TITLE_SEPARATOR = " by @";

    public static String convertSunoUrlToSunoMp3Url(String sunoUrl) {
        String songId = sunoUrl.split("/song/")[1].split("[?]")[0];
        return MP3_START_URL + songId + MP3_EXTENTION;
    }

    public static String convertSunoMp3UrlToSunoUrl(String sunoMp3Url) {
        return sunoMp3Url.replace(MP3_START_URL, SUNO_SONG_START_URL).replace(MP3_EXTENTION, "");
    }

    public static String convertSunoAudiopipeUrlToSunoUrl(String sunoAudiopipeUrl) {
        return sunoAudiopipeUrl.replace(AUDIOPIPE_START_URL, SUNO_SONG_START_URL);
    }

    public static boolean isSunoUrl(String url) {
        return url.contains("suno.ai") || url.contains("suno.com");
    }

    public static boolean isMp3Url(String url) {
        return url.contains(MP3_START_URL);
    }

    public static boolean isAudiopipeUrl(String url) {
        return url.contains(AUDIOPIPE_START_URL);
    }

    public static String getTitleFromUrl(String url) {
        String pageTitle = getSunoPageTitle(url);
        return pageTitle.split(SUNO_PAGE_TITLE_SEPARATOR)[0];
    }

    public static String getAuthorFromUrl(String url) {
        String pageTitle = getSunoPageTitle(url);
        return pageTitle.split(SUNO_PAGE_TITLE_SEPARATOR)[1];
    }

    private static String getSunoPageTitle(String url) {
        if (SunoUtils.isMp3Url(url)) {
            url = SunoUtils.convertSunoMp3UrlToSunoUrl(url);
        } else if (SunoUtils.isAudiopipeUrl(url)) {
            url = SunoUtils.convertSunoAudiopipeUrlToSunoUrl(url);
        }
        try {
            Document document = Jsoup.connect(url).get();
            return document.title();
        } catch (IOException e) {
            return StringsConst.UNKNOWN_SUNO_TITLE;
        }
    }
}
