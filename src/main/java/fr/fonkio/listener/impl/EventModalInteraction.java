package fr.fonkio.listener.impl;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

public class EventModalInteraction extends ListenerAdapter {
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) {
            return;
        }
        String url = "";
        String label = "";
        for(ModalMapping modalMapping : event.getValues()) {
            switch (modalMapping.getId()) {
                case "url":
                    url = modalMapping.getAsString();
                    break;
                case "label":
                    label = modalMapping.getAsString();
                    break;
            }
        }
        try {
            Inicium.CONFIGURATION.addToPlaylist(guild.getId(), url, label);
            event.replyEmbeds(EmbedGenerator.generate(event.getUser(), StringsConst.COMMAND_PLAYLIST_TITLE, String.format(StringsConst.COMMAND_PLAYLIST_ADD_SUCCESS, label))).setEphemeral(true).queue();
        } catch (IllegalStateException exception) {
            event.replyEmbeds(EmbedGenerator.generate(event.getUser(), StringsConst.COMMAND_PLAYLIST_TITLE, exception.getMessage())).setEphemeral(true).queue();
        }
    }
}
