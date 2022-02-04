package fr.fonkio.inicium;

import fr.fonkio.command.GeneralCommands;
import fr.fonkio.command.CommandsMusic;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.utils.ConfigurationEnum;
import net.dv8tion.jda.api.OnlineStatus;
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
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class IniciumListener implements EventListener {

    private final CommandsMusic commandsMusic;
    private final GeneralCommands generalCommands;

    public IniciumListener() {
        this.commandsMusic = new CommandsMusic();
        this.generalCommands = new GeneralCommands();
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
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
        } else if (event instanceof UserUpdateOnlineStatusEvent) {
            onUserStatusChange((UserUpdateOnlineStatusEvent)event);
        }
    }

    private Map<Guild, Map<Member, VoiceChannel>> ancienChannel = new HashMap<>();

    private void onUserStatusChange(UserUpdateOnlineStatusEvent event) {
        Guild guild = event.getGuild();
        if("false".equals(Inicium.CONFIGURATION.getGuildConfig(guild.getId(), ConfigurationEnum.MOVE_AFK))) {
            return;
        }
        Member member = event.getMember();
        VoiceChannel afkChannel = guild.getAfkChannel();
        if (afkChannel != null) {
            switch (event.getNewValue()) {
                case IDLE:
                    if (!ancienChannel.containsKey(guild)) {
                        ancienChannel.put(guild, new HashMap<>());
                    }
                    Map<Member, VoiceChannel> ancienChannelGuild = ancienChannel.get(guild);
                    GuildVoiceState guildVoiceState = member.getVoiceState();
                    if (guildVoiceState != null) {
                        ancienChannelGuild.put(member, guildVoiceState.getChannel());
                        guild.moveVoiceMember(member, afkChannel).queue();
                    }
                    break;
                case ONLINE:
                    if (event.getOldValue().equals(OnlineStatus.IDLE)) {
                        if (ancienChannel.containsKey(guild)) {
                            Map<Member, VoiceChannel> ancienChannelGuild1 = ancienChannel.get(guild);
                            GuildVoiceState guildVoiceState1 = member.getVoiceState();
                            if (guildVoiceState1 != null) {
                                VoiceChannel voiceChannel = guildVoiceState1.getChannel();
                                if (voiceChannel != null && voiceChannel.equals(afkChannel)) {
                                    guild.moveVoiceMember(member, ancienChannelGuild1.get(member)).queue();
                                }
                            }
                        }
                    }
                    break;
            }


        }

    }

    private void onServerJoin(GuildJoinEvent event) {
        Member owner = event.getGuild().getOwner();
        if (owner != null) {
            Inicium.CONFIGURATION.setGuildConfig(event.getGuild().getId(), ConfigurationEnum.SERVER_NAME, event.getGuild().getName() + " admin : " + owner.getUser().getName() + "#" + event.getGuild().getOwner().getUser().getDiscriminator());
        }
    }

    private void onMessage(MessageReceivedEvent event) {
        User user = event.getAuthor();
        if(user.equals(event.getJDA().getSelfUser())) { //Si le message est du bot
            return;
        }
        Message message = event.getMessage();
        String command = message.getContentDisplay();
        String prefix = Inicium.CONFIGURATION.getGuildConfig(event.getGuild().getId(), ConfigurationEnum.PREFIX_COMMAND);

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
                pc.sendMessageEmbeds(EmbedGenerator.generate(user, "Pas de commandes dans ce channel", "Tu ne peux envoyer des commandes que dans les channels prévus à cet effet !")).queue();

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
        Member member = guild.getMember(user);
        if (member == null) {
            return;
        }
        GuildVoiceState guildVoiceState = member.getVoiceState();
        if (guildVoiceState == null) {
            return;
        }
        VoiceChannel voiceChannel = member.getVoiceState().getChannel();

        String args;
        switch (command) {
            case "play":
                if (canSendCommand(voiceChannel, guild, user, message)) {
                    args = commandAndArgs.substring(5);
                    commandsMusic.playExec(guild, textChannel, user, args);
                }
                break;
            case "p":
                if (canSendCommand(voiceChannel, guild, user, message)) {
                    args = commandAndArgs.substring(2);
                    commandsMusic.playExec(guild, textChannel, user, args);
                }
                break;
            case "ps":
                if (canSendCommand(voiceChannel, guild, user, message)) {
                    args = commandAndArgs.substring(3);
                    commandsMusic.skipExec(user, guild, textChannel);
                    commandsMusic.playExec(guild, textChannel, user, args);
                }
                break;
            case "skip":
            case "s":
                if (canSendCommand(voiceChannel, guild, user, message)) {
                    commandsMusic.skipExec(user, guild, textChannel);
                }
                break;
            case "pause":
                if (canSendCommand(voiceChannel, guild, user, message)) {
                    commandsMusic.pauseExec(user, guild, textChannel);
                }
                break;
            case "resume":
                if (canSendCommand(voiceChannel, guild, user, message)) {
                    commandsMusic.resumeExec(user, guild, textChannel);
                }
                break;
            case "seek":
                if (canSendCommand(voiceChannel, guild, user, message)) {
                    args = commandAndArgs.substring(5);
                    commandsMusic.seekExec(user, guild, textChannel, args);
                }
                break;
            case "leave":
            case "quit":
            case "disconnect":
                if (canSendCommand(voiceChannel, guild, user, message)) {
                    commandsMusic.disconnectExec(user, guild, textChannel);
                }
                break;
            case "clear":
            case "clean":
            case "clr":
                if (canSendCommand(voiceChannel, guild, user, message)) {
                    commandsMusic.clearExec(user, textChannel);
                }
                break;
            case "queue":
            case "np":
                commandsMusic.queueExec(user, guild, textChannel);
                break;
            case "helpadmin":
                generalCommands.helpAdmin(guild, message, user);
                break;
            case "help":
                generalCommands.help(guild, message, user);
                break;
            case "blacklist":
                args = commandAndArgs.substring(10);
                generalCommands.blacklistExec(user, guild, message, args);
                break;
            case "welcome":
                args = commandAndArgs.substring(8);
                generalCommands.welcomeExec(user, guild, message, args);
                break;
            case "goodbye":
                args = commandAndArgs.substring(8);
                generalCommands.goodbyeExec(user, guild, message, args);
                break;
            case "prefix":
                args = commandAndArgs;
                generalCommands.setPrefix(guild, message, user, args);
                break;
        }
    }

    private boolean canSendCommand(VoiceChannel userVoiceChannel, Guild guild, User user, Message message) {
        Member self = guild.getMember(Inicium.getJda().getSelfUser());
        if (self != null) {
            GuildVoiceState guildVoiceState = self.getVoiceState();
            if (guildVoiceState != null) {
                VoiceChannel botVoiceChannel = guildVoiceState.getChannel();
                MessageEmbed messageEmbed = null;

                if (userVoiceChannel == null) {
                    messageEmbed = EmbedGenerator.generate(user, "Tu dois être connecté", "Tu peux envoyer des commandes uniquement si tu es connecté !");
                } else {
                    if (botVoiceChannel == null) {
                        return true;
                    }
                    String idBotVoiceChannel = botVoiceChannel.getId();
                    if (!userVoiceChannel.getId().equals(idBotVoiceChannel)) {
                        messageEmbed = EmbedGenerator.generate(user, "Bot occupé", "Le bot est déjà connecté dans un autre channel !");
                    }
                }
                if (messageEmbed != null) { // Une des condition n'a pas été validée
                    //Suppression du message avec anim réaction + envoi de la raison en PM
                    if (message != null)  {
                        message.addReaction("U+274C").queue();
                        TimerTask task = new TimerTask() {
                            public void run() {
                                message.delete().queue();
                            }
                        };
                        Timer timer = new Timer("Timer");
                        long delay = 1000L;
                        timer.schedule(task, delay);
                    }
                    PrivateChannel pc = user.openPrivateChannel().complete();
                    pc.sendMessageEmbeds(messageEmbed).queue();
                }
            }
        }
        return false;
    }

    private void onButtonClicked(ButtonClickEvent event) {
        User user = event.getUser();
        Guild guild = event.getGuild();
        TextChannel textChannel = event.getTextChannel();
        if (guild != null) {
            Member member = guild.getMember(user);
            if (member != null) {
                GuildVoiceState guildVoiceState = member.getVoiceState();
                if (guildVoiceState != null) {
                    VoiceChannel voiceChannel = guildVoiceState.getChannel();
                    if (!canSendCommand(voiceChannel, guild, user, null)) {
                        return;
                    } else {
                        event.deferEdit().queue();
                    }
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
            }
        }
    }

    private void onDisconnect(GuildVoiceLeaveEvent event) {
        checkLeaveIfChannelEmpty(event.getGuild(), event.getChannelLeft());
    }

    private void onMove(GuildVoiceMoveEvent event) {
        checkLeaveIfChannelEmpty(event.getGuild(), event.getChannelLeft());
    }

    private void checkLeaveIfChannelEmpty(Guild guild, VoiceChannel voiceChannel) {
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
        String idTC = Inicium.CONFIGURATION.getGuildConfig(guild.getId(), ConfigurationEnum.QUIT_CHANNEL);
        if (!idTC.equals("")) {
            if(guild.getSelfMember().hasPermission(Permission.MESSAGE_WRITE)) {
                TextChannel tc = guild.getTextChannelById(idTC);
                if (tc != null) {
                    tc.sendMessageEmbeds(EmbedGenerator.generate(event.getUser(), "Leave", event.getUser().getName()+" a quitté le serveur "+guild.getName()+".")).queue();
                }
            }
        }
    }
    private void onJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        String idTC = Inicium.CONFIGURATION.getGuildConfig(guild.getId(), ConfigurationEnum.WELCOME_CHANNEL);
        if (!idTC.equals("")) {
            if (guild.getSelfMember().hasPermission(Permission.MESSAGE_WRITE)) {
                TextChannel tc = guild.getTextChannelById(idTC);
                if (tc != null) {
                    tc.sendMessageEmbeds(EmbedGenerator.generate(event.getUser(), "Bienvenue !", event.getUser().getAsMention() + " a rejoint le serveur "+guild.getName()+".")).queue((message -> message.addReaction("👋").queue()));
                }
            }
        }
    }
}
