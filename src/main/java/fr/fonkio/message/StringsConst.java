package fr.fonkio.message;

public class StringsConst {

    public static final String COMMAND_PLAY_TITLE = "▶ Play";
    public static final String COMMAND_PLAY_DESC = "Jouer une piste sur le channel vocal (YouTube, Twitch, SoundCloud, ...)";
    public static final String COMMAND_PLAY_PARAM = "Lien/Recherche de la musique";

    public static final String COMMAND_BLACKLIST_TITLE = "⛔ Blacklist";
    public static final String COMMAND_BLACKLIST_SUCCESS = "Sélectionner les channels dans lesquels les commandes sont interdites";
    public static final String COMMAND_BLACKLIST_DESC = "Définir les channels où l'on ne peut pas executer de commande";

    public static final String COMMAND_CLEAR_TITLE = "\uD83D\uDDD1 Clear";
    public static final String COMMAND_CLEAR_ALREADY_EMPTY = "La liste est déjà vide.";
    public static final String COMMAND_CLEAR_SUCCESS = "La liste a été effacée !";
    public static final String COMMAND_CLEAR_DESC = "Effacer la liste des musiques en attente";

    public static final String COMMAND_DISCONNECT_TITLE = "\uD83D\uDEAA Disconnect";
    public static final String COMMAND_DISCONNECT_SUCCESS = "Aurevoir 👋";
    public static final String COMMAND_DISCONNECT_DESC = "Déconnecter le bot du channel audio";
    
    public static final String COMMAND_GOODBYE_TITLE = "\uD83D\uDC4B Goodbye channel";
    public static final String COMMAND_GOODBYE_SUCCESS = "Sélectionner le channel dans lequel les messages de leave seront postés";

    public static final String COMMAND_HELP_TITLE = "Liste des commandes";
    public static final String COMMAND_HELP_DESC = "Voir la liste des commandes du bot musique";

    public static final String COMMAND_HELPADMIN_TITLE = "Liste des commandes administrateur";
    public static final String COMMAND_HELPADMIN_DESC = "Voir la liste des commandes administrateur";

    public static final String COMMAND_WELCOME_TITLE = "\uD83D\uDC4B Welcome channel";
    public static final String COMMAND_WELCOME_DESC = "Définir un channel pour l'affichage d'un message de bienvenue";
    public static final String COMMAND_WELCOME_SUCCESS = "Sélectionner le channel dans lequel les messages de bienvenue seront postés";

    public static final String COMMAND_GOODBYE_DESC = "Définir un channel pour l'affichage d'un message lors d'un départ";

    public static final String COMMAND_SKIP_TITLE = "⏭ Skip";
    public static final String COMMAND_SKIP_DESC = "Passer la lecture en cours pour jouer la musique suivante";
    public static final String COMMAND_SKIP_SUCCESS = "La piste viens d'être passée ⏭";

    public static final String COMMAND_PAUSE_TITLE = "⏸ Pause";
    public static final String COMMAND_PAUSE_DESC = "Mettre en pause la musique en cours de lecture";
    public static final String COMMAND_PAUSE_ALREADY_PAUSED = "Déjà en pause.";

    public static final String COMMAND_RESUME_TITLE = "▶ Resume";
    public static final String COMMAND_RESUME_DESC = "Relancer la musique mise en pause";
    public static final String COMMAND_RESUME_ALREDY_PLAYING = "Déjà en cours de lecture...";

    public static final String COMMAND_PLAYSKIP_TITLE = "▶ ⏭ PlaySkip";
    public static final String COMMAND_PLAYSKIP_DESC = "Lancer la lecture d'une musique avec un lien ou une recherche et passer la musique en cours";

    public static final String COMMAND_SEEK_TITLE = "\uD83D\uDD0E Seek";
    public static final String COMMAND_SEEK_DESC = "Avancer / Reculer le temps de lecture de la musique en cours";
    public static final String COMMAND_SEEK_PARAM = "Temps sous forme HH:MM:SS ou MM:SS";
    public static final String COMMAND_SEEK_SUCCESS = "La piste a été avancée à ";
    public static final String COMMAND_SEEK_INVALID_FORMAT = "Le temps entré n'est pas valide";

    public static final String COMMAND_QUEUE_TITLE = "ℹ Now playing";
    public static final String COMMAND_QUEUE_SUCCESS = "⏬⏬ Voici la liste ⏬⏬";
    public static final String COMMAND_QUEUE_DESC = "Voir la liste de lecture";

