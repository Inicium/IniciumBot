package fr.fonkio.command;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.music.YoutubeSearch;
import fr.fonkio.utils.ConfigurationEnum;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand {

    protected  YoutubeSearch youtubeSearch = new YoutubeSearch();

    public abstract boolean run(SlashCommandInteractionEvent event, ButtonInteractionEvent eventButton);

    public boolean canNotSendCommand(User user, Guild guild, GenericInteractionCreateEvent event) {
        Member member = guild.getMember(user);
        if (member == null) {
            return true;
        }
        GuildVoiceState guildVoiceState = member.getVoiceState();
        if (guildVoiceState == null) {
            return true;
        }
        AudioChannel userVoiceChannel = guildVoiceState.getChannel();
        Member self = member.getGuild().getMember(Inicium.getJda().getSelfUser());
        if (self != null) {
            GuildVoiceState botGuildVoiceState = self.getVoiceState();
            if (botGuildVoiceState != null) {
                AudioChannel botVoiceChannel = botGuildVoiceState.getChannel();
                MessageEmbed messageEmbed;

                if (userVoiceChannel == null) {
                    messageEmbed = EmbedGenerator.generate(member.getUser(), "Tu dois être connecté", "Tu peux envoyer des commandes uniquement si tu es connecté !");
                } else {
                    if (botVoiceChannel == null) {
                        return false;
                    }
                    String idBotVoiceChannel = botVoiceChannel.getId();
                    if (!userVoiceChannel.getId().equals(idBotVoiceChannel)) {
                        messageEmbed = EmbedGenerator.generate(member.getUser(), "Bot occupé", "Le bot est déjà connecté dans un autre channel !");
                    } else {
                        return false;
                    }
                }
                if (event instanceof SlashCommandInteractionEvent) {
                    ((SlashCommandInteractionEvent)event).replyEmbeds(messageEmbed).setEphemeral(true).queue();
                } else if (event instanceof ButtonInteractionEvent) {
                    ((ButtonInteractionEvent)event).replyEmbeds(messageEmbed).setEphemeral(true).queue();
                }

            }
        }
        return true;
    }

    protected List<SelectOption> getSelectOptionsChannelList(Guild guild, ConfigurationEnum configurationEnum) {
        List<SelectOption> optionList = new ArrayList<>();
        for (TextChannel tc : guild.getTextChannels()) {
            boolean defaultValue = false;
            if (configurationEnum != null) {
                defaultValue = tc.getId().equals(Inicium.CONFIGURATION.getGuildConfig(guild.getId(), configurationEnum));
            } else {
                defaultValue = Inicium.CONFIGURATION.blackListContains(guild.getId(), tc.getId());
            }

            optionList.add(SelectOption.of(tc.getName(), tc.getId())
                    .withDescription("Définir " + tc.getName())
                    .withEmoji(Emoji.fromUnicode("\ud83d\udce2"))
                    .withDefault(defaultValue));
        }
        return optionList;
    }

    protected void permissionCheck(SlashCommandInteractionEvent event, User user) {
        replyEmbed(event, user, "Permission", "Vous n'êtes pas administrateur de ce serveur");
    }

    protected void replyEmbed(SlashCommandInteractionEvent event, User user, String title, String reply) {
        event.replyEmbeds(
                EmbedGenerator.generate(user, title, reply)
        ).queue();
    }
}