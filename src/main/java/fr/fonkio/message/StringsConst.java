package fr.fonkio.message;

public class StringsConst {

    public static final String COMMAND_PLAY_EMOTE = "▶️";
    public static final String COMMAND_PLAY_TITLE = COMMAND_PLAY_EMOTE + " Play";
    public static final String COMMAND_PLAY_DESC = "Jouer une piste sur le channel vocal (YouTube, Twitch, SoundCloud, ...)";
    public static final String COMMAND_PLAY_PARAM = "Recherche ou URL";

    public static final String COMMAND_BLACKLIST_EMOTE = "⛔";
    public static final String COMMAND_BLACKLIST_TITLE = COMMAND_BLACKLIST_EMOTE + " Blacklist";
    public static final String COMMAND_BLACKLIST_SUCCESS = "Sélectionner les channels dans lesquels les commandes sont interdites";
    public static final String COMMAND_BLACKLIST_DESC = "Définir les channels où l'on ne peut pas executer de commande";
    public static final String SELECT_BLACKLIST_SUCCESS = "La blacklist a été mise à jour";

    public static final String COMMAND_CLEAR_EMOTE = "🗑️";
    public static final String COMMAND_CLEAR_TITLE = COMMAND_CLEAR_EMOTE + " Clear";
    public static final String COMMAND_CLEAR_ALREADY_EMPTY = "La liste est déjà vide.";
    public static final String COMMAND_CLEAR_SUCCESS = "La liste a été effacée !";
    public static final String COMMAND_CLEAR_DESC = "Effacer la liste des musiques en attente";

    public static final String COMMAND_DISCONNECT_EMOTE = "🚪";
    public static final String COMMAND_DISCONNECT_TITLE = COMMAND_DISCONNECT_EMOTE + " Disconnect";
    public static final String COMMAND_DISCONNECT_SUCCESS = "Aurevoir 👋";
    public static final String COMMAND_DISCONNECT_DESC = "Déconnecter le bot du channel audio";
    public static final String COMMAND_DISCONNECT_ALREADY_DISCONNECTED = "Je suis déjà déconnecté.";

    public static final String COMMAND_GOODBYE_EMOTE = "👋";
    public static final String COMMAND_GOODBYE_TITLE = COMMAND_GOODBYE_EMOTE + " Goodbye channel";
    public static final String COMMAND_GOODBYE_DESC = "Définir un channel pour l'affichage d'un message lors d'un départ";
    public static final String COMMAND_GOODBYE_SUCCESS = "Sélectionner le channel dans lequel les messages de leave seront postés";
    public static final String SELECT_GOODBYE_SUCCESS = "Le channel a été modifié";

    public static final String COMMAND_HELP_TITLE = "Liste des commandes";
    public static final String COMMAND_HELP_DESC = "Voir la liste des commandes du bot musique";

    public static final String COMMAND_HELPADMIN_TITLE = "Liste des commandes administrateur";
    public static final String COMMAND_HELPADMIN_DESC = "Voir la liste des commandes administrateur";

    public static final String COMMAND_WELCOME_EMOTE = "👋";
    public static final String COMMAND_WELCOME_TITLE = COMMAND_WELCOME_EMOTE + " Welcome channel";
    public static final String COMMAND_WELCOME_DESC = "Définir un channel pour l'affichage d'un message de bienvenue";
    public static final String COMMAND_WELCOME_SUCCESS = "Sélectionner le channel dans lequel les messages de bienvenue seront postés";
    public static final String SELECT_WELCOME_SUCCESS = "Le channel a été modifié";

    public static final String COMMAND_DISCONNECT_SONG_EMOTE = "🚪";
    public static final String COMMAND_DISCONNECT_SONG_TITLE = COMMAND_DISCONNECT_SONG_EMOTE + " Son de déconnexion";
    public static final String COMMAND_DISCONNECT_SONG_DESC = "Activer ou désactiver le son de déconnexion du bot musique";
    public static final String COMMAND_DISCONNECT_SONG_SUCCESS = "Voulez-vous que le bot vous dise au revoir quand il part ? (La politesse c'est important)\n Actuellement la fonctionnalité est %s";
    public static final String BUTTON_DISCONNECT_SONG_SUCCESS = "Le son de déconnexion a été %s";

    public static final String COMMAND_DEFAULT_ROLE_EMOTE = "👤";
    public static final String COMMAND_DEFAULT_ROLE_TITLE = COMMAND_DEFAULT_ROLE_EMOTE + " Default role";
    public static final String COMMAND_DEFAULT_ROLE_DESC = "Définir le rôle par défaut attribué aux nouveaux arrivant";
    public static final String COMMAND_DEFAULT_ROLE_SUCCESS = "Sélectionner rôle attribué par défaut";
    public static final String SELECT_DEFAULT_ROLE_SUCCESS = "Le rôle attribué par défaut a été modifié";

