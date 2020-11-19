package fr.fonkio.music;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.fonkio.command.Command;
import fr.fonkio.inicium.Inicium;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicCommand {
    private final YouTube youTube;
    private final MusicManager manager;
    private Inicium iniciumBot;

    public MusicCommand(Inicium iniciumBot) {
        this.iniciumBot = iniciumBot;
        manager = new MusicManager(iniciumBot);
        YouTube temp = null;

        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null)
                    .setApplicationName("API YouTube BOT")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        youTube = temp;
    }

    @Command(name="play", type= Command.ExecutorType.USER)
    private void play(Guild guild, TextChannel textChannel, User user, String command) {
        playExec(guild, textChannel, user, command);
    }

    @Command(name="p", type= Command.ExecutorType.USER)
    private void p(Guild guild, TextChannel textChannel, User user, String command) {
        playExec(guild, textChannel, user, command);
    }

    @Command(name="ps", type= Command.ExecutorType.USER)
    private void ps(Guild guild, TextChannel textChannel, User user, String command) {
        playExec(guild, textChannel, user, command);
        TimerTask task = new TimerTask() {
            public void run() {
                skipExec(guild, textChannel);
            }
        };
        Timer timer = new Timer("Timer");
        long delay = 500L;
        timer.schedule(task, delay);
    }

    @Command(name="skip",type= Command.ExecutorType.USER)
    private void skip(Guild guild, TextChannel textChannel) {
        skipExec(guild, textChannel);
    }
    @Command(name="s",type= Command.ExecutorType.USER)
    private void s(Guild guild, TextChannel textChannel) {
        skipExec(guild, textChannel);
    }

    @Command(name = "clear", type = Command.ExecutorType.USER)
    private void clear(TextChannel textChannel) {
        clearExec(textChannel);
    }

    @Command(name = "clr", type = Command.ExecutorType.USER)
    private void clr(TextChannel textChannel) {
        clearExec(textChannel);
    }

    @Command(name = "clean", type = Command.ExecutorType.USER)
    private void clean(TextChannel textChannel) {
        clearExec(textChannel);
    }

    @Command(name = "seek", type = Command.ExecutorType.USER)
    private void seek(Guild guild, TextChannel textChannel, String command) {
        seekExec(guild, textChannel, command);
    }

    @Command(name = "pause", type = Command.ExecutorType.USER)
    private void pause(Guild guild, TextChannel textChannel) {
        pauseExec(guild, textChannel);
    }

    @Command(name = "resume", type = Command.ExecutorType.USER)
    private void resume(Guild guild, TextChannel textChannel) {
        resumeExec(guild, textChannel);
    }
    @Command(name = "disconnect", type = Command.ExecutorType.USER)
    private void disconnect(Guild guild, TextChannel textChannel) {
        disconnectExec(guild, textChannel);
    }
    @Command(name = "dc", type = Command.ExecutorType.USER)
    private void dc(Guild guild, TextChannel textChannel) {
        disconnectExec(guild, textChannel);
    }
    @Command(name = "leave", type = Command.ExecutorType.USER)
    private void leave(Guild guild, TextChannel textChannel) {
        disconnectExec(guild, textChannel);
    }
    @Command(name = "quit", type = Command.ExecutorType.USER)
    private void quit(Guild guild, TextChannel textChannel) {
        disconnectExec(guild, textChannel);
    }
    @Command(name = "np", type = Command.ExecutorType.USER)
    private void np(Guild guild, TextChannel textChannel) {
        queueExec(guild, textChannel);
    }
    @Command(name = "queue", type = Command.ExecutorType.USER)
    private void queue(Guild guild, TextChannel textChannel) {
        queueExec(guild, textChannel);
    }

    private void queueExec(Guild guild, TextChannel textChannel) {
        if(!guild.getAudioManager().isConnected()) {
            textChannel.sendMessage(iniciumBot.createEmbed("Now playing", null, "Il n'y a pas de musique en cours ...", Color.red, false, guild)).queue();
            return;
        }
        textChannel.sendMessage(iniciumBot.createEmbed("Now playing", null, "⏬⏬ Voici la liste ⏬⏬", Color.green, true, guild)).queue((message) -> {
            iniciumBot.addReaction(message);
            iniciumBot.stopUpdateBar();
            iniciumBot.launchUpdateBar(message);
        });
    }

    private void pauseExec (Guild guild, TextChannel textChannel) {
        if(!guild.getAudioManager().isConnected()) {
            textChannel.sendMessage(iniciumBot.createEmbed("Pause", null, "Il n'y a pas de musique en cours ...", Color.red, false, guild)).queue();
            return;
        }
        if(manager.getPlayer(guild).isPause()) {

            textChannel.sendMessage(iniciumBot.createEmbed("Pause", null, "Déjà en pause...", Color.red, false, guild)).queue((message) -> {
                iniciumBot.addReaction(message);
            });
            return;
        }
        manager.getPlayer(guild).setPause(true);

        textChannel.sendMessage(iniciumBot.createEmbed("Pause", null, "⏸", Color.green, false, guild)).queue((message) -> {
            iniciumBot.addReaction(message);
            iniciumBot.stopUpdateBar();
        });

    }
    private void resumeExec (Guild guild, TextChannel textChannel) {
        if(!guild.getAudioManager().isConnected()) {

            textChannel.sendMessage(iniciumBot.createEmbed("Resume", null, "Il n'y a pas de musique en cours ...", Color.red, false, guild)).queue();
            return;
        }
        if(manager.getPlayer(guild).isPause()) {
            manager.getPlayer(guild).setPause(false);

            textChannel.sendMessage(iniciumBot.createEmbed("Resume", null, "▶", Color.green, true, guild)).queue((message) -> {
                iniciumBot.addReaction(message);
                iniciumBot.stopUpdateBar();
                iniciumBot.launchUpdateBar(message);
            });
            return;
        }

        textChannel.sendMessage(iniciumBot.createEmbed("Resume", null, "Déjà en cours de lecture...", Color.red, false, guild)).queue((message) -> {
            iniciumBot.addReaction(message);
        });


    }

    private void seekExec(Guild guild, TextChannel textChannel, String command) {
        if(!guild.getAudioManager().isConnected()) {

            textChannel.sendMessage(iniciumBot.createEmbed("Seek", null, "Il n'y a pas de musique en cours ...", Color.red, false, guild)).queue();
            return;
        }
        try {
            manager.getPlayer(guild).seekTrack(command.replaceFirst("seek ", ""));
        } catch (IllegalArgumentException e) {
            textChannel.sendMessage(iniciumBot.createEmbed("Seek", null, "Le temps entré n'est pas valide", Color.red, false, guild)).queue((message)->{
                iniciumBot.addReaction(message);
            });
            return;
        }

        textChannel.sendMessage(iniciumBot.createEmbed("Seek", null, "La piste a été avancée à "+command.replaceFirst("seek ", "")+"...", Color.green, true, guild)).queue((message)->{
            iniciumBot.addReaction(message);
            iniciumBot.stopUpdateBar();
            iniciumBot.launchUpdateBar(message);
        });
    }

    private void playExec(Guild guild, TextChannel textChannel, User user, String command) {
        if (guild == null) {
            return;
        }
        VoiceChannel voiceChannel = guild.getMember(user).getVoiceState().getChannel();
        if (!guild.getAudioManager().isConnected()) {

            if (voiceChannel == null) {

                textChannel.sendMessage(iniciumBot.createEmbed("Play", null, "Tu dois être co sur un channel vocal pour demander ça.", Color.red, false, guild)).queue();
                return;
            }
            try {
                guild.getAudioManager().openAudioConnection(voiceChannel);
                guild.getAudioManager().setSelfDeafened(true);
            } catch (InsufficientPermissionException e){
                textChannel.sendMessage("Je n'ai pas la permission de rejoindre ce channel !");
            }

        } else {
            if (voiceChannel == null) {
                textChannel.sendMessage(iniciumBot.createEmbed("Play", null, "Tu dois être co sur un channel vocal pour demander ça.", Color.red, false, guild)).queue();
                return;
            }
            if (!voiceChannel.getId().equals(guild.getMember(iniciumBot.getJda().getSelfUser()).getVoiceState().getChannel().getId())) {
                try {
                    guild.getAudioManager().openAudioConnection(voiceChannel);
                } catch (InsufficientPermissionException e){
                    textChannel.sendMessage("Je n'ai pas la permission de rejoindre ce channel !");
                }
            }
        }
        if (command.startsWith("play")) {
            manager.loadTrack(textChannel, searchOrUrl(command.replaceFirst("play ", "")));
        } else if (command.startsWith("ps")) {
            manager.loadTrack(textChannel, searchOrUrl(command.replaceFirst("ps ", "")));
        } else if (command.startsWith("p")) {
            manager.loadTrack(textChannel, searchOrUrl(command.replaceFirst("p ", "")));
        }

    }

    private String searchOrUrl(String input) {
        if (input.startsWith("http")) {
            return input;
        } else {
            return searchYoutube(input);
        }
    }

    private String searchYoutube(String input) {
        System.out.println(input);
        try {
            List <SearchResult> result = youTube.search()
                    .list("id,snippet")
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Inicium.CONFIGURATION.getYtapi())
                    .execute()
                    .getItems();
            if (!result.isEmpty()) {
                String videoId = result.get(0).getId().getVideoId();
                return "https://www.youtube.com/watch?v="+videoId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void skipExec(Guild guild, TextChannel textChannel) {
        if(!guild.getAudioManager().isConnected()) {
            textChannel.sendMessage(iniciumBot.createEmbed("Skip", null, "Il n'y a pas de musique en cours ...", Color.red, false, guild)).queue();
            return;
        }
        manager.getPlayer(guild).skipTrack();
        textChannel.sendMessage(iniciumBot.createEmbed("Skip", null, "La piste viens d'être passée...", Color.green, true, guild)).queue((message)->{
            iniciumBot.addReaction(message);
            iniciumBot.stopUpdateBar();
            iniciumBot.launchUpdateBar(message);
        });
    }
    public void disconnectQuiet(Guild guild) {
        guild.getAudioManager().closeAudioConnection();
        MusicPlayer player = manager.getPlayer(guild);
        player.getListener().getTracks().clear();
        player.getAudioPlayer().stopTrack();
    }
    public void disconnectExec(Guild guild, TextChannel textChannel) {
        if(!guild.getAudioManager().isConnected()) {
            textChannel.sendMessage(iniciumBot.createEmbed("Disconnect", null, "Je ne suis déjà pas là ...", Color.red, false, guild)).queue();
            return;
        }
        MusicPlayer player = manager.getPlayer(guild);
        player.getListener().getTracks().clear();
        player.getAudioPlayer().stopTrack();
        if(guild.getId().equals("296520788033404929")) {
            manager.loadTrack(textChannel, "https://youtu.be/d9rUssodfpg", false);
            textChannel.sendMessage(iniciumBot.createEmbed("Disconnect", null, "Aurevoir 👋", Color.green, false, guild)).queue((message)->{
                iniciumBot.stopUpdateBar();
            });
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

    private void clearExec(TextChannel textChannel) {
        MusicPlayer player = manager.getPlayer(textChannel.getGuild());

        if(player.getListener().getTracks().isEmpty()) {

            textChannel.sendMessage(iniciumBot.createEmbed("Clear", null, "La liste est déjà vide ...", Color.red, true, textChannel.getGuild())).queue((message)->{
                iniciumBot.addReaction(message);
                iniciumBot.stopUpdateBar();
                iniciumBot.launchUpdateBar(message);
            });
            return;
        }

        player.getListener().getTracks().clear();

        textChannel.sendMessage(iniciumBot.createEmbed("Clear", null, "La liste a été effacée !", Color.green, true, textChannel.getGuild())).queue((message)->{
            iniciumBot.addReaction(message);
            iniciumBot.stopUpdateBar();
            iniciumBot.launchUpdateBar(message);
        });
    }

    public boolean isPause(Guild guild) {
        return manager.getPlayer(guild).getAudioPlayer().isPaused();
    }
    public List<AudioTrack> getQueue(Guild guild) {
        return manager.getPlayer(guild).getQueue();
    }
}
