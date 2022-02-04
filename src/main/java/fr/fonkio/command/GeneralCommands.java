package fr.fonkio.command;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.EmbedGenerator;
import fr.fonkio.utils.ConfigurationEnum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONArray;

import java.awt.*;

public class GeneralCommands {



    public void blacklistExec(User author, Guild guild, Message message, String args) {
        String action = args.split(" ")[0];
        switch (action) {
            case "add":
                blacklistAdd(guild, message, author, args.substring(4));
                break;
            case "remove":
            case "delete":
                blacklistRemove(guild, message, author, args.substring(7));
                break;
            case "list":
                blacklist(guild, message, author);
                break;
            default:
                message.reply(
                        EmbedGenerator.generate(author, "blacklist", "La commande s'utilise de la manière suivante :\n"+Inicium.CONFIGURATION.getGuildConfig(guild.getId(), ConfigurationEnum.PREFIX_COMMAND)+"blacklist (add/remove/list) [idChannel]")
                ).queue();
        }
    }

    public void welcomeExec(User author, Guild guild, Message message, String args) {
        String action = args.split(" ")[0];
        switch (action) {
            case "set":
                setWelcomeChannel(guild, message, author, args.split(" ")[1]);
                break;
            case "remove":
            case "delete":
                delWelcomeChannel(guild, message, author);
                break;
            default:
                message.reply(
                        EmbedGenerator.generate(author, "welcome", "La commande s'utilise de la manière suivante :\n"+Inicium.CONFIGURATION.getGuildConfig(guild.getId(), ConfigurationEnum.PREFIX_COMMAND)+"welcome (set/remove) [idChannel]")
                ).queue();
        }
    }

    public void goodbyeExec(User author, Guild guild, Message message, String args) {
        String action = args.split(" ")[0];
        switch (action) {
            case "set":
                quitChannel(guild, message, author, args.split(" ")[1]);
                break;
            case "remove":
            case "delete":
                delQuitChannel(guild, message, author);
                break;
            default:
                message.reply(
                        EmbedGenerator.generate(author, "goodbye", "La commande s'utilise de la manière suivante :\n"+Inicium.CONFIGURATION.getGuildConfig(guild.getId(), ConfigurationEnum.PREFIX_COMMAND)+"goodbye (set/remove) [idChannel]")
                ).queue();
        }
    }

