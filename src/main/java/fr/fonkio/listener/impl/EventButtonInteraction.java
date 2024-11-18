package fr.fonkio.listener.impl;

import fr.fonkio.enums.InterractionIdEnum;
import fr.fonkio.inicium.Inicium;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventButtonInteraction extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        Inicium.replies.get(InterractionIdEnum.getInterractionIdEnum(event.getComponentId())).execute(event);
    }
}
