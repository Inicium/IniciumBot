package fr.fonkio.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.fonkio.inicium.Utils;
import fr.fonkio.message.MusicPlayer;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

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
    private final MusicPlayer musicPlayer;
    private final Timer timer = new Timer("timerUpdate");
    private TimerTask timerTask;

    private static final long DELAY  = 5000L;
    private static final long PERIOD = 5000L;
    private MessageEmbed oldEmbed;

    public PlayerMessage(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public void updatePlayerMessage(String command, String message, User author, boolean afficherQueue, InteractionHook hook) {

        if (messageEnCours != null && !messageEnCours.equals(hook.retrieveOriginal().complete())) {
            messageEnCours.editMessageEmbeds(oldEmbed).setActionRow(Button.success("done", StringsConst.BUTTON_DONE).asDisabled()).queue();
            if(timerTask != null) {
                try {
                    timerTask.cancel();
                } catch (IllegalStateException e) {
                    System.err.println("Le timer est déjà arrêté");
                }
            }
        }

        this.command = command;
        this.message = message;
        this.afficherQueue = afficherQueue;
        this.author = author;

        this.messageEnCours = hook.editOriginalEmbeds(getEmbed()).setComponents(addButtons()).complete();

        timerTask = new PlayerUpdater();
        timer.scheduleAtFixedRate(timerTask, DELAY, PERIOD);
    }

    private MessageEmbed getEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(StringsConst.MESSAGE_MUSIC_BOT, null, "https://i.pinimg.com/originals/79/ab/9f/79ab9f804b5ebbdd514af3329cad6e0c.gif?size=256");
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
                builder.addField(""+convertIntEmoji(i+2)+" "+at.getInfo().title,
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
        List<ItemComponent> itemComponentLine1List = new ArrayList<>();
        if (musicPlayer.getAudioPlayer().getPlayingTrack() == null) {
            itemComponentLine1List.add(Button.success("done", StringsConst.BUTTON_DONE).asDisabled());
            actionRowList.add(ActionRow.of(itemComponentLine1List));
            return actionRowList;
        }
        List<ItemComponent> itemComponentLine2List = new ArrayList<>();
        if (musicPlayer.isPause()) {
            itemComponentLine1List.add(Button.success("resume", StringsConst.COMMAND_PLAY_TITLE));
        } else {
            itemComponentLine1List.add(Button.success("pause", StringsConst.COMMAND_PAUSE_TITLE));
        }
        itemComponentLine1List.add(Button.primary("skip", StringsConst.COMMAND_SKIP_TITLE));
        itemComponentLine1List.add(Button.primary("shuffle", StringsConst.COMMAND_SHUFFLE_TITLE));
        Button button = Button.danger("clear", StringsConst.COMMAND_CLEAR_TITLE);
        if (musicPlayer.getQueue().size()<2) {
            button = button.asDisabled();
        }
        itemComponentLine2List.add(button);
        itemComponentLine2List.add(Button.danger("disconnect", StringsConst.COMMAND_DISCONNECT_TITLE));
        actionRowList.add(ActionRow.of(itemComponentLine1List));
        actionRowList.add(ActionRow.of(itemComponentLine2List));
        return actionRowList;
    }

    public List<ItemComponent> addTrackEndButtons() {
        List<ItemComponent> buttons = new ArrayList<>();
        buttons.add(Button.success("done", StringsConst.BUTTON_DONE).asDisabled());
        buttons.add(Button.danger("disconnect", StringsConst.COMMAND_DISCONNECT_TITLE));
        return buttons;
    }

    private void barGenerator(EmbedBuilder builder, AudioTrack np, String duration) {
        String position = Utils.convertLongToString(np.getPosition());
        builder.addField("\uD83C\uDFB6 1️⃣ "+np.getInfo().title + " \uD83D\uDD0A", "**" + StringsConst.MESSAGE_DURATION + "** ``"+position+" / "+duration + "``\n**" + StringsConst.MESSAGE_AUTHOR + "** ``"+np.getInfo().author+"``", false);
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
                messageEnCours = messageEnCours.editMessageEmbeds(getEmbed()).setActionRow(addTrackEndButtons()).complete();
                cancel();
            }
            else {
                if (musicPlayer.getQueue().size()==0) {
                    messageEnCours = messageEnCours.editMessageEmbeds(getEmbed()).setActionRow(addTrackEndButtons()).complete();
                    cancel();
                } else {
                    messageEnCours = messageEnCours.editMessageEmbeds(getEmbed()).complete();
                }
            }
        }
    }

}
