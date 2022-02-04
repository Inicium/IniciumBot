package fr.fonkio.utils;

public enum ConfigurationEnum {
    QUIT_CHANNEL("quit", ""),
    WELCOME_CHANNEL("welcome", ""),
    PREFIX_COMMAND("prefix", "!"),
    MOVE_AFK("moveAfk", "false"),
    SERVER_NAME("name", "undefined");

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
