package fr.fonkio.utils;

public enum ConfigurationEnum {
    QUIT_CHANNEL("quit", ""),
    WELCOME_CHANNEL("welcome", ""),
    SERVER_NAME("name", "undefined"),
    DEFAULT_ROLE("defaultrole", ""),
    DC_SONG("dcsong", "true"),
    BLACK_LIST("blacklist", null);



    ConfigurationEnum(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    private final String key;
    private final String defaultValue;

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
