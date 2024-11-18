package fr.fonkio.listener.impl;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.enums.InterractionIdEnum;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventSelectMenuInteraction extends ListenerAdapter {

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        Inicium.replies.get(InterractionIdEnum.getInterractionIdEnum(event.getComponentId())).execute(event);
    }

}
