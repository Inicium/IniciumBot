package fr.fonkio.music;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.enums.ConfigurationBotEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class YoutubeSearch {

    private final Logger logger = LoggerFactory.getLogger(YoutubeSearch.class);
    private final YouTube youtube;

    public YoutubeSearch() {
        YouTube temp = null;
        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null)
                    .setApplicationName("API YouTube BOT")
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        this.youtube = temp;
    }

    public String searchOrUrl(String input) {
        if (input.startsWith("http")) {
            return input;
        } else {
            return searchYoutube(input);
        }
    }

    private String searchYoutube(String input) {
        logger.info("Recherche sur YouTube de \"{}\"...", input);
        try {
            List<SearchResult> result = youtube.search()
                    .list("id,snippet")
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Inicium.CONFIGURATION.getGlobalParam(ConfigurationBotEnum.YOUTUBE_API))
                    .execute()
                    .getItems();
            if (!result.isEmpty()) {
                String videoId = result.get(0).getId().getVideoId();
                String urlResult = "https://www.youtube.com/watch?v=" + videoId;
                logger.info("Résultat trouvé : {}", urlResult);
                return urlResult;
            }
        } catch (Exception e) {
            logger.error("Problème lors de la recherche YouTube", e);
        }
        return "";
    }

}
