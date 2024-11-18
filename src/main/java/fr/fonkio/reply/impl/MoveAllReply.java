package fr.fonkio.reply.impl;

import fr.fonkio.reply.AbstractReply;
import fr.fonkio.inicium.Utils;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class MoveAllReply extends AbstractReply {

    @Override
    public boolean reply(IReplyCallback event, Guild guild, User user) {
        Member memberSender = event.getMember();

        if(!(event instanceof SlashCommandInteractionEvent) || memberSender == null ) {
            return false;
        }
        SlashCommandInteractionEvent slashCommandEvent = (SlashCommandInteractionEvent) event;
        GuildVoiceState guildVoiceStateOfSender = memberSender.getVoiceState();
        if (guildVoiceStateOfSender != null) {
            AudioChannel audioChannelOfSender = guildVoiceStateOfSender.getChannel();
            OptionMapping optionDestination = slashCommandEvent.getOption("destination");
            AudioChannel audioChannelDestination = null;
            if(optionDestination != null) {
                audioChannelDestination = optionDestination.getAsChannel().asAudioChannel();
            }
            if (audioChannelDestination != null) {
                if (audioChannelOfSender != null) {
                    if(memberSender.hasPermission(Permission.VOICE_MOVE_OTHERS) && memberSender.hasAccess(audioChannelDestination)) {
                        for (Member memberToMove : audioChannelOfSender.getMembers()) {
                            guild.moveVoiceMember(memberToMove, audioChannelDestination).queue();
                            logger.info(Utils.getFormattedLogString(guild, "Déplacement de " + memberToMove.getUser().getName() + " de " + audioChannelOfSender.getName() + " vers " + audioChannelDestination.getName()));
                        }
                        slashCommandEvent.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_MOVEALL_TITLE, StringsConst.COMMAND_MOVEALL_SUCCESS)).setEphemeral(true).queue();
                    } else {
                        slashCommandEvent.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_MOVEALL_TITLE, StringsConst.COMMAND_MOVEALL_PERM))
                                .setEphemeral(true).queue();
                    }
                } else {
                    slashCommandEvent.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_MOVEALL_TITLE, StringsConst.MESSAGE_NOT_CONNECTED))
                            .setEphemeral(true).queue();
                }
            } else {
                slashCommandEvent.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_MOVEALL_TITLE, StringsConst.MESSAGE_UNKNOWN_CHANNEL))
                        .setEphemeral(true).queue();
            }

        } else {
            slashCommandEvent.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_MOVEALL_TITLE, StringsConst.MESSAGE_NOT_CONNECTED))
                    .setEphemeral(true).queue();
        }
        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return false;
    }
}
