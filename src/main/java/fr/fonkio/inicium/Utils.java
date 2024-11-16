package fr.fonkio.inicium;

import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.concurrent.TimeUnit;

public class Utils {

    public static String convertLongToString(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public static String getFormattedLogString(Guild guild, String message) {
        if (guild == null) {
            return message;
        }
        String log = "[" + guild.getName();
        if (guild.getOwner() != null) {
            log += " de " + guild.getOwner().getUser().getName();
        }
        log += "] " + message;
        return log;
    }

    public static boolean checkUserAndBotNoPermission(User user, Guild guild, InteractionHook hook) throws IllegalArgumentException{
        Member member = guild.getMember(user);
        if (member == null) {
            throw new IllegalArgumentException("member is null!");
        }

        GuildVoiceState memberGuildVoiceState = member.getVoiceState();
        if (memberGuildVoiceState == null) {
            throw new IllegalArgumentException("memberGuildVoiceState is null!");
        }

        AudioChannel memberAudioChannel = memberGuildVoiceState.getChannel();
        Member botMember = member.getGuild().getMember(Inicium.getJda().getSelfUser());
        if (botMember == null) {
            throw new IllegalArgumentException("botMember is null!");
        }

        GuildVoiceState botGuildVoiceState = botMember.getVoiceState();
        if (botGuildVoiceState == null) {
            throw new IllegalArgumentException("botGuildVoiceState is null!");
        }

        AudioChannel botAudioChannel = botGuildVoiceState.getChannel();
        MessageEmbed messageEmbed;
        if (memberAudioChannel == null) {
            messageEmbed = EmbedGenerator.generate(member.getUser(), StringsConst.MESSAGE_IMPOSSIBLE, StringsConst.MESSAGE_NOT_CONNECTED);
        } else {
            if (botAudioChannel == null) {
                return false;
            } else {
                if (memberAudioChannel.equals(botAudioChannel)) {
                    return false;
                }
                messageEmbed = EmbedGenerator.generate(member.getUser(), StringsConst.MESSAGE_BOT_BUSY, StringsConst.MESSAGE_BOT_IN_OTHER_CHANNEL);
            }
        }
        hook.editOriginalEmbeds(messageEmbed).queue();
        return true;
    }

}
