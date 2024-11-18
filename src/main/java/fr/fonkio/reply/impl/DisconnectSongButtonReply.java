package fr.fonkio.reply.impl;

import fr.fonkio.reply.AbstractReply;
import fr.fonkio.enums.ConfigurationGuildEnum;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class DisconnectSongButtonReply extends AbstractReply {

    @Override
    protected boolean reply(IReplyCallback event, Guild guild, User user) {
        if(!(event instanceof ButtonInteractionEvent)) {
            return false;
        }
        ButtonInteractionEvent buttonEvent = (ButtonInteractionEvent) event;

        String newParam = "false";
        if (ButtonStyle.SUCCESS.equals(buttonEvent.getComponent().getStyle())) {
            newParam = "true";
        }

        Inicium.CONFIGURATION.setGuildConfig(guild.getId(), ConfigurationGuildEnum.DC_SONG, newParam);
        buttonEvent.editMessageEmbeds(EmbedGenerator.generate(buttonEvent.getUser(), StringsConst.COMMAND_DISCONNECT_SONG_TITLE, String.format(StringsConst.BUTTON_DISCONNECT_SONG_SUCCESS,"true".equals(newParam) ? "activé" : "désactivé")))
                .setActionRow(Button.success("saved",StringsConst.BUTTON_SAVED).asDisabled())
                .queue();
        return true;
    }

    @Override
    public boolean isBlacklistable() {
        return false;
    }
}
