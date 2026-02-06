package fr.fonkio.inicium;

import club.minnced.discord.jdave.interop.JDaveSessionFactory;
import dev.lavalink.youtube.clients.Web;
import fr.fonkio.IniciumActivity;
import fr.fonkio.reply.AbstractReply;
import fr.fonkio.reply.impl.*;
import fr.fonkio.enums.InterractionIdEnum;
import fr.fonkio.listener.impl.*;
import fr.fonkio.message.StringsConst;
import fr.fonkio.music.MusicManager;
import fr.fonkio.utils.Configuration;
import fr.fonkio.enums.ConfigurationBotEnum;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.audio.AudioModuleConfig;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
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
    public static Map<InterractionIdEnum, AbstractReply> replies = new HashMap<>();
    public static Set<User> protectedUserSet = new HashSet<>();

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

        logger.info("Création des replies...");
        AbstractReply clearReply = new ClearReply();
        AbstractReply disconnectReply = new DisconnectReply();
        AbstractReply pauseReply = new PauseReply();
        AbstractReply playReply = new PlayReply();
        AbstractReply playSkipReply = new PlaySkipReply();
        AbstractReply queueReply = new QueueReply();
        AbstractReply resumeReply = new ResumeReply();
        AbstractReply seekReply = new SeekReply();
        AbstractReply skipReply = new SkipReply();
        AbstractReply helpAdminReply = new HelpAdminReply();
        AbstractReply helpReply = new HelpReply();
        AbstractReply blacklistReply = new BlacklistReply();
        AbstractReply blacklistSelectMenuReply = new BlacklistStringSelectMenuReply();
        AbstractReply welcomeReply = new WelcomeReply();
        AbstractReply welcomeStringSelectMenuReply = new WelcomeStringSelectMenuReply();
        AbstractReply goodbyeReply = new GoodbyeReply();
        AbstractReply goodbyeStringSelectMenuReply = new GoodbyeStringSelectMenuReply();
        AbstractReply moveAllReply = new MoveAllReply();
        AbstractReply shuffleReply = new ShuffleReply();
        AbstractReply defaultRoleReply = new DefaultRoleReply();
        AbstractReply defaultRoleStringSelectMenuReply = new DefaultRoleStringSelectMenuReply();
        AbstractReply disconnectSongReply = new DisconnectSongReply();
        AbstractReply disconnectSongButtonReply = new DisconnectSongButtonReply();
        AbstractReply playlistReply = new PlaylistReply();
        AbstractReply playlistStringSelectMenuReply = new PlaylistStringSelectMenuReply();
        AbstractReply playlistAddReply = new PlaylistAddReply();
        AbstractReply playlistAddModal = new PlaylistAddModalReply();
        AbstractReply playlistRemoveReply = new PlaylistRemoveReply();
        AbstractReply playlistRemoveStringSelectMenuReply = new PlaylistRemoveStringSelectMenuReply();
        AbstractReply protectMeReply = new ProtectMeReply();

        replies.put(InterractionIdEnum.PLAY, playReply);
        replies.put(InterractionIdEnum.SKIP, skipReply);
        replies.put(InterractionIdEnum.PLAY_SKIP, playSkipReply);
        replies.put(InterractionIdEnum.PAUSE, pauseReply);
        replies.put(InterractionIdEnum.RESUME, resumeReply);
        replies.put(InterractionIdEnum.SEEK, seekReply);
        replies.put(InterractionIdEnum.DISCONNECT, disconnectReply);
        replies.put(InterractionIdEnum.CLEAR, clearReply);
        replies.put(InterractionIdEnum.QUEUE, queueReply);
        replies.put(InterractionIdEnum.HELP_ADMIN, helpAdminReply);
        replies.put(InterractionIdEnum.HELP, helpReply);
        replies.put(InterractionIdEnum.BLACKLIST, blacklistReply);
        replies.put(InterractionIdEnum.BLACKLIST_SELECT_MENU, blacklistSelectMenuReply);
        replies.put(InterractionIdEnum.WELCOME, welcomeReply);
        replies.put(InterractionIdEnum.WELCOME_SELECT_MENU, welcomeStringSelectMenuReply);
        replies.put(InterractionIdEnum.GOODBYE, goodbyeReply);
        replies.put(InterractionIdEnum.GOODBYE_SELECT_MENU, goodbyeStringSelectMenuReply);
        replies.put(InterractionIdEnum.MOVE_ALL, moveAllReply);
        replies.put(InterractionIdEnum.SHUFFLE, shuffleReply);
        replies.put(InterractionIdEnum.DEFAULT_ROLE, defaultRoleReply);
        replies.put(InterractionIdEnum.DEFAULT_ROLE_SELECT_MENU, defaultRoleStringSelectMenuReply);
        replies.put(InterractionIdEnum.DISCONNECT_SONG, disconnectSongReply);
        replies.put(InterractionIdEnum.DISCONNECT_SONG_BUTTON, disconnectSongButtonReply);
        replies.put(InterractionIdEnum.PLAYLIST, playlistReply);
        replies.put(InterractionIdEnum.PLAYLIST_SELECT_MENU, playlistStringSelectMenuReply);
        replies.put(InterractionIdEnum.PLAYLIST_ADD, playlistAddReply);
        replies.put(InterractionIdEnum.PLAYLIST_ADD_MODAL, playlistAddModal);
        replies.put(InterractionIdEnum.PLAYLIST_REMOVE, playlistRemoveReply);
        replies.put(InterractionIdEnum.PLAYLIST_REMOVE_SELECT_MENU, playlistRemoveStringSelectMenuReply);
        replies.put(InterractionIdEnum.PROTECTME, protectMeReply);

        logger.info("Démarrage JDA...");
        Set<GatewayIntent> intents = new HashSet<>(EnumSet.allOf(GatewayIntent.class));
        jda = JDABuilder.create(CONFIGURATION.getGlobalParam(ConfigurationBotEnum.DISCORD_TOKEN), intents)
                .setAutoReconnect(true)
                .setAudioModuleConfig(
                        new AudioModuleConfig().withDaveSessionFactory(new JDaveSessionFactory())
                )
                .build();
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
        jda.addEventListener(new EventGuildVoiceGuild());

        logger.info("JDA : Lancement de l'activité Discord...");
        Activity act = new IniciumActivity();
        jda.getPresence().setActivity(act);

        logger.info("JDA : Mise à jour des commandes...");
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
                Commands.slash("playlist", StringsConst.COMMAND_PLAYLIST_DESC),
                Commands.slash("mva", StringsConst.COMMAND_MOVEALL_DESC)
                        .addOptions(new OptionData(OptionType.CHANNEL, "destination", StringsConst.COMMAND_MOVEALL_PARAM, true)
                                .setChannelTypes(ChannelType.VOICE, ChannelType.STAGE)),
                Commands.slash("moveall", StringsConst.COMMAND_MOVEALL_DESC)
                        .addOptions(new OptionData(OptionType.CHANNEL, "destination", StringsConst.COMMAND_MOVEALL_PARAM, true).setChannelTypes(ChannelType.VOICE, ChannelType.STAGE)),
                Commands.slash("shuffle", StringsConst.COMMAND_SHUFFLE_DESC),
                Commands.slash("defaultrole", StringsConst.COMMAND_DEFAULT_ROLE_DESC),
                Commands.slash("protectme", StringsConst.COMMAND_PROTECTME_DESC)
                ).queue();

        logger.info("Bot connecté");
        CONFIGURATION.save();
    }

    public static JDA getJda() {
        return jda;
    }
}
