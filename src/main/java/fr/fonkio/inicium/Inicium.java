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


public class Inicium implements Runnable {
    public static Configuration CONFIGURATION;
    static {
        Configuration configuration = null;
        try {
            configuration = new Configuration("data.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CONFIGURATION = configuration;
    }
    private static JDA jda;
    private boolean running;
    private final Scanner scanner = new Scanner(System.in);
    private IniciumListener iniciumListener;

    @Override
    public void run() {
        running = true;
        while (running) {
        }
        scanner.close();
        System.out.println("Le bot a été arrété.");
        jda.shutdown();
        System.exit(0);
    }

    public static void main(String[] args) {
        try {
            Inicium bot = new Inicium();
            new Thread(bot, "boot").start();
        } catch (LoginException | IllegalArgumentException | RateLimitedException e) {
            e.printStackTrace();
        }
        CONFIGURATION.save();

    }

    public Inicium() throws LoginException, IllegalArgumentException, RateLimitedException {
        System.out.println("Demarrage du bot ...");
        Set<GatewayIntent> intents = new HashSet<>();
        intents.addAll(EnumSet.allOf(GatewayIntent.class));
        jda = JDABuilder.create(CONFIGURATION.getToken(),intents).setAutoReconnect(true).build();
        CONFIGURATION.save();
        this.iniciumListener = new IniciumListener();
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
    }
    public static JDA getJda() {
        return jda;
    }


    public void setRunning(boolean running) {
        this.running = running;
    }
}
