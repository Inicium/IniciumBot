package fr.fonkio.inicium;

import dev.lavalink.youtube.clients.Web;
import fr.fonkio.IniciumActivity;
import fr.fonkio.command.AbstractCommand;
import fr.fonkio.command.impl.*;
import fr.fonkio.listener.impl.*;
import fr.fonkio.message.StringsConst;
import fr.fonkio.music.MusicManager;
import fr.fonkio.utils.Configuration;
import fr.fonkio.enums.ConfigurationBotEnum;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.*;


public class Inicium {
    static Logger logger = LoggerFactory.getLogger(Inicium.class);

    public static Configuration CONFIGURATION;
    private static JDA jda;
    public static Map<String, AbstractCommand> commands = new HashMap<>();

    public static MusicManager manager = new MusicManager();

    static {
        Configuration configuration = null;
        try {
            configuration = new Configuration("data.json");
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        CONFIGURATION = configuration;
    }

    public static void main(String[] args) {
        logger.info("Démarrage du bot ...");
        logger.info("Ajout du PO_TOKEN et du VISITOR_DATA ...");
        Web.setPoTokenAndVisitorData(Inicium.CONFIGURATION.getGlobalParam(ConfigurationBotEnum.PO_TOKEN),
                Inicium.CONFIGURATION.getGlobalParam(ConfigurationBotEnum.VISITOR_DATA));
        logger.info("Création des commandes...");
        AbstractCommand clear = new CommandClear();
        AbstractCommand disconnect = new CommandDisconnect();
        AbstractCommand pause = new CommandPause();
        AbstractCommand play = new CommandPlay();
        AbstractCommand playSkip = new CommandPlaySkip();
        AbstractCommand queue = new CommandQueue();
        AbstractCommand resume = new CommandResume();
        AbstractCommand seek = new CommandSeek();
        AbstractCommand skip = new CommandSkip();
        AbstractCommand helpadmin = new CommandHelpadmin();
        AbstractCommand help = new CommandHelp();
        AbstractCommand blacklist = new CommandBlacklist();
        AbstractCommand welcome = new CommandWelcomeChannel();
        AbstractCommand goodbye = new CommandGoodbyeChannel();
        AbstractCommand moveall = new CommandMoveAll();
        AbstractCommand shuffle = new CommandShuffle();
        AbstractCommand defaultRole = new CommandDefaultRole();
        AbstractCommand disconnectsong = new CommandDisconnectSong();
        AbstractCommand playlist = new CommandPlaylist();

        commands.put("play", play);
        commands.put("p", play);
        commands.put("ps", playSkip);
        commands.put("skip", skip);
        commands.put("s", skip);
        commands.put("pause", pause);
        commands.put("resume", resume);
        commands.put("seek", seek);
        commands.put("leave", disconnect);
        commands.put("quit", disconnect);
        commands.put("disconnect", disconnect);
        commands.put("dc", disconnect);
        commands.put("clear", clear);
        commands.put("clean", clear);
        commands.put("clr", clear);
        commands.put("queue", queue);
        commands.put("np", queue);
        commands.put("helpadmin", helpadmin);
        commands.put("help", help);
        commands.put("blacklist", blacklist);
        commands.put("welcome", welcome);
        commands.put("goodbye", goodbye);
        commands.put("moveall", moveall);
        commands.put("mva", moveall);
        commands.put("shuffle", shuffle);
        commands.put("defaultrole", defaultRole);
        commands.put("disconnectsong", disconnectsong);
        commands.put("dcsong", disconnectsong);
        commands.put("playlist", playlist);
        logger.info("Démarrage JDA...");
        Set<GatewayIntent> intents = new HashSet<>(EnumSet.allOf(GatewayIntent.class));
        jda = JDABuilder.create(CONFIGURATION.getGlobalParam(ConfigurationBotEnum.DISCORD_TOKEN), intents).setAutoReconnect(true).build();
        logger.info("JDA : Démarré ! Connexion au token OK.");
        CONFIGURATION.save();
        logger.info("JDA : Enregistrement des listeners...");
        jda.addEventListener(new EventButtonInteraction());
        jda.addEventListener(new EventGuildJoin());
        jda.addEventListener(new EventGuildMemberRemoveJoin());
        jda.addEventListener(new EventGuildVoiceLeave());
        jda.addEventListener(new EventSlashCommandInteraction());
        jda.addEventListener(new EventSelectMenuInteraction());
        jda.addEventListener(new EventModalInteraction());
        logger.info("JDA : Lancement de l'activité Discord...");
        Activity act = new IniciumActivity();
        jda.getPresence().setActivity(act);
        logger.info("JDA : Mise à jour des commandes...");
        OptionData favoriteOptionData = new OptionData(OptionType.STRING, "action", StringsConst.COMMAND_PLAYLIST_PARAM, false)
                .addChoices(new Command.Choice("remove", "remove"), new Command.Choice("add", "add"));
        jda.updateCommands().addCommands(
                Commands.slash("play", StringsConst.COMMAND_PLAY_DESC)
                        .addOption(OptionType.STRING, "musique", StringsConst.COMMAND_PLAY_PARAM, true),
                Commands.slash("p", StringsConst.COMMAND_PLAY_DESC)
                        .addOption(OptionType.STRING, "musique", StringsConst.COMMAND_PLAY_PARAM, true),
                Commands.slash("ps", StringsConst.COMMAND_PLAYSKIP_DESC)
                        .addOption(OptionType.STRING, "musique", StringsConst.COMMAND_PLAY_PARAM, true),
                Commands.slash("skip", StringsConst.COMMAND_SKIP_DESC),
                Commands.slash("s", StringsConst.COMMAND_SKIP_DESC),
                Commands.slash("pause", StringsConst.COMMAND_PAUSE_DESC),
                Commands.slash("resume", StringsConst.COMMAND_RESUME_DESC),
                Commands.slash("seek", StringsConst.COMMAND_SEEK_DESC)
                        .addOption(OptionType.STRING, "time", StringsConst.COMMAND_SEEK_PARAM, true),
                Commands.slash("leave", StringsConst.COMMAND_DISCONNECT_DESC),
                Commands.slash("quit", StringsConst.COMMAND_DISCONNECT_DESC),
                Commands.slash("disconnect", StringsConst.COMMAND_DISCONNECT_DESC),
                Commands.slash("dc", StringsConst.COMMAND_DISCONNECT_DESC),
                Commands.slash("clear", StringsConst.COMMAND_CLEAR_DESC),
                Commands.slash("clean", StringsConst.COMMAND_CLEAR_DESC),
                Commands.slash("clr", StringsConst.COMMAND_CLEAR_DESC),
                Commands.slash("queue", StringsConst.COMMAND_QUEUE_DESC),
                Commands.slash("np", StringsConst.COMMAND_QUEUE_DESC),
                Commands.slash("helpadmin", StringsConst.COMMAND_HELPADMIN_DESC),
                Commands.slash("help", StringsConst.COMMAND_HELP_DESC),
                Commands.slash("blacklist", StringsConst.COMMAND_BLACKLIST_DESC),
                Commands.slash("welcome", StringsConst.COMMAND_WELCOME_DESC),
                Commands.slash("goodbye", StringsConst.COMMAND_GOODBYE_DESC),
                Commands.slash("disconnectsong", StringsConst.COMMAND_DISCONNECT_SONG_DESC),
                Commands.slash("dcsong", StringsConst.COMMAND_DISCONNECT_SONG_DESC),
                Commands.slash("playlist", StringsConst.COMMAND_PLAYLIST_DESC)
                        .addOptions(favoriteOptionData),
                Commands.slash("mva", StringsConst.COMMAND_MOVEALL_DESC)
                        .addOptions(new OptionData(OptionType.CHANNEL, "destination", StringsConst.COMMAND_MOVEALL_PARAM, true)
                                .setChannelTypes(ChannelType.VOICE, ChannelType.STAGE)),
                Commands.slash("moveall", StringsConst.COMMAND_MOVEALL_DESC)
                        .addOptions(new OptionData(OptionType.CHANNEL, "destination", StringsConst.COMMAND_MOVEALL_PARAM, true).setChannelTypes(ChannelType.VOICE, ChannelType.STAGE)),
                Commands.slash("shuffle", StringsConst.COMMAND_SHUFFLE_DESC),
                Commands.slash("defaultrole", StringsConst.COMMAND_DEFAULT_ROLE_DESC)
                ).queue();
        logger.info("Bot connecté");
        CONFIGURATION.save();
    }

    public static JDA getJda() {
        return jda;
    }
}