    public static final String COMMAND_SKIP_EMOTE = "⏭️";
    public static final String COMMAND_SKIP_TITLE = COMMAND_SKIP_EMOTE + " Skip";
    public static final String COMMAND_SKIP_DESC = "Passer la lecture en cours pour jouer la musique suivante";
    public static final String COMMAND_SKIP_SUCCESS = "La piste viens d'être passée " + COMMAND_SKIP_EMOTE;

    public static final String COMMAND_PAUSE_EMOTE = "⏸️";
    public static final String COMMAND_PAUSE_TITLE = COMMAND_PAUSE_EMOTE + " Pause";
    public static final String COMMAND_PAUSE_DESC = "Mettre en pause la musique en cours de lecture";
    public static final String COMMAND_PAUSE_ALREADY_PAUSED = "Déjà en pause.";

    public static final String COMMAND_RESUME_EMOTE = "▶️";
    public static final String COMMAND_RESUME_TITLE = COMMAND_RESUME_EMOTE + " Resume";
    public static final String COMMAND_RESUME_DESC = "Relancer la musique mise en pause";
    public static final String COMMAND_RESUME_ALREDY_PLAYING = "Déjà en cours de lecture...";

    public static final String COMMAND_PLAYSKIP_EMOTE = "▶️⏭️️";
    public static final String COMMAND_PLAYSKIP_DESC = "Lancer la lecture d'une musique avec un lien ou une recherche et passer la musique en cours";

    public static final String COMMAND_SEEK_EMOTE = "⏭️";
    public static final String COMMAND_SEEK_TITLE = COMMAND_SEEK_EMOTE + " Seek";
    public static final String COMMAND_SEEK_DESC = "Avancer / Reculer le temps de lecture de la musique en cours";
    public static final String COMMAND_SEEK_PARAM = "Temps sous forme HH:MM:SS ou MM:SS";
    public static final String COMMAND_SEEK_SUCCESS = "La piste a été avancée à ";
    public static final String COMMAND_SEEK_INVALID_FORMAT = "Le temps entré n'est pas valide";

    public static final String COMMAND_QUEUE_EMOTE = "ℹ️";
    public static final String COMMAND_QUEUE_TITLE = COMMAND_QUEUE_EMOTE + " Now playing";
    public static final String COMMAND_QUEUE_SUCCESS = "⏬⏬ Voici la liste ⏬⏬";
    public static final String COMMAND_QUEUE_DESC = "Voir la liste de lecture";

    public static final String COMMAND_SHUFFLE_EMOTE = "🔀";
    public static final String COMMAND_SHUFFLE_TITLE = COMMAND_SHUFFLE_EMOTE + " Shuffle";
    public static final String COMMAND_SHUFFLE_DESC = "Mélanger la liste de lecture";
    public static final String COMMAND_SHUFFLE_SUCCESS = "Mélange de la liste " + COMMAND_SHUFFLE_EMOTE;

    public static final String COMMAND_MOVEALL_EMOTE = "🚀";
    public static final String COMMAND_MOVEALL_TITLE = COMMAND_MOVEALL_EMOTE + " MoveAll";
    public static final String COMMAND_MOVEALL_DESC = "Déplacer tous les utilisateurs du channel actuel";
    public static final String COMMAND_MOVEALL_SUCCESS = "J'ai déplacé tout le monde ! " + COMMAND_MOVEALL_EMOTE;
    public static final String COMMAND_MOVEALL_PARAM = "Channel de destination";
    public static final String COMMAND_MOVEALL_PERM = "Tu n'as pas les permissions nécessaires pour déplacer des membres ou pour accéder à ce channel";

