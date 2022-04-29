package fr.fonkio.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.fonkio.message.MusicPlayer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

import java.util.HashMap;
import java.util.Map;

public class MusicManager {

    private final AudioPlayerManager manager = new DefaultAudioPlayerManager();
    private final Map<String, MusicPlayer> players = new HashMap<>();

    public MusicManager() {
        AudioSourceManagers.registerRemoteSources(manager);
        AudioSourceManagers.registerLocalSource(manager);
    }

    public synchronized MusicPlayer getPlayer (Guild guild) {
        if (!players.containsKey(guild.getId())) {
            players.put(guild.getId(), new MusicPlayer(manager.createPlayer(), guild));
        }
        return players.get(guild.getId());
    }


    public void loadTrack (final Guild guild, final String source, User author, GenericInteractionCreateEvent event) {
        loadTrack(guild, source, author, event, true);
    }
    public void loadTrack (final Guild guild, final String source, User author, GenericInteractionCreateEvent event, boolean message) {
        MusicPlayer player = getPlayer(guild);
        guild.getAudioManager().setSendingHandler(player.getAudioHandler());
        manager.loadItemOrdered(player, source, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                player.playTrack(track);
                if (message) {
                    player.getPlayerMessage().newMessage("▶ Play",
                            "\uD83D\uDCBF\u200B Ajout de la piste \uD83D\uDCBF\u200B" +
                                    "\n\uD83D\uDD17\u200B " + track.getInfo().uri +
                                    "\n\uD83C\uDFB5\u200B " + track.getInfo().title +
                                    "\n\uD83C\uDF99️\u200B " + track.getInfo().author
                            , author, true, event);
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

                for(int i = 0; i < playlist.getTracks().size(); i++) {
                    AudioTrack track = playlist.getTracks().get(i);
                    player.playTrack(track);
                }
                player.getPlayerMessage().newMessage("▶ Play", "Ajout de la playlist **" + playlist.getName() + "**\n",author, true, event);
            }

            @Override
            public void noMatches() {
                player.getPlayerMessage().newMessage("▶ Play", "La piste "+ source + " n'a pas été trouvée.",author, true, event);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
                player.getPlayerMessage().newMessage("▶ Play",
                        "Impossible de jouer la piste (raison:" +exception.getMessage()+")\n**SI C'EST UNE MUSIQUE ESSAYE D'AJOUTER \"LYRICS\" A TA RECHERCHE**"
                        ,author, true, event);
            }
        });
    }
}

