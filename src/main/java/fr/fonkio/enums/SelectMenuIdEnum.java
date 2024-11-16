package fr.fonkio.enums;

public enum SelectMenuIdEnum {
    GOODBYE,
    WELCOME,
    BLACKLIST,
    DEFAULT_ROLE,
    PLAYLIST,
    PLAYLIST_REMOVE;

    public static SelectMenuIdEnum getSelectMenuIdEnum(String name) {
        for (SelectMenuIdEnum selectMenuIdEnum : SelectMenuIdEnum.values()) {
            if (selectMenuIdEnum.toString().equals(name)) {
                return selectMenuIdEnum;
            }
        }
        return null;
    }
}
