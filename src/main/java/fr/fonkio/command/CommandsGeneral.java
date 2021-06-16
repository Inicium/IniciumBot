package fr.fonkio.command;

import fr.fonkio.inicium.Inicium;
import fr.fonkio.message.EmbedGenerator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONArray;

import java.awt.*;

public class CommandsGeneral {



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
                        EmbedGenerator.generate(author, "blacklist", "La commande s'utilise de la manière suivante :\n"+Inicium.CONFIGURATION.getPrefix(guild.getId())+"blacklist (add/remove/list) [idChannel]")
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
                        EmbedGenerator.generate(author, "welcome", "La commande s'utilise de la manière suivante :\n"+Inicium.CONFIGURATION.getPrefix(guild.getId())+"welcome (set/remove) [idChannel]")
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
                        EmbedGenerator.generate(author, "goodbye", "La commande s'utilise de la manière suivante :\n"+Inicium.CONFIGURATION.getPrefix(guild.getId())+"goodbye (set/remove) [idChannel]")
                ).queue();
        }
    }

    private void blacklistAdd(Guild guild, Message message, User user, String command) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            message.reply(
                    EmbedGenerator.generate(user, "Permission", "Vous n'êtes pas administrateur de ce serveur")
            ).queue();
            return;
        }
        JSONArray blackList = Inicium.CONFIGURATION.getBlackList(guild.getId());
        String id = command.replaceFirst("blacklist-add ", "");
        TextChannel tc = guild.getTextChannelById(id);
        if (tc == null) {
            message.reply(
                    EmbedGenerator.generate(user, "Ajout blacklist échec", "Channel introuvable ! Vous devez entrer l'ID du channel :\nhttps://support.discord.com/hc/fr/articles/206346498-Où-trouver-l-ID-de-mon-compte-utilisateur-serveur-message-")
            ).queue();
            return;
        }
        boolean trouve = false;
        for(int i = 0; i < blackList.length(); i++) {
            if (blackList.getString(i).equals(id)) {
                trouve = true;
            }
        }
        if (trouve) {
            message.reply(
                    EmbedGenerator.generate(user, "Ajout blacklist échec", tc.getName()+" est déjà dans la blacklist !")
            ).queue();
            return;
        }
        Inicium.CONFIGURATION.addBlackList(guild.getId(), id);
        message.reply(
                EmbedGenerator.generate(user, "Ajout blacklist", tc.getName() + " a été ajouté à la blacklist !")
        ).queue();
    }

    private void blacklistRemove(Guild guild, Message message, User user, String command) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            message.reply(
                    EmbedGenerator.generate(user, "Permission", "Vous n'êtes pas administrateur de ce serveur")
            ).queue();
            return;
        }
        String id = command.replaceFirst("blacklist-del ", "");

        if (Inicium.CONFIGURATION.delBlacklist(guild.getId(), id)) {
            message.reply(
                    EmbedGenerator.generate(user, "Suppression blacklist", "Le channel a été supprimé de la blacklist")
            ).queue();
        } else {
            message.reply(
                    EmbedGenerator.generate(user, "Suppression blacklist échec", "Le channel n'a pas été trouvé dans la blacklist ! Vous devez entrer l'ID du channel :\nhttps://support.discord.com/hc/fr/articles/206346498-Où-trouver-l-ID-de-mon-compte-utilisateur-serveur-message-")
            ).queue();
        }

    }

    private void blacklist(Guild guild, Message message, User user) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            message.reply(
                    EmbedGenerator.generate(user, "Permission", "Vous n'êtes pas administrateur de ce serveur")
            ).queue();
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
        message.reply(
                EmbedGenerator.generate(user, "Blacklist :", listBl.toString())
        ).queue();
    }

    public void quitChannel(Guild guild, Message message, User user, String command) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            message.reply(
                    EmbedGenerator.generate(user, "Permission", "Vous n'êtes pas administrateur de ce serveur")
            ).queue();
            return;
        }
        String id = command.replaceFirst("quit-channel ", "");
        TextChannel tc = guild.getTextChannelById(id);
        if (tc == null) {
            message.reply(
                    EmbedGenerator.generate(user, "goodbye set échec", "Channel introuvable ! Vous devez entrer l'ID du channel :\nhttps://support.discord.com/hc/fr/articles/206346498-Où-trouver-l-ID-de-mon-compte-utilisateur-serveur-message-")
            ).queue();
            return;
        }
        Inicium.CONFIGURATION.setQuit(guild.getId(), id);
        message.reply(
                EmbedGenerator.generate(user, "goodbye set", "Les messages de leave seront postés sur "+guild.getTextChannelById(id))
        ).queue();
    }

    public void setWelcomeChannel(Guild guild, Message message, User user, String command) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            message.reply(
                    EmbedGenerator.generate(user, "Permission", "Vous n'êtes pas administrateur de ce serveur")
            ).queue();
            return;
        }
        String id = command.replaceFirst("welcome-channel ", "");
        TextChannel tc = guild.getTextChannelById(id);
        if (tc == null) {
            message.reply(
                    EmbedGenerator.generate(user, "welcome set échec", "Channel introuvable ! Vous devez entrer l'ID du channel :\nhttps://support.discord.com/hc/fr/articles/206346498-Où-trouver-l-ID-de-mon-compte-utilisateur-serveur-message-")
            ).queue();
            return;
        }
        Inicium.CONFIGURATION.setWelcome(guild.getId(), id);
        message.reply(
                EmbedGenerator.generate(user, "welcome set", "Les messages de join seront postés sur "+guild.getTextChannelById(id))
        ).queue();
    }

    public void setPrefix(Guild guild, Message message, User user, String command) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            message.reply(
                    EmbedGenerator.generate(user, "Permission", "Vous n'êtes pas administrateur de ce serveur")
            ).queue();
            return;
        }
        String prefix = command.replaceFirst("prefix ", "");
        if (prefix.length() <1 || prefix.length() >= 5) {
            message.reply(
                    EmbedGenerator.generate(user, "Prefix échec", "Le prefix doit avoir une taille comprise entre 1 et 5 caractères ")
            ).queue();
            return;
        }
        Inicium.CONFIGURATION.setPrefix(guild.getId(), prefix);
        message.reply(
                EmbedGenerator.generate(user, "Prefix", "Le nouveau prefix est ``"+prefix+"``")
        ).queue();
    }

    public void delQuitChannel(Guild guild, Message message, User user) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            message.reply(
                    EmbedGenerator.generate(user, "Permission", "Vous n'êtes pas administrateur de ce serveur")
            ).queue();
            return;
        }
        Inicium.CONFIGURATION.setQuit(guild.getId(), "");
        message.reply(
                EmbedGenerator.generate(user, "goodbye remove", "Les messages de leave ne seront plus postés")
        ).queue();
    }

    public void delWelcomeChannel(Guild guild, Message message, User user) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            message.reply(
                    EmbedGenerator.generate(user, "Permission", "Vous n'êtes pas administrateur de ce serveur")
            ).queue();
            return;
        }
        Inicium.CONFIGURATION.setWelcome(guild.getId(), "");
        message.reply(
                EmbedGenerator.generate(user, "welcome remove", "Les messages de join sont désormais désactivés")
        ).queue();
    }

    public void help(Guild guild, Message message, User user) {
        if (guild == null) {
            return;
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(user.getName(), null, user.getAvatarUrl());
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
        eb.addField("Boutons d'action", "Vous pouvez effectuer des actions avec les boutons d'action", false);
        eb.setFooter("Pour voir les commandes admin : " + prefix+"helpadmin");
        eb.setColor(Color.BLUE);
        eb.setImage("https://cdn.discordapp.com/attachments/297407304871968768/854394142761943060/unknown.png");
        eb.setAuthor(user.getName(), null, user.getAvatarUrl());
        message.reply(
                eb.build()
        ).queue();
    }

    public void helpAdmin(Guild guild, Message message, User user) {
        if (guild == null) {
            return;
        }
        if (!guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) {
            message.reply(
                    EmbedGenerator.generate(user, "Permission", "Vous n'êtes pas administrateur de ce serveur")
            ).queue();
            return;
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Liste des commandes");
        String prefix = Inicium.CONFIGURATION.getPrefix(guild.getId());
        eb.addBlankField(false);
        eb.addField(prefix+"welcome help", "Définir un channel pour l'affichage d'un message de bienvenue", false);
        eb.addField(prefix+"goodbye help", "Définir un channel pour l'affichage d'un message lors d'un départ", false);
        eb.addField(prefix+"blacklist help", "Info gestion de la blacklist (Channel ou l'on ne peut pas executer de commande)", false);
        eb.addField(prefix+"prefix [nouveau-prefix]", "Change le prefix pour les commandes (Entre 1 et 5 caractères)", false);
        eb.setColor(Color.CYAN);
        eb.setAuthor(user.getName(), null, user.getAvatarUrl());
        message.reply(
                eb.build()
        ).queue();
    }

}
