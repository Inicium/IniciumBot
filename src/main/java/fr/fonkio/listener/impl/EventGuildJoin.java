package fr.fonkio.listener.impl;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.inicium.Utils;
import fr.fonkio.enums.ConfigurationGuildEnum;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventGuildJoin extends ListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(EventGuildJoin.class);

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Member owner = event.getGuild().getOwner();
        if (owner != null) {
            String text = Utils.getFormattedLogString(
                    event.getGuild(),
                    "J'ai rejoint un nouveau serveur : Admin avatar url : " + owner.getUser().getAvatarUrl() + " Description : " + event.getGuild().getDescription() + " Icone url : " + event.getGuild().getIconUrl());
            Inicium.getJda().openPrivateChannelById("287268866147483649").complete().sendMessage(text).queue();
            Inicium.CONFIGURATION.setGuildConfig(event.getGuild().getId(), ConfigurationGuildEnum.SERVER_NAME, event.getGuild().getName() + " admin : " + owner.getUser().getName());
            logger.info(text);
        }
    }
}
