package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

import java.util.List;

public class CommandBlacklist extends AbstractCommand {

    @Override
    public boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {

        if (eventSlash == null) {
            return false;
        }

        Guild guild = eventSlash.getGuild();
        User user = eventSlash.getUser();

        if (guild != null) {
            Member member = guild.getMember(user);
            if (member != null && !member.hasPermission(Permission.ADMINISTRATOR)) {
                permissionCheck(eventSlash, user);
            } else {
                List<SelectOption> optionList = getSelectOptionsChannelList(guild, null);
                eventSlash.replyEmbeds(EmbedGenerator.generate(user, StringsConst.COMMAND_BLACKLIST_TITLE, StringsConst.COMMAND_BLACKLIST_SUCCESS))
                        .addActionRow(
                                SelectMenu.create("choix-channel-blacklist")
                                        .setMinValues(0)
                                        .setMaxValues(optionList.size()-1)
                                        .addOptions(optionList).build()
                        )
                        .setEphemeral(true).queue();
            }
            return true;
        } else {
            return false;
        }

    }
}
