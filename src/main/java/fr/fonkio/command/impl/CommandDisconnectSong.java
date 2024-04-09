package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.utils.ConfigurationEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class CommandDisconnectSong extends AbstractCommand {
    @Override
    public boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        if (eventButton != null) {
            runButtonCommand(eventButton);
        } else {
            runSlashCommand(eventSlash);
        }
        return true;
    }

    private static void runButtonCommand(ButtonInteractionEvent eventButton) {
        Guild guild = eventButton.getGuild();
        if(guild == null) {
            return;
        }
        String newParam = "false";
        if (ButtonStyle.SUCCESS.equals(eventButton.getComponent().getStyle())) {
            newParam = "true";
        }
        Inicium.CONFIGURATION.setGuildConfig(guild.getId(), ConfigurationEnum.DC_SONG, newParam);
        eventButton.getHook().editOriginalEmbeds(EmbedGenerator.generate(eventButton.getUser(), StringsConst.MESSAGE_CONFIRM_TITLE, StringsConst.MESSAGE_CONFIRM)).setActionRow(Button.success("saved",StringsConst.BUTTON_SAVED).asDisabled()).queue();
    }

    private void runSlashCommand(SlashCommandInteractionEvent eventSlash) {
        Guild guild = eventSlash.getGuild();
        User user = eventSlash.getUser();
        if(guild != null) {
            Member member = guild.getMember(user);
            if (member != null) {
                if (!member.hasPermission(Permission.ADMINISTRATOR)) {
                    permissionCheck(eventSlash, user);
                    return;
                }
                ItemComponent ic;
                if ("false".equals(Inicium.CONFIGURATION.getGuildConfig(guild.getId(), ConfigurationEnum.DC_SONG))) {
                    ic = Button.success("disconnectsong", "Activer");
                } else {
                    ic = Button.danger("disconnectsong", "Désactiver");
                }
                eventSlash.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_DISCONNECT_SONG_TITLE, StringsConst.COMMAND_DISCONNECT_SONG_SUCCESS))
                        .addActionRow(ic)
                        .setEphemeral(true).queue();
            }
        }
    }
}
