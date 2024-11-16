package fr.fonkio.message;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.fonkio.inicium.Utils;
import fr.fonkio.music.MusicPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class PlayerMessage {

    private final Logger logger = LoggerFactory.getLogger(PlayerMessage.class);
    private Message messageEnCours;
    private User author;
    private String title;
    private String message;
    private boolean afficherQueue;
    private final MusicPlayer musicPlayer;
    private IniciumTimer timer;
    private TimerTask timerTask;

    private static final long DELAY  = 5000L;
    private static final long PERIOD = 5000L;
    private MessageEmbed oldEmbed;

    public PlayerMessage(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public void updatePlayerMessage(String title, String message, User author, boolean afficherQueue, InteractionHook hook) {

        if (messageEnCours != null && !messageEnCours.equals(hook.retrieveOriginal().complete())) {
            logger.info("Changement du message d'affichage du lecteur... Suppression des boutons d'action de l'ancien message...");
            messageEnCours.editMessageEmbeds(oldEmbed).setActionRow(Button.success("done", StringsConst.BUTTON_DONE).asDisabled()).queue();
            if(timerTask != null) {
                logger.info("Arrêt de la task");
                timerTask.cancel();
            }
        }

        this.title = title;
        this.message = message;
        this.afficherQueue = afficherQueue;
        this.author = author;
        this.messageEnCours = hook.editOriginalEmbeds(getEmbed()).setComponents(addButtons()).complete();

        if (afficherQueue) {
            logger.info("Le nouveau message du lecteur doit afficher la queue");
            logger.info("Création de la tache");
            timerTask = new PlayerUpdater();
            if (timer == null || timer.isCancelled()) {
                logger.info("Le timer n'existe pas... Création du timer");
                timer = new IniciumTimer("timerUpdate");
            }
            timer.scheduleAtFixedRate(timerTask, DELAY, PERIOD);
            logger.info("Tâche programmée sur le timer");
        } else {
            logger.info("Le nouveau message du lecteur n'affiche pas la queue");
            if (timer != null && !timer.isCancelled()) {
                logger.info("Arrêt du timer");
                timer.cancel();
            }
        }
    }

    private MessageEmbed getEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(StringsConst.MESSAGE_MUSIC_BOT, null, "https://cdn.dribbble.com/users/2011679/screenshots/5816471/____2.gif");
        builder.setTitle(this.title);
        builder.setDescription(this.message);
        builder.setColor(Color.GREEN);
        builder.setFooter(this.author.getEffectiveName(), this.author.getAvatarUrl());

        if (this.afficherQueue) {
            List <AudioTrack> queue = this.musicPlayer.getQueue();
            addFields(builder, queue);
        }
        this.oldEmbed = builder.build();
        return builder.build();
    }

    private void addFields(EmbedBuilder builder, List<AudioTrack> queue) {
        if (queue.isEmpty()) { //Pas de musique dans la file
            builder.addField(StringsConst.MESSAGE_MUSIC, StringsConst.MESSAGE_NO_MUSIC_IN_PROGRESS, false);
        } else if (queue.size()==1) { //1 musique dans la file
            AudioTrack np = queue.get(0);
            if (np == null) { //Si elle est nulle
                builder.addField(StringsConst.MESSAGE_MUSIC, StringsConst.MESSAGE_NO_MUSIC_IN_PROGRESS, false);
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
                builder.addField(StringsConst.MESSAGE_WAITLIST, StringsConst.MESSAGE_NO_MUSIC_IN_WAITLIST, false);
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
                builder.addField(convertIntEmoji(i+2)+" "+at.getInfo().title,
                        "**" + StringsConst.MESSAGE_DURATION + "** ``"+duration +
                        "``\n**"+ StringsConst.MESSAGE_AUTHOR +"** ``"+at.getInfo().author+"``",
                        false);
            }
            if (i == 23) {
                int trackLeft = queue.size()-23;
                if (trackLeft > 1) {
                    builder.addField("+" + trackLeft + StringsConst.MESSAGE_OTHER_TRACKS, "", false);
                } else if (trackLeft == 1){
                    builder.addField("+" + trackLeft + StringsConst.MESSAGE_OTHER_TRACK, "", false);
                }

            }
            if (np.getInfo().uri.contains("youtube.com")) {
                builder.setThumbnail("http://i3.ytimg.com/vi/"+np.getInfo().uri.split("v=")[1].split("&")[0]+"/maxresdefault.jpg");
            }
        }
    }

    private String convertIntEmoji(int i) {
        return (i+"")
                .replaceAll("0", "0️⃣")
                .replaceAll("1", "1️⃣")
                .replaceAll("2", "2️⃣")
                .replaceAll("3", "3️⃣")
                .replaceAll("4", "4️⃣")
                .replaceAll("5", "5️⃣")
                .replaceAll("6", "6️⃣")
                .replaceAll("7", "7️⃣")
                .replaceAll("8", "8️⃣")
                .replaceAll("9", "9️⃣");
    }

    public List<ActionRow> addButtons() {
        List<ActionRow> actionRowList = new ArrayList<>();

        if (StringsConst.COMMAND_DISCONNECT_TITLE.equals(this.title) || musicPlayer.getAudioPlayer().getPlayingTrack() == null) {
            actionRowList.add(ActionRow.of(addTrackEndButtons()));
            return actionRowList;
        }

        List<ItemComponent> itemComponentLine1List = new ArrayList<>();
        List<ItemComponent> itemComponentLine2List = new ArrayList<>();
        if (musicPlayer.isPause()) {
            itemComponentLine1List.add(Button.success("resume", StringsConst.COMMAND_PLAY_TITLE));
        } else {
            itemComponentLine1List.add(Button.success("pause", StringsConst.COMMAND_PAUSE_TITLE));
        }
        itemComponentLine1List.add(Button.primary("skip", StringsConst.COMMAND_SKIP_TITLE));
        itemComponentLine1List.add(Button.primary("shuffle", StringsConst.COMMAND_SHUFFLE_TITLE));
        Button buttonClear = Button.danger("clear", StringsConst.COMMAND_CLEAR_TITLE);
        if (musicPlayer.getQueue().size()<2) {
            buttonClear = buttonClear.asDisabled();
        }
        itemComponentLine1List.add(Button.primary("playlist", StringsConst.COMMAND_PLAYLIST_TITLE));
        itemComponentLine2List.add(buttonClear);
        itemComponentLine2List.add(Button.danger("disconnect", StringsConst.COMMAND_DISCONNECT_TITLE));
        actionRowList.add(ActionRow.of(itemComponentLine1List));
        actionRowList.add(ActionRow.of(itemComponentLine2List));
        return actionRowList;
    }

    public List<ItemComponent> addTrackEndButtons() {
        List<ItemComponent> buttons = new ArrayList<>();
        buttons.add(Button.success("done", StringsConst.BUTTON_DONE).asDisabled());
        if (!StringsConst.COMMAND_DISCONNECT_TITLE.equals(this.title)) {
            buttons.add(Button.danger("disconnect", StringsConst.COMMAND_DISCONNECT_TITLE));
        }
        buttons.add(Button.primary("playlist", StringsConst.COMMAND_PLAYLIST_TITLE));
        return buttons;
    }

    private void barGenerator(EmbedBuilder builder, AudioTrack np, String duration) {
        String position = Utils.convertLongToString(np.getPosition());
        builder.addField(
                "🔊 1️⃣ "+np.getInfo().title + " 🎶",
                "**" + StringsConst.MESSAGE_DURATION + "** ``"+position+" / "+duration + "``\n**" + StringsConst.MESSAGE_AUTHOR + "** ``"+np.getInfo().author+"``",
                false);
    }

    private class PlayerUpdater extends TimerTask {
        @Override
        public void run() {
            if (musicPlayer.isPause()) {
                logger.debug("En pause : Timer cancel");
                cancel();
            } else if (musicPlayer.getAudioPlayer().getPlayingTrack() == null) {
                logger.debug("getPlayingTrack null : Timer cancel");
                messageEnCours = messageEnCours.editMessageEmbeds(getEmbed()).setActionRow(addTrackEndButtons()).complete();
                cancel();
            }
            else {
                if (musicPlayer.getQueue().isEmpty()) {
                    logger.debug("getQueue().isEmpty() : Timer cancel");
                    messageEnCours = messageEnCours.editMessageEmbeds(getEmbed()).setActionRow(addTrackEndButtons()).complete();
                    cancel();
                } else {
                    logger.debug("Timer");
                    messageEnCours = messageEnCours.editMessageEmbeds(getEmbed()).complete();
                }
            }
        }
    }

}
