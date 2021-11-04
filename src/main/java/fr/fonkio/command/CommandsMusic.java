package fr.fonkio.command;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.music.MusicManager;
import fr.fonkio.message.MusicPlayer;
import fr.fonkio.music.YoutubeSearch;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import java.util.Timer;
import java.util.TimerTask;

public class CommandsMusic {

    private final YoutubeSearch youtubeSearch;
    private final MusicManager manager;

    public CommandsMusic() {
        this.manager = new MusicManager();
        this.youtubeSearch = new YoutubeSearch();
    }

    public void queueExec(User user, Guild guild, TextChannel textChannel) {
        if(!guild.getAudioManager().isConnected()) {
            manager.getPlayer(guild).getPlayerMessage().newMessage("Now playing","Il n'y a pas de musique en cours ...", user, false, textChannel);
            return;
        }
        manager.getPlayer(guild).getPlayerMessage().newMessage("Now playing","⏬⏬ Voici la liste ⏬⏬", user, true, textChannel);
    }

    public void pauseExec (User user, Guild guild, TextChannel textChannel) {
        if(!guild.getAudioManager().isConnected()) {
            manager.getPlayer(guild).getPlayerMessage().newMessage("Pause","Il n'y a pas de musique en cours ...", user, false, textChannel);
            return;
        }
        if(manager.getPlayer(guild).isPause()) {
            manager.getPlayer(guild).getPlayerMessage().newMessage("Pause","Déjà en pause...", user, false, textChannel);
            return;
        }
        manager.getPlayer(guild).setPause(true);
        manager.getPlayer(guild).getPlayerMessage().newMessage("Pause","⏸", user, false, textChannel);
    }

    public void resumeExec (User user, Guild guild, TextChannel textChannel) {
        if(!guild.getAudioManager().isConnected()) {
            manager.getPlayer(guild).getPlayerMessage().newMessage("Resume","Il n'y a pas de musique en cours ...", user, false, textChannel);
            return;
        }
        if(manager.getPlayer(guild).isPause()) {
            manager.getPlayer(guild).setPause(false);
            manager.getPlayer(guild).getPlayerMessage().newMessage("Resume","▶", user, false, textChannel);
            return;
        }
        manager.getPlayer(guild).getPlayerMessage().newMessage("Resume","Déjà en cours de lecture...", user, false, textChannel);
    }

    public void seekExec(User user, Guild guild, TextChannel textChannel, String time) {
        if(!guild.getAudioManager().isConnected()) {
            manager.getPlayer(guild).getPlayerMessage().newMessage("Seek","Il n'y a pas de musique en cours ...", user, false, textChannel);
            return;
        }
        try {
            manager.getPlayer(guild).seekTrack(time);
        } catch (IllegalArgumentException e) {
            manager.getPlayer(guild).getPlayerMessage().newMessage("Seek","Le temps entré n'est pas valide", user, false, textChannel);
            return;
        }
        manager.getPlayer(guild).getPlayerMessage().newMessage("Seek","La piste a été avancée à "+time, user, true, textChannel);
    }

    public void playExec(Guild guild, TextChannel textChannel, User user, String searchOrURL) {
        if (guild == null) {
            return;
        }
        VoiceChannel voiceChannel = guild.getMember(user).getVoiceState().getChannel();
        if (!guild.getAudioManager().isConnected()) {

            if (voiceChannel == null) {
                manager.getPlayer(guild).getPlayerMessage().newMessage("Play","Tu dois être co sur un channel vocal pour demander ça.", user, false, textChannel);
                return;
            }
            try {
                guild.getAudioManager().openAudioConnection(voiceChannel);
                guild.getAudioManager().setSelfDeafened(true);
            } catch (InsufficientPermissionException e){
                manager.getPlayer(guild).getPlayerMessage().newMessage("Play","Je n'ai pas la permission de rejoindre ce channel !", user, false, textChannel);
            }

        } else {
            if (voiceChannel == null) {
                manager.getPlayer(guild).getPlayerMessage().newMessage("Play","Tu dois être co sur un channel vocal pour demander ça.", user, false, textChannel);
                return;
            }
            // Verification que l'utilisateur soit dans le même chan
            if (!voiceChannel.getId().equals(guild.getMember(Inicium.getJda().getSelfUser()).getVoiceState().getChannel().getId())) {
                try {
                    guild.getAudioManager().openAudioConnection(voiceChannel);
                } catch (InsufficientPermissionException e) {
                    textChannel.sendMessage("Je n'ai pas la permission de rejoindre ce channel !");
                }
            }
        }
        manager.loadTrack(textChannel, youtubeSearch.searchOrUrl(searchOrURL), user);
    }

    public void skipExec(User user, Guild guild, TextChannel textChannel) {
        if(!guild.getAudioManager().isConnected()) {
            manager.getPlayer(guild).getPlayerMessage().newMessage("Skip","Il n'y a pas de musique en cours ...", user, false, textChannel);
            return;
        }
        manager.getPlayer(guild).skipTrack();
        manager.getPlayer(guild).getPlayerMessage().newMessage("Skip","La piste viens d'être passée...", user, true, textChannel);
    }

    public void disconnectQuiet(Guild guild) {
        guild.getAudioManager().closeAudioConnection();
        MusicPlayer player = manager.getPlayer(guild);
        player.getListener().getTracks().clear();
        player.getAudioPlayer().stopTrack();
    }

    public void disconnectExec(User user, Guild guild, TextChannel textChannel) {
        if(!guild.getAudioManager().isConnected()) {
            return;
        }
        MusicPlayer player = manager.getPlayer(guild);
        player.getListener().getTracks().clear();
        player.getAudioPlayer().stopTrack();
        if(guild.getId().equals("296520788033404929")) {
            manager.loadTrack(textChannel, Inicium.CONFIGURATION.getDCsong(), user, false);
            manager.getPlayer(guild).getPlayerMessage().newMessage("Disconnect","Aurevoir 👋", user, false, textChannel);
            TimerTask task = new TimerTask() {
                public void run() {
                    guild.getAudioManager().closeAudioConnection();
                    player.getListener().getTracks().clear();
                    manager.getPlayer(guild).getAudioPlayer().stopTrack();
                }
            };
            Timer timer = new Timer("Timer");

            long delay = 3000L;
            timer.schedule(task, delay);
        } else {
            guild.getAudioManager().closeAudioConnection();
        }
    }

    public void clearExec(User user, TextChannel textChannel) {
        Guild guild = textChannel.getGuild();
        MusicPlayer player = manager.getPlayer(guild);

        if(player.getListener().getTracks().isEmpty()) {
            manager.getPlayer(guild).getPlayerMessage().newMessage("Clear","La liste est déjà vide ...", user, true, textChannel);
            return;
        }

        player.getListener().getTracks().clear();
        manager.getPlayer(guild).getPlayerMessage().newMessage("Clear","La liste a été effacée !", user, true, textChannel);
    }
}
