package fr.fonkio.command;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.inicium.Utils;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.enums.ConfigurationGuildEnum;
import fr.fonkio.utils.PlaylistItem;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildMessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand {

    protected Logger logger = LoggerFactory.getLogger(AbstractCommand.class);

    protected abstract boolean run(SlashCommandInteractionEvent event, ButtonInteractionEvent eventButton);

    public abstract boolean isBlacklistable();

    public boolean execute(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        if (eventSlash != null) {
            StringBuilder log = new StringBuilder("eventSlash : " + eventSlash.getName() + " ");
            if (!eventSlash.getOptions().isEmpty()) {
                for (OptionMapping optionMapping : eventSlash.getOptions()) {
                    log.append(optionMapping.getName()).append("=").append(optionMapping.getAsString());
                }
            }
            log.append(" par ").append(eventSlash.getUser().getName());
            logger.info(Utils.getFormattedLogString(eventSlash.getGuild(), log.toString()));
        } else if (eventButton != null) {
            logger.info(Utils.getFormattedLogString(eventButton.getGuild(),"eventButton : " + eventButton.getComponent().getLabel() + " par " + eventButton.getUser().getName()));
        } else {
            logger.error("Execution sans event impossible !");
            return false;
        }
        return this.run(eventSlash, eventButton);
    }

    /** Recupère la liste des musiques de la playlist du serveur sous forme de liste de SelectOption
     *
     * @param guild Serveur Discord
     * @return List<SelectOption> pour ajouter dans un Select
     */
    protected List<SelectOption> getSelectOptionsPlaylist(Guild guild, String emoji) {
        List<SelectOption> optionList = new ArrayList<>();
        List<PlaylistItem> playlist = Inicium.CONFIGURATION.getPlaylist(guild.getId());
        playlist.forEach(
                playlistItem -> optionList.add(SelectOption.of(playlistItem.getLabel(), playlistItem.getUrl())
                        .withDescription(playlistItem.getUrl())
                        .withEmoji(Emoji.fromUnicode(emoji)))
        );
        return optionList;
    }

    /** Recupère la liste des channels du serveur sous forme de liste de SelectOption
     * Les channels sont sélectionnés par défaut (withDefault(true)) en fonction de la config
     * passée en paramètre
     * @see ConfigurationGuildEnum
     * @param guild Serveur Discord
     * @param configurationGuildEnum Liste à récupérer
     * @return List<SelectOption> pour ajouter dans un Select
     */
    protected List<SelectOption> getSelectOptionsChannelList(Guild guild, ConfigurationGuildEnum configurationGuildEnum) {
        List<SelectOption> optionList = new ArrayList<>();
        for (GuildChannel guildChannel : guild.getChannels()) {
            if (!(guildChannel instanceof StandardGuildMessageChannel)) { //Si ce n'est pas un channel textuel (News, Rules ...)
                continue;
            }
            //On sélectionne ou pas le channel dans la liste pour représenter la config actuelle
            boolean defaultValue;
            if (ConfigurationGuildEnum.BLACK_LIST.equals(configurationGuildEnum)) {
                defaultValue = Inicium.CONFIGURATION.blackListContains(guild.getId(), guildChannel.getId());
            } else {
                defaultValue = guildChannel.getId().equals(Inicium.CONFIGURATION.getGuildConfig(guild.getId(), configurationGuildEnum));
            }

            optionList.add(SelectOption.of(guildChannel.getName(), guildChannel.getId())
                    .withDescription(StringsConst.SELECT_OPTION_DEFINE + guildChannel.getName())
                    .withEmoji(Emoji.fromUnicode("\ud83d\udce2"))
                    .withDefault(defaultValue));
        }
        return optionList;
    }

    protected List<SelectOption> getSelectOptionsRoleList(Guild guild) {
        List<SelectOption> optionList = new ArrayList<>();
        int i = 0;
        for (Role role : guild.getRoles()) {
            boolean defaultValue = role.getId().equals(Inicium.CONFIGURATION.getGuildConfig(guild.getId(), ConfigurationGuildEnum.DEFAULT_ROLE));
            if (i < 25) {
                optionList.add(SelectOption.of(role.getName(), role.getId())
                        .withDescription(StringsConst.SELECT_OPTION_DEFINE + role.getName())
                        .withEmoji(Emoji.fromUnicode("\uD83D\uDC64"))
                        .withDefault(defaultValue));
            }
            i++;
        }
        return optionList;
    }

    protected void permissionCheck(SlashCommandInteractionEvent event, User user) {
        event.replyEmbeds(
                EmbedGenerator.generate(user, StringsConst.MESSAGE_ADMIN_PERM, StringsConst.MESSAGE_NO_ADMIN_PERM)
        ).setEphemeral(true).queue();
    }
}
