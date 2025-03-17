package fr.fonkio.listener.impl;

import fr.fonkio.inicium.Inicium;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildMuteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventGuildVoiceGuild extends ListenerAdapter {
    @Override
    public void onGuildVoiceGuildMute(@NotNull GuildVoiceGuildMuteEvent event) {
        if (event.isGuildMuted()) {
            Member member = event.getMember();
            User user = member.getUser();
            if (Inicium.protectedUserSet.contains(user)) {
                event.getGuild().mute(member, false).queue();
            }
        }
    }

    @Override
    public void onGuildVoiceGuildDeafen(@NotNull GuildVoiceGuildDeafenEvent event) {
        if (event.isGuildDeafened()) {
            Member member = event.getMember();
            User user = member.getUser();
            if (Inicium.protectedUserSet.contains(user)) {
                event.getGuild().deafen(member, false).queue();
            }
        }
    }
}
