package fr.fonkio.command;

import fr.fonkio.inicium.Inicium;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONArray;

import java.awt.*;

public class CommandGeneral {

    private Inicium iniciumBot;

    public CommandGeneral(Inicium iniciumBot) {
        this.iniciumBot = iniciumBot;
    }


    @Command(name = "stop", type = Command.ExecutorType.CONSOLE)
    private void stop() {
        iniciumBot.setRunning(false);
    }


    @Command(name="blacklist-add", type= Command.ExecutorType.USER)
    private void blacklistAdd(Guild guild, TextChannel textChannel, User user, String command) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            textChannel.sendMessage("Vous n'êtes pas administrateur de ce serveur").queue();
            return;
        }
        JSONArray blackList = Inicium.CONFIGURATION.getBlackList(guild.getId());
        String id = command.replaceFirst("blacklist-add ", "");
        TextChannel tc = guild.getTextChannelById(id);
        if (tc == null) {
            textChannel.sendMessage("Channel introuvable ! Vous devez entrer l'ID du channel :\nhttps://support.discord.com/hc/fr/articles/206346498-Où-trouver-l-ID-de-mon-compte-utilisateur-serveur-message-").queue();
            return;
        }
        boolean trouve = false;
        for(int i = 0; i < blackList.length(); i++) {
            if (blackList.getString(i).equals(id)) {
                trouve = true;
            }
        }
        if (trouve) {
            textChannel.sendMessage(tc.getName()+" est déjà dans la blacklist !").queue();
            return;
        }
        Inicium.CONFIGURATION.addBlackList(guild.getId(), id);
        textChannel.sendMessage(tc.getName() + " a été ajouté à la blacklist !").queue();
    }


    @Command(name="blacklist-remove", type= Command.ExecutorType.USER)
    private void blacklistRemove(Guild guild, TextChannel textChannel, User user, String command) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            textChannel.sendMessage("Vous n'êtes pas administrateur de ce serveur").queue();
            return;
        }
        JSONArray blacklist = Inicium.CONFIGURATION.getBlackList(guild.getId());
        String id = command.replaceFirst("blacklist-del ", "");

        if (Inicium.CONFIGURATION.delBlacklist(guild.getId(), id)) {
            textChannel.sendMessage("Le channel a été supprimé de la blacklist").queue();
        } else {
            textChannel.sendMessage("Le channel n'a pas été trouvé dans la blacklist ! Vous devez entrer l'ID du channel :\nhttps://support.discord.com/hc/fr/articles/206346498-Où-trouver-l-ID-de-mon-compte-utilisateur-serveur-message-").queue();
        }

    }

    @Command(name = "blacklist", type = Command.ExecutorType.USER)
    private void blacklist(Guild guild, TextChannel textChannel, User user) {

        if (guild == null) {
            return;
        }

        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            textChannel.sendMessage("Vous n'êtes pas administrateur de ce serveur").queue();
            return;
        }

        JSONArray commandOff = Inicium.CONFIGURATION.getBlackList(guild.getId());
        StringBuilder message = new StringBuilder();
        for(int i = 0; i < commandOff.length(); i++) {
            String id = commandOff.getString(i);
            message.append(id);
            TextChannel tc = guild.getTextChannelById(id);
            if (tc == null) {
                message.append("- ???");
            } else {
                message.append(" - "+tc.getName());
            }
            message.append("\n");
        }
        textChannel.sendMessage("BlackList :\n"+message.toString()).queue();
    }

    @Command(name="quit-channel", type= Command.ExecutorType.USER)
    private void quitChannel(Guild guild, TextChannel textChannel, User user, String command) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            textChannel.sendMessage("Vous n'êtes pas administrateur de ce serveur").queue();
            return;
        }
        String id = command.replaceFirst("quit-channel ", "");
        TextChannel tc = guild.getTextChannelById(id);
        if (tc == null) {
            textChannel.sendMessage("Channel introuvable ! Vous devez entrer l'ID du channel :\nhttps://support.discord.com/hc/fr/articles/206346498-Où-trouver-l-ID-de-mon-compte-utilisateur-serveur-message-").queue();
            return;
        }
        Inicium.CONFIGURATION.setQuit(guild.getId(), id);
        textChannel.sendMessage("Les messages de leave seront postés sur "+guild.getTextChannelById(id)).queue();
    }
    @Command(name="welcome-channel", type= Command.ExecutorType.USER)
    private void setWelcomeChannel(Guild guild, TextChannel textChannel, User user, String command) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            textChannel.sendMessage("Vous n'êtes pas administrateur de ce serveur").queue();
            return;
        }
        String id = command.replaceFirst("welcome-channel ", "");
        TextChannel tc = guild.getTextChannelById(id);
        if (tc == null) {
            textChannel.sendMessage("Channel introuvable ! Vous devez entrer l'ID du channel :\nhttps://support.discord.com/hc/fr/articles/206346498-Où-trouver-l-ID-de-mon-compte-utilisateur-serveur-message-").queue();
            return;
        }
        Inicium.CONFIGURATION.setWelcome(guild.getId(), id);
        textChannel.sendMessage("Les messages de join seront postés sur "+guild.getTextChannelById(id)).queue();
    }
    @Command(name="prefix", type= Command.ExecutorType.USER)
    private void setPrefix(Guild guild, TextChannel textChannel, User user, String command) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            textChannel.sendMessage("Vous n'êtes pas administrateur de ce serveur").queue();
            return;
        }
        String prefix = command.replaceFirst("prefix ", "");
        if (prefix.length() <1 || prefix.length() >= 5) {
            textChannel.sendMessage("Le prefix doit avoir une taille comprise entre 1 et 5 caractères ").queue();
            return;
        }
        Inicium.CONFIGURATION.setPrefix(guild.getId(), prefix);
        textChannel.sendMessage("Le nouveau prefix est ``"+prefix+"``").queue();
    }
    @Command(name="quit-channel-remove", type= Command.ExecutorType.USER)
    private void delQuitChannel(Guild guild, TextChannel textChannel, User user) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            textChannel.sendMessage("Vous n'êtes pas administrateur de ce serveur");
            return;
        }
        Inicium.CONFIGURATION.setQuit(guild.getId(), "");
        textChannel.sendMessage("Les messages de leave ne seront plus postés").queue();
    }

    @Command(name="welcome-channel-remove", type= Command.ExecutorType.USER)
    private void delWelcomeChannel(Guild guild, TextChannel textChannel, User user) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            textChannel.sendMessage("Vous n'êtes pas administrateur de ce serveur");
            return;
        }
        Inicium.CONFIGURATION.setWelcome(guild.getId(), "");
        textChannel.sendMessage("Les messages de join ne seront plus postés").queue();
    }

    @Command(name="help", type= Command.ExecutorType.USER)
    private void help(Guild guild, TextChannel textChannel, User user) {
        if (guild == null) {
            return;
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Liste des commandes");
        String prefix = Inicium.CONFIGURATION.getPrefix(guild.getId());
        eb.addBlankField(false);
        eb.addField(prefix+"play [Lien/RechercheYT], "+prefix+"p [...]", "Jouer une musique/video sur le channel vocal (YouTube, Twitch, SoundCloud, ...), ajoute à la file d'attente si une piste est en cours", false);
        eb.addField(prefix+"skip, "+prefix+"s", "Passe à la piste suivante", true);
        eb.addField(prefix+"pause", "Mettre en pause", true);
        eb.addField(prefix+"resume", "Reprendre", true);
        eb.addField(prefix+"ps [Lien/RechercheYT]", "Passe à la piste suivante et ajoute la musique à la file", false);
        eb.addField(prefix+"seek [Temps]", "Avance la piste au temps demandé (Ex pour 5min 38sec ``"+prefix+"seek 5:38``)", false);
        eb.addField(prefix+"leave, "+prefix+"quit, "+prefix+"disconnect, "+prefix+"disconnect", "Deconnexion du bot", false);
        eb.addField(prefix+"clear, "+prefix+"clean, "+prefix+"clr", "Effacer la liste d'attente", true);
        eb.addField(prefix+"queue, "+prefix+"np", "Affiche la file des pistes", true);
        eb.addBlankField(false);
        eb.addField("Réactions", "Vous pouvez réagir aux messages du bot pour effectuer des actions", false);
        eb.addField("Play", "▶", true);
        eb.addField("Pause", "⏸", true);
        eb.addField("Skip", "⏭", true);
        eb.addField("Clear", "🗑", true);
        eb.addField("Disconnect", "🚪", true);
        eb.addBlankField(true);
        eb.addBlankField(false);
        eb.addField("Administration", prefix+"help-admin", false);
        eb.setColor(Color.MAGENTA);
        textChannel.sendMessage(eb.build()).queue();
    }
    @Command(name="help-admin", type= Command.ExecutorType.USER)
    private void helpAdmin(Guild guild, TextChannel textChannel, User user) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            textChannel.sendMessage("Vous n'êtes pas administrateur de ce serveur");
            return;
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Liste des commandes");
        String prefix = Inicium.CONFIGURATION.getPrefix(guild.getId());
        eb.addBlankField(false);
        eb.addField(prefix+"welcome-channel [id-channel]", "Définir un channel pour l'affichage d'un message de bienvenue", false);
        eb.addField(prefix+"quit-channel [id-channel]", "Définir un channel pour l'affichage d'un message lors d'un départ", false);
        eb.addField(prefix+"welcome-channel-remove", "Supprimer le channel de join", false);
        eb.addField(prefix+"quit-channel-remove", "Supprimer le channel de leave", false);
        eb.addField(prefix+"blacklist", "Affiche la blacklist (Channel ou l'on ne peut pas executer de commande)", false);
        eb.addField(prefix+"blacklist-add [id-channel]", "Ajoute un channel à la blacklist", false);
        eb.addField(prefix+"blacklist-remove [id-channel]", "Supprime un channel de la blacklist", false);
        eb.addField(prefix+"prefix [nouveau-prefix]", "Change le prefix pour les commandes (Entre 1 et 5 caractères)", false);
        eb.setColor(Color.MAGENTA);
        textChannel.sendMessage(eb.build()).queue();
    }

}
