package fr.fonkio.listener.impl;

import fr.fonkio.inicium.Inicium;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventButtonInteraction extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        User user = event.getUser();
        Guild guild = event.getGuild();
        if (guild == null) {
            return;
        }
        Member member = guild.getMember(user);
        if (member == null) {
            return;
        }
        GuildVoiceState guildVoiceState = member.getVoiceState();
        if (guildVoiceState != null) {
            Inicium.commands.get(event.getComponentId()).execute(null, event);
        }
    }
}
