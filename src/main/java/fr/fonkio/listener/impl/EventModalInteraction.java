package fr.fonkio.listener.impl;

import fr.fonkio.enums.InterractionIdEnum;
import fr.fonkio.inicium.Inicium;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventModalInteraction extends ListenerAdapter {
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        Inicium.replies.get(InterractionIdEnum.getInterractionIdEnum(event.getModalId())).execute(event);
    }
}
