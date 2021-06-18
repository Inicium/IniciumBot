package fr.fonkio.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.fonkio.inicium.Utils;
import fr.fonkio.message.MusicPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerMessage {
    private Message messageEnCours;
    private User author;
    private String command;
    private String message;
    private boolean afficherQueue;
    private MusicPlayer musicPlayer;
    private Timer timer = new Timer("timerUpdate");
    private TimerTask timerTask;

    private static final long DELAY  = 5000L;
    private static final long PERIOD = 5000L;

    public PlayerMessage(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public void newMessage(String command, String message, User author, boolean afficherQueue, TextChannel textChannel) {
        if (messageEnCours != null) {
            messageEnCours.editMessage(messageEnCours).setActionRow(Button.success("done", "Terminé !").asDisabled()).queue();
        }
        if(timerTask != null) {
            timerTask.cancel();
        }

        this.command = command;
        this.message = message;
        this.afficherQueue = afficherQueue;
        this.author = author;
        MessageAction ma = textChannel.sendMessage(getEmbed());
        this.messageEnCours = addButtons(ma).complete();
        timerTask = new PlayerUpdater();
        timer.scheduleAtFixedRate(timerTask, DELAY, PERIOD);
    }

    private MessageEmbed getEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Bot musique", null, "https://i.pinimg.com/originals/79/ab/9f/79ab9f804b5ebbdd514af3329cad6e0c.gif?size=256");
        builder.setTitle(this.command);
        builder.setDescription(this.message);
        builder.setColor(Color.GREEN);
        builder.setFooter(this.author.getName(), this.author.getAvatarUrl());

        if (this.afficherQueue) {
            List <AudioTrack> queue = this.musicPlayer.getQueue();
            addFields(builder, queue);
        }
        return builder.build();
    }

    private void addFields(EmbedBuilder builder, List<AudioTrack> queue) {
        if (queue.size()==0) { //Pas de musique dans la file
            builder.addField("Musique", "Aucune musique n'est en cours de lecture", false);
        } else if (queue.size()==1) { //1 musique dans la file
            AudioTrack np = queue.get(0);
            if (np == null) { //Si elle est nulle
                builder.addField("Musique", "Aucune musique n'est en cours de lecture", false);
            } else { //Si elle n'est pas nulle
                if (np.getInfo().uri.contains("youtube.com")) {
                    builder.setThumbnail("http://i3.ytimg.com/vi/"+np.getInfo().uri.split("v=")[1].split("&")[0]+"/maxresdefault.jpg");
                }
                String duration;
                if(np.getInfo().isStream) {
                    duration = "STREAM";
                } else {
                    duration = Utils.convertLongToString(np.getDuration());
                }
                barGenerator(builder, np, duration);
                builder.addField("File d'attente", "Aucune musique n'est dans la file d'attente ...", false);
            }

        } else {
            AudioTrack np = queue.get(0);
            String duration;
            if(np.getInfo().isStream) {
                duration = "STREAM";
            } else {
                duration = Utils.convertLongToString(np.getInfo().length);
            }
            barGenerator(builder, np, duration);
            queue.remove(0);
            int i = 0;
            for(AudioTrack at : queue) {
                i++;
                if(at.getInfo().isStream) {
                    duration = "STREAM";
                } else {
                    duration = Utils.convertLongToString(at.getDuration());
                }
                builder.addField("``["+i+"]`` "+at.getInfo().title, "**Durée :** ``"+duration + "``\n**Auteur :** ``"+at.getInfo().author+"``", false);
            }
            if (np.getInfo().uri.contains("youtube.com")) {
                builder.setThumbnail("http://i3.ytimg.com/vi/"+np.getInfo().uri.split("v=")[1].split("&")[0]+"/maxresdefault.jpg");
            }
        }
    }

    public MessageAction addButtons(MessageAction message) {
        if (musicPlayer.getAudioPlayer().getPlayingTrack() == null) {
            message.setActionRow(Button.success("done", "Terminé !").asDisabled());
            return message;
        }
        List<Component> buttons = new ArrayList<>();

        if (musicPlayer.isPause()) {
            buttons.add(Button.success("resume", "▶ Play"));
            buttons.add(Button.secondary("pause", "⏸ Pause").asDisabled());
        } else {
            buttons.add(Button.secondary("resume", "▶ Play").asDisabled());
            buttons.add(Button.success("pause", "⏸ Pause"));
        }
        buttons.add(Button.primary("skip", "⏯ Skip"));
        Button button = Button.danger("clear", "\uD83D\uDDD1 Effacer la liste");
        if (musicPlayer.getQueue().size()<2) {
            button = button.asDisabled();
        }
        buttons.add(button);
        buttons.add(Button.danger("disconnect", "\uD83D\uDEAA Déconnecter"));

        message.setActionRow(buttons);
        return message;
    }

    public MessageAction addTrackEndButtons(MessageAction message) {
        List<Component> buttons = new ArrayList<>();
        buttons.add(Button.success("done", "Terminé !").asDisabled());
        buttons.add(Button.danger("disconnect", "\uD83D\uDEAA Déconnecter"));
        message.setActionRow(buttons);
        return message;
    }

    private void barGenerator(EmbedBuilder builder, AudioTrack np, String duration) {
        String position = Utils.convertLongToString(np.getPosition());
        builder.addField("``[En cours]`` "+np.getInfo().title, "**Durée :** ``"+position+" / "+duration + "``\n**Auteur :** ``"+np.getInfo().author+"``", false);
        Double posF;
        if (np.getInfo().isStream) {
            posF = 100D;
        } else {
            posF = ((double)np.getPosition()/np.getInfo().length)*100;
        }
        builder.setImage("http://www.yarntomato.com/percentbarmaker/button.php?barPosition="+posF.intValue()+"&leftFill=%2300FF00");
    }

    private class PlayerUpdater extends TimerTask {

        @Override
        public void run() {
            if (musicPlayer.isPause()) {
                cancel();
            } else if (musicPlayer.getAudioPlayer().getPlayingTrack() == null) {
                MessageAction ma = messageEnCours.editMessage(getEmbed());
                addTrackEndButtons(ma).queue();
                cancel();
            }
            else {
                if (musicPlayer.getQueue().size()==0) {
                    MessageAction ma = messageEnCours.editMessage(getEmbed());
                    addTrackEndButtons(ma).queue();
                    cancel();
                } else {
                    messageEnCours.editMessage(getEmbed()).queue();
                }
            }
        }
    }

}
