package fr.fonkio.enums;

import java.util.*;

public enum InterractionIdEnum {
    PLAY("play", "p"),
    SKIP("skip", "s"),
    PLAY_SKIP("ps"),
    PAUSE("pause"),
    RESUME("resume"),
    SEEK("seek"),
    LEAVE("leave"),
    DISCONNECT("quit", "disconnect", "dc"),
    CLEAR("clear", "clean", "clr"),
    QUEUE("queue", "np"),
    HELP_ADMIN("helpadmin"),
    HELP("help"),
    BLACKLIST("blacklist"),
    BLACKLIST_SELECT_MENU("blacklistselectmenu"),
    WELCOME("welcome"),
    WELCOME_SELECT_MENU("welcomeselectmenu"),
    GOODBYE("goodbye"),
    GOODBYE_SELECT_MENU("goodbyeselectmenu"),
    MOVE_ALL("moveall", "mva"),
    SHUFFLE("shuffle"),
    DEFAULT_ROLE("defaultrole"),
    DEFAULT_ROLE_SELECT_MENU("defaultroleselectmenu"),
    DISCONNECT_SONG("disconnectsong", "dcsong"),
    DISCONNECT_SONG_BUTTON("disconnectsongbutton"),
    PLAYLIST("playlist"),
    PLAYLIST_SELECT_MENU("playlistselectmenu"),
    PLAYLIST_REMOVE("playlistremove"),
    PLAYLIST_REMOVE_SELECT_MENU("playlistremoveselectmenu"),
    PLAYLIST_ADD("playlistadd"),
    PLAYLIST_ADD_MODAL("playlistaddmodal"),;

    private final List<String> ids;

    InterractionIdEnum(String... ids) {
        this.ids = new ArrayList<>(Arrays.asList(ids));
    }

    public static InterractionIdEnum getInterractionIdEnum(String id) {
        for (InterractionIdEnum interractionIdEnum : InterractionIdEnum.values()) {
            if (interractionIdEnum.ids.contains(id)) {
                return interractionIdEnum;
            }
        }
        throw new IllegalArgumentException("Pas d'interraction avec l'ID: " + id);
    }

    public String getMainId() {
        return ids.get(0);
    }
}
