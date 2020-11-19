package fr.fonkio.inicium;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.fonkio.command.CommandMap;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.json.JSONArray;

import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class IniciumListener implements EventListener {
    private final CommandMap commandMap;
    private Inicium iniciumBot;

    public IniciumListener(CommandMap commandMap, Inicium fonkBot) {
        this.commandMap = commandMap;
        this.iniciumBot = fonkBot;
    }

    @Override
    public void onEvent(GenericEvent event) {
        if (event instanceof GuildMemberRemoveEvent) {
            onLeave((GuildMemberRemoveEvent)event);
        } else if (event instanceof GuildMemberJoinEvent) {
            onJoin((GuildMemberJoinEvent)event);
        } else if(event instanceof MessageReceivedEvent) {
            onMessage((MessageReceivedEvent)event);
        } else if (event instanceof MessageReactionAddEvent) {
            onReactionAdd((MessageReactionAddEvent)event);
        } else if (event instanceof GuildVoiceMoveEvent) {
            onMove((GuildVoiceMoveEvent)event);
        } else if (event instanceof GuildVoiceLeaveEvent) {
            onDisconnect((GuildVoiceLeaveEvent)event);
        }
    }

    private void onDisconnect(GuildVoiceLeaveEvent event) {
        leaveChannel(event.getGuild(), event.getChannelLeft(), event.getMember().getUser());

    }

    private void onMove(GuildVoiceMoveEvent event) {
        leaveChannel(event.getGuild(), event.getChannelLeft(), event.getMember().getUser());
    }

    private void leaveChannel(Guild guild, VoiceChannel voiceChannel, User user) {
        if(!guild.getAudioManager().isConnected()) {
            return;
        }
        if(voiceChannel.getMembers().size() == 1) {
            commandMap.disconnect(guild);

        }
    }

    private void onReactionAdd(MessageReactionAddEvent event) {
        User user = event.getMember().getUser();
        event.retrieveMessage().queue((message) -> {
            if (message.getAuthor().equals(iniciumBot.getJda().getSelfUser())) {
                if (!event.getMember().getUser().equals(iniciumBot.getJda().getSelfUser())) {
                    event.getReaction().removeReaction(user).queue();
                    if (event.getReactionEmote().getEmoji().equals("⏭")) {
                        commandMap.commandUser(event.getMember().getUser(), "skip", message);
                    } else if (event.getReactionEmote().getEmoji().equals("🗑")) {
                        commandMap.commandUser(event.getMember().getUser(), "clear", message);
                    } else if (event.getReactionEmote().getEmoji().equals("▶")) {
                        commandMap.commandUser(event.getMember().getUser(), "resume", message);
                    } else if (event.getReactionEmote().getEmoji().equals("⏸")) {
                        commandMap.commandUser(event.getMember().getUser(), "pause", message);
                    } else if (event.getReactionEmote().getEmoji().equals("🚪")) {
                        commandMap.commandUser(event.getMember().getUser(), "disconnect", message);
                    }
                }
            }
        });
    }
    private void onLeave(GuildMemberRemoveEvent event) {
        Guild guild = event.getGuild();
        String idTC = Inicium.CONFIGURATION.getQuit(guild.getId());
        if (!idTC.equals("")) {
            if(guild.getSelfMember().hasPermission(Permission.MESSAGE_WRITE)) {
                TextChannel tc = guild.getTextChannelById(idTC);
                if (tc != null) {
                    tc.sendMessage(event.getUser().getName()+" a quitté le serveur "+guild.getName()+".").queue();
                }
            }
        }
    }
    private void onJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        String idTC = Inicium.CONFIGURATION.getWelcome(guild.getId());
        if (!idTC.equals("")) {
            if (guild.getSelfMember().hasPermission(Permission.MESSAGE_WRITE)) {
                TextChannel tc = guild.getTextChannelById(idTC);
                if (tc != null) {
                    tc.sendMessage(event.getUser().getAsMention() + " a rejoint le serveur "+guild.getName()+".").queue();
                }
            }
        }
    }
    private void onMessage(MessageReceivedEvent event) {
        User author = event.getAuthor();
        if(author.equals(event.getJDA().getSelfUser())) {
            return;
        }
        Message message = event.getMessage();
        String command = message.getContentDisplay();
        String prefix = Inicium.CONFIGURATION.getPrefix(event.getGuild().getId());
        if(command.startsWith(prefix)) {
            JSONArray blackList = Inicium.CONFIGURATION.getBlackList(event.getGuild().getId());
            boolean channelAuthorised = true;
            for(int i = 0; i < blackList.length(); i++) {
                if (blackList.getString(i).equals(event.getTextChannel().getId())) {
                    channelAuthorised = false;
                }
            }
            if(channelAuthorised) {
                command = command.replaceFirst(prefix, "");
                commandMap.commandUser(author, command, message);

            } else {
                message.addReaction("U+274C").queue();
                PrivateChannel pc = author.openPrivateChannel().complete();
                pc.sendMessage("Tu ne peux envoyer des commandes que dans les channels prévus à cet effet !").queue();

                TimerTask task = new TimerTask() {
                    public void run() {
                        message.delete().queue();
                    }
                };
                Timer timer = new Timer("Timer");

                long delay = 5000L;
                timer.schedule(task, delay);
            }
        }

    }
    public boolean isPause(Guild guild) {
        return commandMap.isPause(guild);
    }
    public List<AudioTrack> getQueue(Guild guild) {
        return commandMap.getQueue(guild);
    }
}
