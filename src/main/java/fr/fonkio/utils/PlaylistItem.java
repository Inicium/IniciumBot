package fr.fonkio.utils;

public class PlaylistItem {

    private final String url;
    private final String label;

    public PlaylistItem(String url, String label) {
        this.url = url;
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public String getLabel() {
        return label;
    }

}
