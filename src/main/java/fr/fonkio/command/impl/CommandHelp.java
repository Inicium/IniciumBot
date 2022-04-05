package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractCommand;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.utils.ConfigurationEnum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.awt.*;

public class CommandHelp extends AbstractCommand {



    @Override
    public boolean run(SlashCommandInteractionEvent eventSlash, ButtonInteractionEvent eventButton) {
        if (eventSlash == null) {
            return false;
        }
        User user = eventSlash.getUser();
        Guild guild = eventSlash.getGuild();
        if (guild != null) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setAuthor(user.getName(), null, user.getAvatarUrl());
            eb.setTitle("Liste des commandes");
            String prefix = "/";
            eb.addBlankField(false);
            eb.addField(prefix+"play [Musique], "+prefix+"p [...]", "Jouer une musique/video sur le channel vocal (YouTube, Twitch, SoundCloud, ...), ajoute à la file d'attente si une piste est en cours", false);
            eb.addField(prefix+"skip, "+prefix+"s", "Passe à la piste suivante", true);
            eb.addField(prefix+"pause", "Mettre en pause", true);
            eb.addField(prefix+"resume", "Reprendre", true);
            eb.addField(prefix+"ps [Musique]", "Passe à la piste suivante et ajoute la musique à la file", false);
            eb.addField(prefix+"seek [Temps]", "Avance la piste au temps demandé (Ex pour 5min 38sec ``"+prefix+"seek 5:38``)", false);
            eb.addField(prefix+"leave, "+prefix+"quit, "+prefix+"disconnect, "+prefix+"dc", "Deconnexion du bot", false);
            eb.addField(prefix+"clear, "+prefix+"clean, "+prefix+"clr", "Effacer la liste d'attente", true);
            eb.addField(prefix+"queue, "+prefix+"np", "Affiche la file des pistes", true);
            eb.addField(prefix+"moveall [Destination], "+prefix+"mva [...]", "Déplacer toutes les personnes du channel actuel dans un autre channel", false);
            eb.addBlankField(false);
            eb.addField("Boutons d'action", "Vous pouvez effectuer des actions avec les boutons d'action", false);
            eb.setFooter("Pour voir les commandes admin : " + prefix+"helpadmin");
            eb.setColor(Color.GREEN);
            eb.setImage("https://cdn.discordapp.com/attachments/297407304871968768/854394142761943060/unknown.png");
            eb.setAuthor(user.getName(), null, user.getAvatarUrl());

            eventSlash.replyEmbeds(
                    eb.build()
            ).setEphemeral(true).queue();
        }
        return true;
    }
}
