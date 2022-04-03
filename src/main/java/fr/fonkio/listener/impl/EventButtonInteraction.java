package fr.fonkio.listener.impl;

import fr.fonkio.inicium.Inicium;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventButtonInteraction extends ListenerAdapter {
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        User user = event.getUser();
        Guild guild = event.getGuild();
        if (guild != null) {
            Member member = guild.getMember(user);
            if (member != null) {
                GuildVoiceState guildVoiceState = member.getVoiceState();
                if (guildVoiceState != null) {
                    event.deferEdit().queue();
                    Inicium.commands.get(event.getComponentId()).run(null, event);
                }
            }
        }
    }
}
