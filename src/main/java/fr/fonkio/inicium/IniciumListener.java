package fr.fonkio.inicium;

import fr.fonkio.command.CommandsGeneral;
import fr.fonkio.command.CommandsMusic;
import fr.fonkio.message.EmbedGenerator;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.json.JSONArray;

import java.util.Timer;
import java.util.TimerTask;

public class IniciumListener implements EventListener {

    private final CommandsMusic commandsMusic;
    private final CommandsGeneral commandsGeneral;

    public IniciumListener() {
        this.commandsMusic = new CommandsMusic();
        this.commandsGeneral = new CommandsGeneral();
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
        } else if (event instanceof GuildJoinEvent) {
            onServerJoin((GuildJoinEvent)event);
        }
    }

    private void onServerJoin(GuildJoinEvent event) {
        Inicium.CONFIGURATION.setServerName(event.getGuild().getId(), event.getGuild().getName() + " admin : " + event.getGuild().getOwner().getUser().getName() + "#" + event.getGuild().getOwner().getUser().getDiscriminator());
    }

    private void onMessage(MessageReceivedEvent event) {
        User user = event.getAuthor();
        if(user.equals(event.getJDA().getSelfUser())) { //Si le message est du bot
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
                commandExec(command, event);
            } else { //Channel blacklisté
                message.addReaction("U+274C").queue();
                PrivateChannel pc = user.openPrivateChannel().complete();
                pc.sendMessage(EmbedGenerator.generate(user, "Pas de commandes dans ce channel", "Tu ne peux envoyer des commandes que dans les channels prévus à cet effet !")).queue();

                TimerTask task = new TimerTask() {
                    public void run() {
                        message.delete().queue();
                    }
                };
                Timer timer = new Timer("Timer");

                long delay = 1000L;
                timer.schedule(task, delay);
            }
        }
    }

    private void commandExec(String commandAndArgs, MessageReceivedEvent event) {
        String command = commandAndArgs.split(" ")[0];
        Guild guild = event.getGuild();
        TextChannel textChannel = event.getTextChannel();
        User user = event.getAuthor();
        Message message = event.getMessage();
        String args;
        switch (command) {
            case "play":
                args = commandAndArgs.substring(5);
                commandsMusic.playExec(guild, textChannel, user, args);
                break;
            case "p":
                args = commandAndArgs.substring(2);
                commandsMusic.playExec(guild, textChannel, user, args);
                break;
            case "ps":
                args = commandAndArgs.substring(3);
                commandsMusic.skipExec(user, guild, textChannel);
                commandsMusic.playExec(guild, textChannel, user, args);
                break;
            case "skip":
            case "s":
                commandsMusic.skipExec(user, guild, textChannel);
                break;
            case "pause":
                commandsMusic.pauseExec(user, guild, textChannel);
                break;
            case "resume":
                commandsMusic.resumeExec(user, guild, textChannel);
                break;
            case "seek":
                args = commandAndArgs.substring(5);
                commandsMusic.seekExec(user, guild, textChannel, args);
                break;
            case "leave":
            case "quit":
            case "disconnect":
                commandsMusic.disconnectExec(user, guild, textChannel);
                break;
            case "clear":
            case "clean":
            case "clr":
                commandsMusic.clearExec(user, textChannel);
                break;
            case "queue":
            case "np":
                commandsMusic.queueExec(user, guild, textChannel);
                break;
            case "helpadmin":
                commandsGeneral.helpAdmin(guild, message, user);
                break;
            case "help":
                commandsGeneral.help(guild, message, user);
                break;
            case "blacklist":
                args = commandAndArgs.substring(10);
                commandsGeneral.blacklistExec(user, guild, message, args);
                break;
            case "welcome":
                args = commandAndArgs.substring(8);
                commandsGeneral.welcomeExec(user, guild, message, args);
                break;
            case "goodbye":
                args = commandAndArgs.substring(8);
                commandsGeneral.goodbyeExec(user, guild, message, args);
                break;
            case "prefix":
                args = commandAndArgs;
                commandsGeneral.setPrefix(guild, message, user, args);
                break;
        }
    }

    private void onButtonClicked(ButtonClickEvent event) {
        User user = event.getUser();
        Guild guild = event.getGuild();
        TextChannel textChannel = event.getTextChannel();
        event.deferEdit().queue();
        switch (event.getComponentId()) {
            case "skip" :
                commandsMusic.skipExec(user, guild, textChannel);
                break;
            case "clear" :
                commandsMusic.clearExec(user, textChannel);
                break;
            case "resume" :
                commandsMusic.resumeExec(user, guild, textChannel);
                break;
            case "pause" :
                commandsMusic.pauseExec(user, guild, textChannel);
                break;
            case "disconnect" :
                commandsMusic.disconnectExec(user, guild, textChannel);
                break;
        }
    }

    private void onDisconnect(GuildVoiceLeaveEvent event) {
        checkLeaveIfChannelEmpty(event.getGuild(), event.getChannelLeft(), event.getMember().getUser());
    }

    private void onMove(GuildVoiceMoveEvent event) {
        checkLeaveIfChannelEmpty(event.getGuild(), event.getChannelLeft(), event.getMember().getUser());
    }

    private void checkLeaveIfChannelEmpty(Guild guild, VoiceChannel voiceChannel, User user) {
        if(!guild.getAudioManager().isConnected()) {
            return;
        }
        if (voiceChannel.equals(guild.getAudioManager().getConnectedChannel())) { //Si c'est le channel du bot
            if(voiceChannel.getMembers().size() == 1) {//Si il ne reste plus que le bot
                commandsMusic.disconnectQuiet(guild);
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
                    tc.sendMessage(EmbedGenerator.generate(event.getUser(), "Leave", event.getUser().getName()+" a quitté le serveur "+guild.getName()+".")).queue();
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
                    tc.sendMessage(EmbedGenerator.generate(event.getUser(), "Bienvenue !", event.getUser().getAsMention() + " a rejoint le serveur "+guild.getName()+".")).queue((message -> message.addReaction("👋").queue()));
                }
            }
        }
    }
}
