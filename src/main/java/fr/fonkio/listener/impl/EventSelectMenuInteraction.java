package fr.fonkio.listener.impl;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.inicium.Utils;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.music.PlayExecutor;
import fr.fonkio.enums.ConfigurationGuildEnum;
import fr.fonkio.enums.SelectMenuIdEnum;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EventSelectMenuInteraction extends ListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(EventSelectMenuInteraction.class);
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        Guild guild = event.getGuild();
        SelectMenuIdEnum menuId = SelectMenuIdEnum.getSelectMenuIdEnum(event.getComponentId());
        if (guild == null || menuId == null) {
            return;
        }
        String guildId = guild.getId();
        logger.info(Utils.getFormattedLogString(guild, "Composant :"+ event.getComponentId() + " / Choix : " + event.getValues()));
        switch (menuId) {
            case GOODBYE:
                updateGuildGonfig(guildId, ConfigurationGuildEnum.QUIT_CHANNEL, event.getValues());
                break;
            case WELCOME:
                updateGuildGonfig(guildId, ConfigurationGuildEnum.WELCOME_CHANNEL, event.getValues());
                break;
            case BLACKLIST:
                Inicium.CONFIGURATION.delBlacklist(guildId);
                if (!event.getValues().isEmpty()) {
                    Inicium.CONFIGURATION.addBlackList(guildId, event.getValues());
                }
                break;
            case DEFAULT_ROLE:
                updateGuildGonfig(guildId, ConfigurationGuildEnum.DEFAULT_ROLE, event.getValues());
                break;
            case PLAYLIST:
                InteractionHook hook = event.deferReply().complete();
                PlayExecutor.runPlay(event.getUser(), guild, hook, event.getValues().get(0), false);
                return;
            case PLAYLIST_REMOVE:
                Inicium.CONFIGURATION.removeFromPlaylist(guildId, event.getValues());
                break;
        }
        event.editMessageEmbeds(EmbedGenerator.generate(event.getUser(), StringsConst.MESSAGE_CONFIRM_TITLE, StringsConst.MESSAGE_CONFIRM)).setActionRow(Button.success("saved",StringsConst.BUTTON_SAVED).asDisabled()).queue();
    }

    private static void updateGuildGonfig(String guildId, ConfigurationGuildEnum quitChannel, List<String> values) {
        Inicium.CONFIGURATION.setGuildConfig(guildId, quitChannel, values.isEmpty() ? "" : values.get(0));
    }
}
