package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.utils.ConfigurationEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

import java.util.List;

public class CommandWelcomeChannel extends AbstractCommand {
    @Override
    public boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        if (eventSlash == null) {
            return false;
        }
        Guild guild = eventSlash.getGuild();
        User user = eventSlash.getUser();
        if(guild != null) {
            Member member = guild.getMember(user);
            if (member != null) {
                if (!member.hasPermission(Permission.ADMINISTRATOR)) {
                    permissionCheck(eventSlash, user);
                    return true;
                }
                List<SelectOption> optionList = getSelectOptionsChannelList(guild, ConfigurationEnum.WELCOME_CHANNEL);
                eventSlash.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_WELCOME_TITLE, StringsConst.COMMAND_WELCOME_SUCCESS))
                        .addActionRow(
                                SelectMenu.create("choix-channel-welcome")
                                        .setMinValues(0)
                                        .setMaxValues(1)
                                        .addOptions(optionList).build()
                        )
                        .setEphemeral(true).queue();
            }
        }
        return true;
    }
}
