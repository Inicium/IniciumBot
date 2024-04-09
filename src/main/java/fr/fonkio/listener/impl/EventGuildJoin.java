package fr.fonkio.listener.impl;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.utils.ConfigurationEnum;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventGuildJoin extends ListenerAdapter {

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        Member owner = event.getGuild().getOwner();
        if (owner != null) {
            Inicium.getJda().openPrivateChannelById("287268866147483649").complete().sendMessage("J'ai rejoint un nouveau serveur : "+ event.getGuild().getName() + " Admin : " + owner.getUser().getName() + " Admin avatar url : " + owner.getUser().getAvatarUrl() + " Description : " + event.getGuild().getDescription() + " Icone url : " + event.getGuild().getIconUrl()).queue();
            Inicium.CONFIGURATION.setGuildConfig(event.getGuild().getId(), ConfigurationEnum.SERVER_NAME, event.getGuild().getName() + " admin : " + owner.getUser().getName());
        }
    }
}
