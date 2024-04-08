package fr.fonkio.message;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class EmbedGenerator {

    public static MessageEmbed generate(User author, String title, String message) {
        return generate(author, title, message, null);
    }

    public static MessageEmbed generate(User author, String title, String message, Guild footer) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(author.getEffectiveName(), null, author.getAvatarUrl());
        builder.setTitle(title);
        builder.setDescription(message);
        builder.setColor(Color.GREEN);
        if (footer != null) {
            builder.setFooter(footer.getName(), footer.getIconUrl());
        }
        return builder.build();
    }

}
