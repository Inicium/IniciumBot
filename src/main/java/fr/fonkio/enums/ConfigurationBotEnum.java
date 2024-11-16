package fr.fonkio.enums;

public enum ConfigurationBotEnum {
    DISCORD_TOKEN("token"),
    YOUTUBE_API("ytapi"),
    DISCONNECT_SONG("dcsong"),
    PO_TOKEN("potoken"),
    VISITOR_DATA("visitordata");

    private final String key;

    ConfigurationBotEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
