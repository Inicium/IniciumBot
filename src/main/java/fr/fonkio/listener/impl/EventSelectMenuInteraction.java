package fr.fonkio.listener.impl;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.utils.ConfigurationEnum;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class EventSelectMenuInteraction extends ListenerAdapter {
    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) {
            return;
        }
        String guildId = guild.getId();
        switch (event.getComponentId()) {
            case "choix-channel-goodbye":
                if (event.getValues().isEmpty()) {
                    Inicium.CONFIGURATION.setGuildConfig(guildId, ConfigurationEnum.QUIT_CHANNEL, "");
                } else {
                    Inicium.CONFIGURATION.setGuildConfig(guildId, ConfigurationEnum.QUIT_CHANNEL, event.getValues().get(0));
                }
                break;
            case "choix-channel-welcome":
                if (event.getValues().isEmpty()) {
                    Inicium.CONFIGURATION.setGuildConfig(guildId, ConfigurationEnum.WELCOME_CHANNEL, "");
                } else {
                    Inicium.CONFIGURATION.setGuildConfig(guildId, ConfigurationEnum.WELCOME_CHANNEL, event.getValues().get(0));
                }
                break;
            case "choix-channel-blacklist":
                Inicium.CONFIGURATION.delBlacklist(guildId);
                if (!event.getValues().isEmpty()) {
                    Inicium.CONFIGURATION.addBlackList(guildId, event.getValues());
                }
                break;
        }
        event.editMessageEmbeds(EmbedGenerator.generate(event.getUser(), StringsConst.MESSAGE_CONFIRM_TITLE, StringsConst.MESSAGE_CONFIRM)).setActionRow(Button.success("saved",StringsConst.BUTTON_SAVED).asDisabled()).queue();
    }
}
