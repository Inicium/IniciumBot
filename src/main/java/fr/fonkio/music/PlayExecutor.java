package fr.fonkio.music;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.inicium.Utils;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.InteractionHook;

public class PlayExecutor {

    private static final YoutubeSearch youtubeSearch = new YoutubeSearch();

    public static boolean runPlay(User user, Guild guild, InteractionHook hook, String musiqueParameterString, boolean skipBeforePlay) {
        if (guild == null) {
            return false;
        }

        if (Utils.checkUserAndBotNoPermission(user, guild, hook)) {
            return true;
        }

        Member member = guild.getMember(user);
        if (member != null && musiqueParameterString != null) {
            GuildVoiceState memberGuildVoiceState = member.getVoiceState();
            if (memberGuildVoiceState != null) {
                AudioChannel memberAudioChannel = memberGuildVoiceState.getChannel();
                if (!guild.getAudioManager().isConnected()) {
                    try {
                        guild.getAudioManager().openAudioConnection(memberAudioChannel);
                        guild.getAudioManager().setSelfDeafened(true);
                    } catch (InsufficientPermissionException e){
                        hook.editOriginalEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_PLAY_TITLE, StringsConst.MESSAGE_CANT_CONNECT)).queue();
                        return true;
                    }
                } else {
                    // Verification que l'utilisateur soit dans le même chan
                    Member botMember = guild.getMember(Inicium.getJda().getSelfUser());
                    if (botMember != null) {
                        GuildVoiceState botGuildVoiceState = botMember.getVoiceState();
                        if (botGuildVoiceState != null) {
                            AudioChannel botAudioChannel = botGuildVoiceState.getChannel();
                            if (botAudioChannel != null && !botAudioChannel.equals(memberAudioChannel)) {
                                try {
                                    guild.getAudioManager().openAudioConnection(memberAudioChannel);
                                } catch (InsufficientPermissionException e) {
                                    hook.editOriginalEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_PLAY_TITLE, StringsConst.MESSAGE_NO_PERMISSIONS)).queue();
                                }
                            }
                        }
                    }
                }
                if (skipBeforePlay) {
                    Inicium.manager.getPlayer(guild).skipTrack();
                }
                Inicium.manager.loadTrack(guild, youtubeSearch.searchOrUrl(musiqueParameterString), user, hook);
            }
        }
        return true;
    }
}