    private void blacklistAdd(Guild guild, Message message, User user, String command) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            permissionCheck(message, user);
            return;
        }
        JSONArray blackList = Inicium.CONFIGURATION.getBlackList(guild.getId());
        String id = command.replaceFirst("blacklist-add ", "");
        TextChannel tc = guild.getTextChannelById(id);
        if (tc == null) {
            replyEmbed(message, user, "Ajout blacklist échec", "Channel introuvable ! Vous devez entrer l'ID du channel :\nhttps://support.discord.com/hc/fr/articles/206346498-Où-trouver-l-ID-de-mon-compte-utilisateur-serveur-message-");
            return;
        }
        boolean trouve = false;
        for(int i = 0; i < blackList.length(); i++) {
            if (blackList.getString(i).equals(id)) {
                trouve = true;
            }
        }
        if (trouve) {
            replyEmbed(message, user, "Ajout blacklist échec", tc.getName()+" est déjà dans la blacklist !");
            return;
        }
        Inicium.CONFIGURATION.addBlackList(guild.getId(), id);
        replyEmbed(message, user, "Ajout blacklist", tc.getName() + " a été ajouté à la blacklist !");
    }

    private void permissionCheck(Message message, User user) {
        replyEmbed(message, user, "Permission", "Vous n'êtes pas administrateur de ce serveur");
    }

    private void blacklistRemove(Guild guild, Message message, User user, String command) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            permissionCheck(message, user);
            return;
        }
        String id = command.replaceFirst("blacklist-del ", "");

        if (Inicium.CONFIGURATION.delBlacklist(guild.getId(), id)) {
            replyEmbed(message, user, "Suppression blacklist", "Le channel a été supprimé de la blacklist");
        } else {
            replyEmbed(message, user, "Suppression blacklist échec", "Le channel n'a pas été trouvé dans la blacklist ! Vous devez entrer l'ID du channel :\nhttps://support.discord.com/hc/fr/articles/206346498-Où-trouver-l-ID-de-mon-compte-utilisateur-serveur-message-");
        }

    }

    private void blacklist(Guild guild, Message message, User user) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            permissionCheck(message, user);
            return;
        }

        JSONArray commandOff = Inicium.CONFIGURATION.getBlackList(guild.getId());
        StringBuilder listBl = new StringBuilder();
        for(int i = 0; i < commandOff.length(); i++) {
            String id = commandOff.getString(i);
            listBl.append(id);
            TextChannel tc = guild.getTextChannelById(id);
            if (tc == null) {
                listBl.append("- ???");
            } else {
                listBl.append(" - "+tc.getName());
            }
            listBl.append("\n");
        }
        replyEmbed(message, user, "Blacklist :", listBl.toString());
    }

    public void quitChannel(Guild guild, Message message, User user, String command) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            permissionCheck(message, user);
            return;
        }
        String id = command.replaceFirst("quit-channel ", "");
        TextChannel tc = guild.getTextChannelById(id);
        if (tc == null) {
            replyEmbed(message, user, "goodbye set échec", "Channel introuvable ! Vous devez entrer l'ID du channel :\nhttps://support.discord.com/hc/fr/articles/206346498-Où-trouver-l-ID-de-mon-compte-utilisateur-serveur-message-");
            return;
        }
        Inicium.CONFIGURATION.setGuildConfig(guild.getId(), ConfigurationEnum.QUIT_CHANNEL, id);
        replyEmbed(message, user, "goodbye set", "Les messages de leave seront postés sur "+guild.getTextChannelById(id));
    }

    public void setWelcomeChannel(Guild guild, Message message, User user, String command) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            permissionCheck(message, user);
            return;
        }
        String id = command.replaceFirst("welcome-channel ", "");
        TextChannel tc = guild.getTextChannelById(id);
        if (tc == null) {
            replyEmbed(message, user, "welcome set échec", "Channel introuvable ! Vous devez entrer l'ID du channel :\nhttps://support.discord.com/hc/fr/articles/206346498-Où-trouver-l-ID-de-mon-compte-utilisateur-serveur-message-");
            return;
        }
        Inicium.CONFIGURATION.setGuildConfig(guild.getId(), ConfigurationEnum.WELCOME_CHANNEL, id);
        replyEmbed(message, user, "welcome set", "Les messages de join seront postés sur "+guild.getTextChannelById(id));
    }

    private void replyEmbed(Message message, User user, String permission, String s) {
        message.reply(
                EmbedGenerator.generate(user, permission, s)
        ).queue();
    }

    public void setPrefix(Guild guild, Message message, User user, String command) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            permissionCheck(message, user);
            return;
        }
        String prefix = command.replaceFirst("prefix ", "");
        if (prefix.length() <1 || prefix.length() >= 5) {
            replyEmbed(message, user, "Prefix échec", "Le prefix doit avoir une taille comprise entre 1 et 5 caractères ");
            return;
        }
        Inicium.CONFIGURATION.setGuildConfig(guild.getId(), ConfigurationEnum.PREFIX_COMMAND, prefix);
        message.replyEmbeds(
                EmbedGenerator.generate(user, "Prefix", "Le nouveau prefix est ``" + prefix + "``")
        ).queue();
    }

    public void delQuitChannel(Guild guild, Message message, User user) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            permissionCheck(message, user);
            return;
        }
        Inicium.CONFIGURATION.setGuildConfig(guild.getId(), ConfigurationEnum.QUIT_CHANNEL, "");
        replyEmbed(message, user, "goodbye remove", "Les messages de leave ne seront plus postés");
    }

    public void delWelcomeChannel(Guild guild, Message message, User user) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            permissionCheck(message, user);
            return;
        }
        Inicium.CONFIGURATION.setGuildConfig(guild.getId(), ConfigurationEnum.WELCOME_CHANNEL, "");
        replyEmbed(message, user, "welcome remove", "Les messages de join sont désormais désactivés");
    }

    public void help(Guild guild, Message message, User user) {
        if (guild == null) {
            return;
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(user.getName(), null, user.getAvatarUrl());
        eb.setTitle("Liste des commandes");
        String prefix = Inicium.CONFIGURATION.getGuildConfig(guild.getId(), ConfigurationEnum.PREFIX_COMMAND);
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
        eb.addField("Boutons d'action", "Vous pouvez effectuer des actions avec les boutons d'action", false);
        eb.setFooter("Pour voir les commandes admin : " + prefix+"helpadmin");
        eb.setColor(Color.GREEN);
        eb.setImage("https://cdn.discordapp.com/attachments/297407304871968768/854394142761943060/unknown.png");
        eb.setAuthor(user.getName(), null, user.getAvatarUrl());
        message.replyEmbeds(
                eb.build()
        ).queue();
    }

    public void helpAdmin(Guild guild, Message message, User user) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            permissionCheck(message, user);
            return;
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Liste des commandes");
        String prefix = Inicium.CONFIGURATION.getGuildConfig(guild.getId(), ConfigurationEnum.PREFIX_COMMAND);
        eb.addBlankField(false);
        eb.addField(prefix+"welcome help", "Définir un channel pour l'affichage d'un message de bienvenue", false);
        eb.addField(prefix+"goodbye help", "Définir un channel pour l'affichage d'un message lors d'un départ", false);
        eb.addField(prefix+"blacklist help", "Info gestion de la blacklist (Channel ou l'on ne peut pas executer de commande)", false);
        eb.addField(prefix+"prefix [nouveau-prefix]", "Change le prefix pour les commandes (Entre 1 et 5 caractères)", false);
        eb.setColor(Color.GREEN);
        eb.setAuthor(user.getName(), null, user.getAvatarUrl());
        message.replyEmbeds(
                eb.build()
        ).queue();
    }

}