    public static final String COMMAND_PLAYLIST_EMOTE = "⭐";
    public static final String COMMAND_PLAYLIST_TITLE = COMMAND_PLAYLIST_EMOTE + " Playlist";
    public static final String COMMAND_PLAYLIST_DESC = "Gérer la playlist";
    public static final String COMMAND_PLAYLIST_SUCCESS = "Sélectionner une musique de la playlist pour l'ajouter à la liste de lecture";
    public static final String COMMAND_PLAYLIST_REMOVE_EMOTE = "❌";
    public static final String COMMAND_PLAYLIST_REMOVE_TITLE = COMMAND_PLAYLIST_REMOVE_EMOTE + " Playlist remove";
    public static final String COMMAND_PLAYLIST_REMOVE_SUCCESS = "Sélectionner les musiques à supprimer de la playlist";
    public static final String SELECT_PLAYLIST_REMOVE_SUCCESS = "Suppression de(s) musique(s) OK";
    public static final String COMMAND_PLAYLIST_EMPTY_ERROR = "Il n'y a pas encore de musique dans la playlist";
    public static final String COMMAND_PLAYLIST_ADD_FULL = "La playlist est pleine";
    public static final String COMMAND_PLAYLIST_ADD_ALREADY_EXIST = "L'URL est déjà enregistrée dans la playlist";
    public static final String MODAL_PLAYLIST_ADD_SUCCESS = "%s a bien été ajouté à la playlist !";
    public static final String COMMAND_PLAYLIST_ADD_MODAL_TITLE = "Ajouter à la playlist";
    public static final String COMMAND_PLAYLIST_ADD_MODAL_LABEL = "Nom de la musique";
    public static final String COMMAND_PLAYLIST_ADD_MODAL_LABEL_PLACEHOLDER = "Ma musique";
    public static final String COMMAND_PLAYLIST_ADD_MODAL_URL = "Lien de la musique";
    public static final String COMMAND_PLAYLIST_ADD_MODAL_URL_PLACEHOLDER = "https://www.youtube.com/watch?v=xxxxxxxxxxx";

    public static final String MESSAGE_NOT_CONNECTED = "Tu dois être connecté sur un channel vocal pour demander ça.";
    public static final String MESSAGE_UNKNOWN_CHANNEL = "Le channel est introuvable";
    public static final String MESSAGE_NO_MUSIC_IN_PROGRESS = "Aucune musique n'est en cours de lecture";
    public static final String MESSAGE_CANT_CONNECT = "Je n'ai pas la permission de rejoindre ce channel ! 🚫";
    public static final String MESSAGE_NO_PERMISSIONS = "Je n'ai pas la permission de rejoindre ce channel ! 🚫";
    public static final String MESSAGE_GOODBYE_TITLE = "Au revoir !";
    public static final String MESSAGE_GOODBYE = " a quitté le serveur :";
    public static final String MESSAGE_IMPOSSIBLE = "Impossible d'effectuer cette action";
    public static final String MESSAGE_BOT_BUSY = "Bot occupé";
    public static final String MESSAGE_BOT_IN_OTHER_CHANNEL = "Le bot est déjà connecté dans un autre channel !";
    public static final String MESSAGE_WELCOME_TITLE = "Bienvenue !";
    public static final String MESSAGE_WELCOME = " a rejoint le serveur ";
    public static final String SELECT_OPTION_DEFINE = "Définir ";
    public static final String MESSAGE_ADMIN_PERM = "Permissions manquantes";
    public static final String MESSAGE_NO_ADMIN_PERM = "Vous n'êtes pas administrateur de ce serveur";
    public static final String BUTTON_SAVED = "Enregistré !";
    public static final String MESSAGE_BLACKLISTED_CHANNEL_TITLE = "Channel blacklisté";
    public static final String MESSAGE_BLACKLISTED = "Ce channel est dans la blacklist pour l'envoi de commande. Merci d'envoyer des commandes dans les channels prévus à cet effet";
    public static final String MESSAGE_ADDING_TRACK = "Ajout de la piste";
    public static final String MESSAGE_ADDING_PLAYLIST = "Ajout de la playlist ";
    public static final String MESSAGE_THE_TRACK = "La piste ";
    public static final String MESSAGE_NOT_FOUND = " n'a pas été trouvée.";
    public static final String MESSAGE_CANT_PLAY_RESON = "Impossible de jouer la piste, merci de prévenir <@287268866147483649> (Message d'erreur : ";
    public static final String MESSAGE_SUNO_FAILED = "Le lien suno n'est pas valide. Pour information, les musiques en cours de génération ne peuvent pas être lues, merci d'attendre la fin du chargement pour réessayer.";

    public static final String MESSAGE_LOADING_TITLE = "Chargement";
    public static final String MESSAGE_LOADING = "Chargement...";
    public static final String BUTTON_DONE = "✅ Terminé !";
    public static final String MESSAGE_MUSIC_BOT = "Inicium music";
    public static final String MESSAGE_MUSIC = "Musique";
    public static final String MESSAGE_WAITLIST = "File d'attente";
    public static final String MESSAGE_NO_MUSIC_IN_WAITLIST = "Aucune musique n'est dans la file d'attente ...";
    public static final String MESSAGE_DURATION = "Durée :";
    public static final String MESSAGE_AUTHOR = "Auteur :";
    public static final String MESSAGE_OTHER_TRACKS = " autres pistes";
    public static final String MESSAGE_OTHER_TRACK = " autre piste";
    public static final String MESSAGE_ERROR_TITLE = "Erreur";
    public static final String MESSAGE_ERROR = "Erreur lors de l'exécution de la commande. Merci de réessayer plus tard.";

}