    public static final String COMMAND_SHUFFLE_TITLE = "\uD83D\uDD00 Shuffle";
    public static final String COMMAND_SHUFFLE_DESC = "Mélanger la liste de lecture";
    public static final String COMMAND_SHUFFLE_SUCCESS = "Mélange de la liste \uD83D\uDD00";

    public static final String COMMAND_MOVEALL_DESC = "Déplacer tous les utilisateurs du channel actuel";
    public static final String COMMAND_MOVEALL_SUCCESS = "J'ai déplacé tout le monde ! \uD83D\uDE80\u200B";
    public static final String COMMAND_MOVEALL_PARAM = "Channel de destination";
    public static final String COMMAND_MOVEALL_PERM = "Tu n'as pas les permissions nécessaires pour déplacer des membres ou pour accéder à ce channel";

    public static final String MESSAGE_NOT_CONNECTED = "Tu dois être connecté sur un channel vocal pour demander ça.";
    public static final String MESSAGE_UNKNOWN_CHANNEL = "Le channel est introuvable";
    public static final String MESSAGE_NO_MUSIC_IN_PROGRESS = "Aucune musique n'est en cours de lecture";
    public static final String MESSAGE_CANT_CONNECT = "Je n'ai pas la permission de rejoindre ce channel !";
    public static final String MESSAGE_NO_PERMISSIONS = "Je n'ai pas la permission de rejoindre ce channel ! \uD83D\uDEAB";
    public static final String MESSAGE_GOODBYE_TITLE = "Au revoir !";
    public static final String MESSAGE_GOODBYE = " a quitté le serveur ";
    public static final String MESSAGE_IMPOSSIBLE = "Impossible d'effectuer cette action";
    public static final String MESSAGE_BOT_BUSY = "Bot occupé";
    public static final String MESSAGE_BOT_IN_OTHER_CHANNEL = "Le bot est déjà connecté dans un autre channel !";
    public static final String MESSAGE_WELCOME_TITLE = "Bienvenue !";
    public static final String MESSAGE_WELCOME = " a rejoint le serveur ";
    public static final String SELECT_OPTION_DEFINE = "Définir ";
    public static final String MESSAGE_ADMIN_PERM = "Permissions manquantes";
    public static final String MESSAGE_NO_ADMIN_PERM = "Vous n'êtes pas administrateur de ce serveur";
    public static final String MESSAGE_CONFIRM_TITLE = "Choix validé !";
    public static final String MESSAGE_CONFIRM = "Votre choix est enregistré";
    public static final String BUTTON_SAVED = "Enregistré !";
    public static final String MESSAGE_BLACKLISTED_CHANNEL_TITLE = "Channel blacklisté";
    public static final String MESSAGE_BLACKLISTED = "Ce channel est dans la blacklist pour l'envoi de commande. Merci d'envoyer des commandes dans les channels prévus à cet effet";
    public static final String MESSAGE_ADDING_TRACK = "Ajout de la piste";
    public static final String MESSAGE_ADDING_PLAYLIST = "Ajout de la playlist ";
    public static final String MESSAGE_THE_TRACK = "La piste ";
    public static final String MESSAGE_NOT_FOUND = " n'a pas été trouvée.";
    public static final String MESSAGE_CANT_PLAY_RESON = "Impossible de jouer la piste (raison:";
    public static final String MESSAGE_ADD_LYRIC_TO_SEARCH = "SI C'EST UNE MUSIQUE ESSAYE D'AJOUTER \"LYRICS\" A TA RECHERCHE";
    public static final String MESSAGE_LOADING_TITLE = "Chargement";
    public static final String MESSAGE_LOADING = "Chargement...";
    public static final String BUTTON_DONE = "Terminé !";
    public static final String MESSAGE_MUSIC_BOT = "Bot musique";
    public static final String MESSAGE_MUSIC = "Musique";
    public static final String MESSAGE_WAITLIST = "File d'attente";
    public static final String MESSAGE_NO_MUSIC_IN_WAITLIST = "Aucune musique n'est dans la file d'attente ...";
    public static final String MESSAGE_DURATION = "Durée :";
    public static final String MESSAGE_AUTHOR = "Auteur :";
    public static final String MESSAGE_OTHER_TRACKS = " autres pistes";
    public static final String MESSAGE_OTHER_TRACK = " autre piste";
}
