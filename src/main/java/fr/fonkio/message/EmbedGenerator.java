package fr.fonkio.message;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class EmbedGenerator {

    public static MessageEmbed generate(User author, String command, String message) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(author.getName(), null, author.getAvatarUrl());
        builder.setTitle(command);
        builder.setDescription(message);
        builder.setColor(Color.GREEN);
        return builder.build();
    }

}
