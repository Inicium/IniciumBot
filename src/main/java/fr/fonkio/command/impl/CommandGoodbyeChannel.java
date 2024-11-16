package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.enums.ConfigurationGuildEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.List;

public class CommandGoodbyeChannel extends AbstractCommand {

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

                List<SelectOption> optionList = getSelectOptionsChannelList(guild, ConfigurationGuildEnum.QUIT_CHANNEL);
                eventSlash.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_GOODBYE_TITLE, StringsConst.COMMAND_GOODBYE_SUCCESS))
                        .addActionRow(
                                StringSelectMenu.create("choix-channel-goodbye")
                                        .setMinValues(0)
                                        .setMaxValues(1)
                                        .addOptions(optionList).build()
                        )
                        .setEphemeral(true).queue();
            }
        }
        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return false;
    }
}
