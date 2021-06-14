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
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.json.JSONArray;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class IniciumListener implements EventListener {

    public CommandMap getCommandMap() {
        return commandMap;
    }

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
        } else if (event instanceof GuildVoiceMoveEvent) {
            onMove((GuildVoiceMoveEvent)event);
        } else if (event instanceof GuildVoiceLeaveEvent) {
            onDisconnect((GuildVoiceLeaveEvent)event);
        } else if (event instanceof ButtonClickEvent) {
            onButtonClicked((ButtonClickEvent)event);
        }
    }

    private void onButtonClicked(ButtonClickEvent event) {
        switch (event.getComponentId()) {
            case "skip" :
                commandMap.commandUser(event.getMember().getUser(), "skip", event.getMessage());
                break;
            case "clear" :
                commandMap.commandUser(event.getMember().getUser(), "clear", event.getMessage());
                break;
            case "resume" :
                commandMap.commandUser(event.getMember().getUser(), "resume", event.getMessage());
                break;
            case "pause" :
                commandMap.commandUser(event.getMember().getUser(), "pause", event.getMessage());
                break;
            case "disconnect" :
                commandMap.commandUser(event.getMember().getUser(), "disconnect", event.getMessage());
                break;
        }
        event.deferEdit().queue();
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
        if (voiceChannel.equals(guild.getAudioManager().getConnectedChannel())) { //Si c'est le channel du bot
            if(voiceChannel.getMembers().size() == 1) {//Si il ne reste plus que le bot
                commandMap.disconnect(guild);
            }
        }

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
                    tc.sendMessage(event.getUser().getAsMention() + " a rejoint le serveur "+guild.getName()+".").queue((message -> message.addReaction("👋").queue()));
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
