package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.message.EmbedGenerator;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CommandMoveAll extends AbstractCommand {

    @Override
    public boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        if (eventSlash == null) {
            return false;
        }
        Member member = eventSlash.getMember();
        User user = eventSlash.getUser();
        Guild guild = eventSlash.getGuild();

        if(member != null && guild != null) {
            GuildVoiceState guildVoiceState = member.getVoiceState();
            if (guildVoiceState != null) {
                AudioChannel sourceAudioChannel = guildVoiceState.getChannel();
                OptionMapping destinationOption = eventSlash.getOption("destination");
                AudioChannel destinationAudioChannelParameter = null;
                if(destinationOption != null) {
                    destinationAudioChannelParameter = destinationOption.getAsAudioChannel();
                }
                if (destinationAudioChannelParameter != null && sourceAudioChannel != null) {
                    if(member.hasPermission(Permission.VOICE_MOVE_OTHERS) && member.hasAccess(destinationAudioChannelParameter)) {
                        for (Member m : sourceAudioChannel.getMembers()) {
                            guild.moveVoiceMember(m, destinationAudioChannelParameter).queue();
                        }
                        eventSlash.replyEmbeds(EmbedGenerator.generate(user, "MoveAll", "J'ai déplacé tout le monde !")).setEphemeral(true).queue();
                    } else {
                        eventSlash.replyEmbeds(EmbedGenerator.generate(user, "MoveAll", "Tu n'as pas les permissions nécessaires pour déplacer des membres ou pour accéder à ce channel"))
                                .setEphemeral(true).queue();
                    }
                } else {
                    eventSlash.replyEmbeds(EmbedGenerator.generate(user, "MoveAll", "Le channel est introuvable"))
                            .setEphemeral(true).queue();
                }

            } else {
                eventSlash.replyEmbeds(EmbedGenerator.generate(user, "MoveAll", "Tu dois être co sur un channel vocal pour demander ça."))
                        .setEphemeral(true).queue();
            }
        } else {
            return false;
        }
        return true;
    }
}
