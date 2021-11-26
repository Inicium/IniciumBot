package fr.fonkio.inicium;

import fr.fonkio.utils.Configuration;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.*;


public class Inicium {

    public static Configuration CONFIGURATION;
    private static JDA jda;

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
        Set<GatewayIntent> intents = new HashSet<>();
        intents.addAll(EnumSet.allOf(GatewayIntent.class));
        jda = JDABuilder.create(CONFIGURATION.getToken(),intents).setAutoReconnect(true).build();
        CONFIGURATION.save();
        IniciumListener iniciumListener = new IniciumListener();
        jda.addEventListener(iniciumListener);
        Activity act = new Activity() {
            @Override
            public boolean isRich() {
                return true;
            }
            @Override
            public String getUrl() {
                return null;
            }
            @Override
            public ActivityType getType() {
                return ActivityType.DEFAULT;
            }
            @Override
            public Timestamps getTimestamps() {
                return new Timestamps(System.currentTimeMillis(), System.currentTimeMillis()+10000000L);
            }
            @Override
            public String getName() {
                return "IniciumBot \uD83C\uDF0C                                                                                 \nCréé par Fonkio";
            }
            @Override
            public Emoji getEmoji() {
                return new Activity.Emoji("milky_way");
            }
            @Override
            public RichPresence asRichPresence() {
                return null;
            }
        };
        jda.getPresence().setActivity(act);
        System.out.println("Bot connecté");
        CONFIGURATION.save();

    }

    public Inicium() throws LoginException, IllegalArgumentException {

    }

    public static JDA getJda() {
        return jda;
    }
}
