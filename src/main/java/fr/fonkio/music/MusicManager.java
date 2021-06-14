package fr.fonkio.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.fonkio.inicium.Inicium;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MusicManager {

    private final AudioPlayerManager manager = new DefaultAudioPlayerManager();
    private final Map<String, MusicPlayer> players = new HashMap<>();
    private Inicium fonkBot;

    public MusicManager(Inicium fonkBot) {
        this.fonkBot = fonkBot;
        AudioSourceManagers.registerRemoteSources(manager);
        AudioSourceManagers.registerLocalSource(manager);
    }

    public synchronized MusicPlayer getPlayer (Guild guild) {
        if (!players.containsKey(guild.getId())) {
            players.put(guild.getId(), new MusicPlayer(manager.createPlayer(), guild));
        }
        return players.get(guild.getId());
    }

    public void loadTrack (final TextChannel channel, final String source) {
        loadTrack(channel, source, true);
    }
    public void loadTrack (final TextChannel channel, final String source, boolean message) {
        MusicPlayer player = getPlayer(channel.getGuild());
        channel.getGuild().getAudioManager().setSendingHandler(player.getAudioHandler());
        manager.loadItemOrdered(player, source, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                if (!message) {
                    player.playTrack(track);
                    return;
                }

                player.playTrack(track);
                MessageAction ma = channel.sendMessage(fonkBot.createEmbed("Play", null, "Ajout de la piste...\n"+track.getInfo().uri+"\n ", Color.green, true, channel.getGuild(), true));
                fonkBot.addButtons(ma, player.getGuild()).queue((message)->{
                    fonkBot.stopUpdateBar();
                    fonkBot.launchUpdateBar(message);
                });


            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                StringBuilder builder = new StringBuilder();

                builder.append("Ajout de la playlist **").append(playlist.getName()).append("**\n");

                for(int i = 0; i < playlist.getTracks().size(); i++) {
                    AudioTrack track = playlist.getTracks().get(i);
                    player.playTrack(track);
                }

                MessageAction ma = channel.sendMessage(fonkBot.createEmbed("Play", null, builder.toString(), Color.green, true, channel.getGuild(), true));
                fonkBot.addButtons(ma, player.getGuild()).queue((message)->{
                    fonkBot.stopUpdateBar();
                    fonkBot.launchUpdateBar(message);
                });
            }

            @Override
            public void noMatches() {

                MessageAction ma = channel.sendMessage(fonkBot.createEmbed("Play", null, "La piste "+ source + " n'a pas été trouvée.", Color.red, true, channel.getGuild()));
                fonkBot.addButtons(ma, player.getGuild()).queue((message)->{
                    fonkBot.stopUpdateBar();
                    fonkBot.launchUpdateBar(message);
                });

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
                MessageAction ma = channel.sendMessage(fonkBot.createEmbed("Play", null, "Impossible de jouer la piste (raison:" +exception.getMessage()+")\n**SI C'EST UNE MUSIQUE ESSAYE D'AJOUTER \"LYRICS\" A TA RECHERCHE**"
                        + "Exception :", Color.red, true, channel.getGuild()));
                fonkBot.addButtons(ma, player.getGuild()).queue((message)->{
                    fonkBot.stopUpdateBar();
                    fonkBot.launchUpdateBar(message);
                });

            }
        });
    }
}

