package fr.fonkio.inicium;

import fr.fonkio.IniciumActivity;
import fr.fonkio.command.AbstractCommand;
import fr.fonkio.command.impl.*;
import fr.fonkio.listener.impl.*;
import fr.fonkio.music.MusicManager;
import fr.fonkio.utils.Configuration;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.*;


public class Inicium {

    public static Configuration CONFIGURATION;
    private static JDA jda;
    public static Map<String, AbstractCommand> commands = new HashMap<>();

    public static MusicManager manager = new MusicManager();

    static {
        Configuration configuration = null;
        try {
            configuration = new Configuration("data.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CONFIGURATION = configuration;
    }


    public static void main(String[] args) throws LoginException {
        System.out.println("Demarrage du bot ...");

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

        Set<GatewayIntent> intents = new HashSet<>(EnumSet.allOf(GatewayIntent.class));
        jda = JDABuilder.create(CONFIGURATION.getToken(), intents).setAutoReconnect(true).build();
        CONFIGURATION.save();
        //jda.addEventListener(new IniciumListener());
        jda.addEventListener(new EventButtonInteraction());
        jda.addEventListener(new EventGuildJoin());
        jda.addEventListener(new EventGuildMemberRemoveJoin());
        jda.addEventListener(new EventGuildVoiceLeave());
        jda.addEventListener(new EventSlashCommandInteraction());
        jda.addEventListener(new EventSelectMenuInteraction());
        //jda.addEventListener(new EventMessageReceived());
        Activity act = new IniciumActivity();
        jda.getPresence().setActivity(act);

        jda.updateCommands().addCommands(
                Commands.slash("play", "Lancer la lecture d'une musique avec un lien ou une recherche")
                        .addOption(OptionType.STRING, "musique", "Lien/Recherche de la musique", true),
                Commands.slash("p", "Lancer la lecture d'une musique avec un lien ou une recherche")
                        .addOption(OptionType.STRING, "musique", "Lien/Recherche de la musique", true),
                Commands.slash("ps", "Lancer la lecture d'une musique avec un lien ou une recherche et passer à la musique en cours")
                        .addOption(OptionType.STRING, "musique", "Lien/Recherche de la musique", true),
                Commands.slash("skip", "Passer la lecture en cours pour jouer la musique suivante"),
                Commands.slash("s", "Passer la lecture en cours pour jouer la musique suivante"),
                Commands.slash("pause", "Mettre en pause la musique en cours de lecture"),
                Commands.slash("resume", "Relancer la musique mise en pause"),
                Commands.slash("seek", "Avancer / Reculer le temps de lecture de la musique en cours")
                        .addOption(OptionType.STRING, "time", "Temps sous forme HH:MM:SS ou MM:SS", true),
                Commands.slash("leave", "Déconnecter le bot du channel audio"),
                Commands.slash("quit", "Déconnecter le bot du channel audio"),
                Commands.slash("disconnect", "Déconnecter le bot du channel audio"),
                Commands.slash("dc", "Déconnecter le bot du channel audio"),
                Commands.slash("clear", "Effacer la liste des musiques en attente"),
                Commands.slash("clean", "Effacer la liste des musiques en attente"),
                Commands.slash("clr", "Effacer la liste des musiques en attente"),
                Commands.slash("queue", "Voir la liste de lecture"),
                Commands.slash("np", "Voir la liste de lecture"),
                Commands.slash("helpadmin", "Voir le commande administrateur"),
                Commands.slash("help", "Voir la liste des commandes du bot musique"),
                Commands.slash("blacklist", "Gérer la liste des channels qui interdisent l'envoi d'une commande")
                        .addOptions(
                                new OptionData(OptionType.STRING, "action", "Action que vous voulez effectuer", true)
                                .addChoice("Ajouter", "add")
                                .addChoice("Supprimer", "remove")
                                .addChoice("Lister", "list")
                        )
                        .addOption(OptionType.CHANNEL, "channel", "Channel à ajouter/supprimer de la liste"),
                Commands.slash("welcome", "Gérer le channel d'annonce des nouveaux arrivant sur le serveur"),
                Commands.slash("goodbye", "Gérer le channel d'annonce des personnes qui quittent le serveur")
                ).queue();

        System.out.println("Bot connecté");
        CONFIGURATION.save();

    }

    public static JDA getJda() {
        return jda;
    }
}
