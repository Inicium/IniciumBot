package fr.fonkio.listener.impl;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.utils.ConfigurationEnum;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class EventSelectMenuInteraction extends ListenerAdapter {

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
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
            case "choix-default-role":
                if (event.getValues().isEmpty()) {
                    Inicium.CONFIGURATION.setGuildConfig(guildId, ConfigurationEnum.DEFAULT_ROLE, "");
                } else {
                    Inicium.CONFIGURATION.setGuildConfig(guildId, ConfigurationEnum.DEFAULT_ROLE, event.getValues().get(0));
                }
                break;
        }
        event.editMessageEmbeds(EmbedGenerator.generate(event.getUser(), StringsConst.MESSAGE_CONFIRM_TITLE, StringsConst.MESSAGE_CONFIRM)).setActionRow(Button.success("saved",StringsConst.BUTTON_SAVED).asDisabled()).queue();
    }
}
