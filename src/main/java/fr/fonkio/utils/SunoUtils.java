package fr.fonkio.utils;

import fr.fonkio.message.StringsConst;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SunoUtils {

    private final static Logger logger = LoggerFactory.getLogger(SunoUtils.class);

    public static final String MP3_START_URL = "https://cdn1.suno.ai/";
    public static final String MP3_EXTENTION = ".mp3";

    public static final String AUDIOPIPE_START_URL = "https://audiopipe.suno.ai/?item_id=";

    public static final String SUNO_SONG_START_URL = "https://suno.com/song/";

    public static final String SUNO_PAGE_TITLE_SEPARATOR = " by ";

    private static Map<String, String> jsonScriptCache = new HashMap<>();

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
        return getPartStringWithSunoPageTitleSeparator(pageTitle, 0);
    }

    public static String getAuthorFromUrl(String url) {
        String pageTitle = getSunoPageTitle(url);
        return getPartStringWithSunoPageTitleSeparator(pageTitle, 1);
    }

    public static String getThumbnailUrlFromUrl(String url) {
        return getSunoPageThumbnailUrl(url);
    }

    public static String getStyleFromUrl(String url) {
        return getSunoPageStyle(url);
    }

    @NotNull
    private static String getPartStringWithSunoPageTitleSeparator(String pageTitle, int part) {
        String partString;
        try {
            partString = pageTitle.split(SUNO_PAGE_TITLE_SEPARATOR)[part];
        } catch (ArrayIndexOutOfBoundsException e) {
            SunoUtils.logger.debug("Le titre de contient pas le séparateur : \"{}\"", SUNO_PAGE_TITLE_SEPARATOR, e);
            return StringsConst.UNKNOWN_SUNO_TITLE;
        }
        if (StringUtils.isBlank(partString)) {
            partString = StringsConst.UNKNOWN_SUNO_TITLE;
        }
        return partString;
    }

    private static String getSunoPageThumbnailUrl(String url) {
        String scriptJson = getSunoPageScriptJson(url);
        int start = scriptJson.lastIndexOf("\"image_url\\\":\\\"");
        scriptJson = scriptJson.substring(start + 15);
        int end = scriptJson.indexOf(".jpeg");
        String pageThumbnail = scriptJson.substring(0, end + 5);
        if (start != -1 && end != -1) {
            SunoUtils.logger.debug("Page Thumbnail Url : \"{}\"", pageThumbnail);
            return pageThumbnail;
        } else {
            return StringUtils.EMPTY;
        }
    }

    private static String getSunoPageStyle(String url) {
        String scriptJson = getSunoPageScriptJson(url);
        int start = scriptJson.lastIndexOf("\"tags\\\":\\\"");
        scriptJson = scriptJson.substring(start + 10);
        int end = scriptJson.indexOf("\\\"");
        String pageTags = scriptJson.substring(0, end);
        return "**Style :** ``" + pageTags +
                "``\n";
    }

    private static String getSunoPageTitle(String url) {
        String scriptJson = getSunoPageScriptJson(url);

        int end = scriptJson.lastIndexOf(" | Suno");
        scriptJson = scriptJson.substring(0, end);
        int start = scriptJson.lastIndexOf("\"") + 1;
        String pageTitle = scriptJson.substring(start);
        SunoUtils.logger.debug("Page title : \"{}\"", pageTitle);
        return pageTitle;
    }

    private static String getSunoPageScriptJson(String url) {
        if (SunoUtils.isMp3Url(url)) {
            url = SunoUtils.convertSunoMp3UrlToSunoUrl(url);
        } else if (SunoUtils.isAudiopipeUrl(url)) {
            url = SunoUtils.convertSunoAudiopipeUrlToSunoUrl(url);
        }
        if (jsonScriptCache.containsKey(url)) {
            return jsonScriptCache.get(url);
        }
        SunoUtils.logger.debug("Récupération de la page : \"{}\"", url);
        try {
            Document document = Jsoup.connect(url).get();
            StringBuilder jsonStringBuilder = new StringBuilder();
            document.select("script").stream()
                    .filter(element -> element.html().contains("self.__next_f.push"))
                    .forEach(element ->
                            jsonStringBuilder.append(
                                    element.html()
                                            .replace("self.__next_f.push([1,\"", "")
                                            .replace("\"])", "")
                            )
                    );

            jsonScriptCache.put(url, jsonStringBuilder.toString());
            logger.debug("Contenu du json script :\n{}", jsonStringBuilder);
            return jsonStringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
