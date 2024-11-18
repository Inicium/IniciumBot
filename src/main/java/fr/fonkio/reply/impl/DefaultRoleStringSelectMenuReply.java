package fr.fonkio.reply.impl;

import fr.fonkio.enums.ConfigurationGuildEnum;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.reply.AbstractStringSelectMenuReply;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class DefaultRoleStringSelectMenuReply extends AbstractStringSelectMenuReply {

    @Override
    protected boolean reply(IReplyCallback event, Guild guild, User user) {
        if (!(event instanceof StringSelectInteractionEvent)) {
            return false;
        }
        StringSelectInteractionEvent stringSelectEvent = (StringSelectInteractionEvent) event;
        Inicium.CONFIGURATION.setGuildConfig(guild.getId(), ConfigurationGuildEnum.DEFAULT_ROLE, stringSelectEvent.getValues().isEmpty() ? "" : stringSelectEvent.getValues().get(0));
        stringSelectEvent.editMessageEmbeds(EmbedGenerator.generate(event.getUser(), StringsConst.COMMAND_DEFAULT_ROLE_TITLE, StringsConst.SELECT_DEFAULT_ROLE_SUCCESS))
                .setActionRow(getButtonSaved())
                .queue();
        return false;
    }

    @Override
    public boolean isBlacklistable() {
        return false;
    }
}
