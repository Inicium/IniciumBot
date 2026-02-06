package fr.fonkio.reply.impl;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import fr.fonkio.reply.AbstractStringSelectMenuReply;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class BlacklistStringSelectMenuReply extends AbstractStringSelectMenuReply {

    @Override
    protected boolean reply(IReplyCallback event, Guild guild, User user) {
        if (!(event instanceof StringSelectInteractionEvent)) {
            return false;
        }
        StringSelectInteractionEvent stringSelectEvent = (StringSelectInteractionEvent) event;
        Inicium.CONFIGURATION.delBlacklist(guild.getId());
        if (!stringSelectEvent.getValues().isEmpty()) {
            Inicium.CONFIGURATION.addBlackList(guild.getId(), stringSelectEvent.getValues());
        }
        stringSelectEvent.editMessageEmbeds(EmbedGenerator.generate(event.getUser(), StringsConst.COMMAND_BLACKLIST_TITLE, StringsConst.SELECT_BLACKLIST_SUCCESS))
                .setComponents(ActionRow.of(getButtonSaved()))
                .queue();
        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return false;
    }
}
