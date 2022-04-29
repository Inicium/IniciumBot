package fr.fonkio.music;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import fr.fonkio.inicium.Inicium;

import java.util.List;

public class YoutubeSearch {

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
        } catch (Exception e) {
            e.printStackTrace();
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
        System.out.println(input);
        try {
            List<SearchResult> result = youtube.search()
                    .list("id,snippet")
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Inicium.CONFIGURATION.getYtapi())
                    .execute()
                    .getItems();
            if (!result.isEmpty()) {
                String videoId = result.get(0).getId().getVideoId();
                return "https://www.youtube.com/watch?v="+videoId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
