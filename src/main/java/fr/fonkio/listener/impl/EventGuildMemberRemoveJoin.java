package fr.fonkio.listener.impl;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.utils.ConfigurationEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventGuildMemberRemoveJoin extends ListenerAdapter {
    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        Guild guild = event.getGuild();
        User user = event.getUser();
        sendWelcomeQuitMessage(ConfigurationEnum.QUIT_CHANNEL, StringsConst.MESSAGE_GOODBYE_TITLE, user.getName() + StringsConst.MESSAGE_GOODBYE, guild, user);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        User user = event.getUser();
        sendWelcomeQuitMessage(ConfigurationEnum.WELCOME_CHANNEL, StringsConst.MESSAGE_WELCOME_TITLE, user.getAsMention() + StringsConst.MESSAGE_WELCOME +guild.getName()+".", guild, user);
        String roleId = Inicium.CONFIGURATION.getGuildConfig(guild.getId(), ConfigurationEnum.DEFAULT_ROLE);
        if (!"".equals(roleId)) {
            Role role = guild.getRoleById(roleId);
            if (role != null) {
                guild.addRoleToMember(user, role).queue();
            }
        }

    }

    private void sendWelcomeQuitMessage(ConfigurationEnum configChannel, String title, String message, Guild guild, User user) {
        String idTC = Inicium.CONFIGURATION.getGuildConfig(guild.getId(), configChannel);
        if (!idTC.equals("")) {
            if (guild.getSelfMember().hasPermission(Permission.MESSAGE_SEND)) {
                TextChannel tc = guild.getTextChannelById(idTC);
                if (tc != null) {
                    tc.sendMessageEmbeds(EmbedGenerator.generate(user, title, message, guild)).queue((messageSent -> messageSent.addReaction(Emoji.fromUnicode("👋")).queue()));
                }
            }
        }
    }
}
