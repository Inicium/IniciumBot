package fr.fonkio.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.fonkio.inicium.Utils;
import fr.fonkio.message.MusicPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerMessage {
    private InteractionHook messageEnCours;
    private User author;
    private String command;
    private String message;
    private boolean afficherQueue;
    private final MusicPlayer musicPlayer;
    private final Timer timer = new Timer("timerUpdate");
    private TimerTask timerTask;

    private static final long DELAY  = 5000L;
    private static final long PERIOD = 5000L;
    private MessageEmbed oldEmbed;

    public PlayerMessage(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public void newMessage(String command, String message, User author, boolean afficherQueue, GenericInteractionCreateEvent event) {
        if (messageEnCours != null) { //Suppression des commandes du dernier message
            messageEnCours.editOriginalEmbeds(oldEmbed).setActionRow(Button.success("done", "Terminé !").asDisabled()).queue();
        }
        if(timerTask != null) {
            try {
                timerTask.cancel();
            } catch (IllegalStateException e) {
                System.err.println("Le timer est déjà arrêté");
            }

        }

        this.command = command;
        this.message = message;
        this.afficherQueue = afficherQueue;
        this.author = author;

        if (event instanceof SlashCommandInteractionEvent) {
            this.messageEnCours = ((SlashCommandInteractionEvent)event).replyEmbeds(getEmbed()).addActionRow(addButtons()).complete();
        } else if (event instanceof ButtonInteractionEvent) {
            if (messageEnCours != null) {
                this.messageEnCours.editOriginalEmbeds(getEmbed()).setActionRow(addButtons()).queue();
            }

        }
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
        this.oldEmbed = builder.build();
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
            int i;
            //Limite de 25 Fields
            // 23 + 1 en cours + 1 message à ajouter
            for(i = 0; i < queue.size() && i < 23; i++) {
                AudioTrack at = queue.get(i);
                if(at.getInfo().isStream) {
                    duration = "STREAM";
                } else {
                    duration = Utils.convertLongToString(at.getDuration());
                }
                builder.addField("``["+(i+2)+"]`` "+at.getInfo().title, "**Durée :** ``"+duration + "``\n**Auteur :** ``"+at.getInfo().author+"``", false);
            }
            if (i == 23) {
                int trackLeft = queue.size()-23;
                if (trackLeft > 1) {
                    builder.addField("+" + trackLeft + " autres pistes", "", false);
                } else if (trackLeft == 1){
                    builder.addField("+" + trackLeft + " autre piste", "", false);
                }

            }
            if (np.getInfo().uri.contains("youtube.com")) {
                builder.setThumbnail("http://i3.ytimg.com/vi/"+np.getInfo().uri.split("v=")[1].split("&")[0]+"/maxresdefault.jpg");
            }
        }
    }

    public List<ItemComponent> addButtons() {
        List<ItemComponent> itemComponentList = new ArrayList<>();
        if (musicPlayer.getAudioPlayer().getPlayingTrack() == null) {
            itemComponentList.add(Button.success("done", "Terminé !").asDisabled());
            return itemComponentList;
        }

        if (musicPlayer.isPause()) {
            itemComponentList.add(Button.success("resume", "▶ Play"));
            itemComponentList.add(Button.secondary("pause", "⏸ Pause").asDisabled());
        } else {
            itemComponentList.add(Button.secondary("resume", "▶ Play").asDisabled());
            itemComponentList.add(Button.success("pause", "⏸ Pause"));
        }
        itemComponentList.add(Button.primary("skip", "⏯ Skip"));
        Button button = Button.danger("clear", "\uD83D\uDDD1 Effacer la liste");
        if (musicPlayer.getQueue().size()<2) {
            button = button.asDisabled();
        }
        itemComponentList.add(button);
        itemComponentList.add(Button.danger("disconnect", "\uD83D\uDEAA Déconnecter"));
        return itemComponentList;
    }

    public List<ItemComponent> addTrackEndButtons() {
        List<ItemComponent> buttons = new ArrayList<>();
        buttons.add(Button.success("done", "Terminé !").asDisabled());
        buttons.add(Button.danger("disconnect", "\uD83D\uDEAA Déconnecter"));
        return buttons;
    }

    private void barGenerator(EmbedBuilder builder, AudioTrack np, String duration) {
        String position = Utils.convertLongToString(np.getPosition());
        builder.addField("``[En cours]`` "+np.getInfo().title, "**Durée :** ``"+position+" / "+duration + "``\n**Auteur :** ``"+np.getInfo().author+"``", false);
        double posF;
        if (np.getInfo().isStream) {
            posF = 100D;
        } else {
            posF = ((double)np.getPosition()/np.getInfo().length)*100;
        }
        builder.setImage("http://www.yarntomato.com/percentbarmaker/button.php?barPosition="+ (int) posF +"&leftFill=%2300FF00");
    }

    private class PlayerUpdater extends TimerTask {
        @Override
        public void run() {
            if (musicPlayer.isPause()) {
                cancel();
            } else if (musicPlayer.getAudioPlayer().getPlayingTrack() == null) {
                messageEnCours.editOriginalEmbeds(getEmbed()).setActionRow(addTrackEndButtons()).queue();
                cancel();
            }
            else {
                if (musicPlayer.getQueue().size()==0) {
                    messageEnCours.editOriginalEmbeds(getEmbed()).setActionRow(addTrackEndButtons()).queue();
                    cancel();
                } else {
                    messageEnCours.editOriginalEmbeds(getEmbed()).queue();
                }
            }
        }
    }

}
