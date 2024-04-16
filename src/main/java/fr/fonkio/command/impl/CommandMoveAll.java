package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.inicium.Utils;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CommandMoveAll extends AbstractCommand {

    @Override
    public boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        if (eventSlash == null) {
            return false;
        }
        Member memberSender = eventSlash.getMember();
        User user = eventSlash.getUser();
        Guild guild = eventSlash.getGuild();

        if(memberSender != null && guild != null) {
            GuildVoiceState guildVoiceStateOfSender = memberSender.getVoiceState();
            if (guildVoiceStateOfSender != null) {
                AudioChannel audioChannelOfSender = guildVoiceStateOfSender.getChannel();
                OptionMapping optionDestination = eventSlash.getOption("destination");
                AudioChannel audioChannelDestination = null;
                if(optionDestination != null) {
                    audioChannelDestination = optionDestination.getAsChannel().asAudioChannel();
                }
                if (audioChannelDestination != null) {
                    if (audioChannelOfSender != null) {
                        if(memberSender.hasPermission(Permission.VOICE_MOVE_OTHERS) && memberSender.hasAccess(audioChannelDestination)) {
                            for (Member memberToMove : audioChannelOfSender.getMembers()) {
                                guild.moveVoiceMember(memberToMove, audioChannelDestination).queue();
                                logger.info(Utils.getFormattedLogString(eventSlash.getGuild(), "Déplacement de " + memberToMove.getUser().getName() + " de " + audioChannelOfSender.getName() + " vers " + audioChannelDestination.getName()));
                            }
                            eventSlash.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_MOVEALL_TITLE, StringsConst.COMMAND_MOVEALL_SUCCESS)).setEphemeral(true).queue();
                        } else {
                            eventSlash.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_MOVEALL_TITLE, StringsConst.COMMAND_MOVEALL_PERM))
                                    .setEphemeral(true).queue();
                        }
                    } else {
                        eventSlash.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_MOVEALL_TITLE, StringsConst.MESSAGE_NOT_CONNECTED))
                                .setEphemeral(true).queue();
                    }
                } else {
                    eventSlash.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_MOVEALL_TITLE, StringsConst.MESSAGE_UNKNOWN_CHANNEL))
                            .setEphemeral(true).queue();
                }

            } else {
                eventSlash.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_MOVEALL_TITLE, StringsConst.MESSAGE_NOT_CONNECTED))
                        .setEphemeral(true).queue();
            }
        } else {
            return false;
        }
        return true;
    }
}
