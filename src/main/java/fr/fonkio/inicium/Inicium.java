package fr.fonkio.inicium;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.fonkio.command.CommandMap;
import fr.fonkio.utils.Configuration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Inicium implements Runnable {
    public static String GENERAL;
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
    private JDA jda;
    private final CommandMap commandMap = new CommandMap(this);
    private boolean running;
    private final Scanner scanner = new Scanner(System.in);
    private IniciumListener iniciumListener;

    @Override
    public void run() {
        running = true;
        while (running) {
            if(scanner.hasNextLine()) {
                commandMap.commandConsole(scanner.nextLine());
            }
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
        this.iniciumListener = new IniciumListener(commandMap, this);
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
    public JDA getJda() {
        return jda;
    }

    private Timer timerUpdateBar;
    public void stopUpdateBar() {
        if(timerUpdateBar != null) {
            timerUpdateBar.cancel();
        }

    }
    public void launchUpdateBar(Message message) {
        if(timerUpdateBar != null) {
            timerUpdateBar.cancel();
        }
        TimerTask updateBar = new TimerTask() {
            public void run() {
                if (iniciumListener.isPause(message.getGuild())) {
                    cancel();
                } else if (iniciumListener.getCommandMap().getMusicCommand().getManager().getPlayer(message.getGuild()).getAudioPlayer().getPlayingTrack() == null) {
                    cancel();
                }
                else {
                    if (iniciumListener.getQueue(message.getGuild()).size()==0) {
                        cancel();
                    } else {
                        List<MessageEmbed> listMessageEmbeds = message.getEmbeds();
                        if (listMessageEmbeds.isEmpty()) {
                            cancel();
                        } else {
                            MessageEmbed me = listMessageEmbeds.get(0);
                            MessageAction ma = message.editMessage(createEmbed(me.getTitle(), me.getUrl(), me.getDescription(), me.getColor(), true, message.getGuild(), me.getFooter().getText(), me.getFooter().getIconUrl()));
                            addButtons(ma, message.getGuild()).queue();
                        }
                    }
                }
            }

        };

        timerUpdateBar = new Timer("timerUpdateBar");
        long delay  = 5000L;
        long period = 5000L;
        timerUpdateBar.scheduleAtFixedRate(updateBar, delay, period);
    }

    public MessageAction addButtons(MessageAction message, Guild guild) {
        List<Component> buttons = new ArrayList<>();

        if (iniciumListener.isPause(guild)) {
            buttons.add(Button.success("resume", "▶ Play"));
            buttons.add(Button.secondary("pause", "⏸ Pause").asDisabled());
        } else {
            buttons.add(Button.secondary("resume", "▶ Play").asDisabled());
            buttons.add(Button.success("pause", "⏸ Pause"));
        }
        if (guild.getAudioManager().isConnected()) {
            buttons.add(Button.primary("skip", "⏯ Skip"));
            buttons.add(Button.danger("clear", "\uD83D\uDDD1 Effacer la liste"));
            buttons.add(Button.danger("disconnect", "\uD83D\uDEAA Déconnecter"));
        }
        message.setActionRow(buttons);
        return message;
    }
    public MessageEmbed createEmbed(String title, String urlTitle, String description, Color couleur, boolean displayQueue, Guild guild, User user) {
        return createEmbed(title, urlTitle, description, couleur, displayQueue, guild, user, false);
    }

    public MessageEmbed createEmbed(String title, String urlTitle, String description, Color couleur, boolean displayQueue, Guild guild, String footerString, String footerIcon) {
        return createEmbed(title, urlTitle, description, couleur, displayQueue, guild, footerString, footerIcon, false);
    }

    public MessageEmbed createEmbed(String title, String urlTitle, String description, Color couleur, boolean displayQueue, Guild guild, User user, boolean newPlay) {
        if (user != null) {
            return createEmbed(title, urlTitle, description, couleur, displayQueue, guild, user.getName(), user.getAvatarUrl(), newPlay);
        } else {
            return createEmbed(title, urlTitle, description, couleur, displayQueue, guild, null, null, newPlay);
        }

    }

    public MessageEmbed createEmbed(String title, String urlTitle, String description, Color couleur, boolean displayQueue, Guild guild, String footerString, String footerIcon, boolean newPlay) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Bot musique", null, "https://i.pinimg.com/originals/79/ab/9f/79ab9f804b5ebbdd514af3329cad6e0c.gif?size=256");
        builder.setTitle(title, urlTitle);
        builder.setDescription(description);
        builder.setColor(couleur);
        builder.setFooter(footerString, footerIcon);


        if (displayQueue) {
            List <AudioTrack> queue = iniciumListener.getQueue(guild);
            addFields(newPlay, builder, queue);
        }

        return builder.build();
    }

    private void addFields(boolean newPlay, EmbedBuilder builder, List<AudioTrack> queue) {
        if (queue.size()==0) {
            builder.addField("Musique", "Aucune musique n'est en cours de lecture", false);
        } else if (queue.size()==1) {
            AudioTrack np = queue.get(0);
            if (np == null) {
                builder.addField("Musique", "Aucune musique n'est en cours de lecture", false);
            } else {
                if (np.getInfo().uri.contains("youtube.com")) {
                    builder.setThumbnail("http://i3.ytimg.com/vi/"+np.getInfo().uri.split("v=")[1].split("&")[0]+"/maxresdefault.jpg");
                }
                String duration;
                if(np.getInfo().isStream) {
                    duration = "STREAM";
                } else {
                    duration = convertLongToString(np.getDuration());
                }
                String position = convertLongToString(np.getPosition());
                builder.addField("``[En cours]`` "+np.getInfo().title, "**Durée :** ``"+position+" / "+duration + "``\n**Auteur :** ``"+np.getInfo().author+"``", false);
                Double posF;
                if (np.getInfo().isStream) {
                    posF = 100D;
                } else {
                    posF = ((double)np.getPosition()/np.getInfo().length)*100;
                }
                builder.setImage("http://www.yarntomato.com/percentbarmaker/button.php?barPosition="+posF.intValue()+"&leftFill=%2300FF00");
                builder.addField("File d'attente", "Aucune musique n'est dans la file d'attente ...", false);
            }

        } else {
            AudioTrack np = queue.get(0);
            String duration;
            if(np.getInfo().isStream) {
                duration = "STREAM";
            } else {
                duration = convertLongToString(np.getInfo().length);
            }
            String position = convertLongToString(np.getPosition());
            builder.addField("``[En cours]`` "+np.getInfo().title, "**Durée :** ``"+position+" / "+duration + "``\n**Auteur :** ``"+np.getInfo().author+"``", false);
            Double posF;
            if (np.getInfo().isStream) {
                posF = 100D;
            } else {
                posF = ((double)np.getPosition()/np.getInfo().length)*100;
            }
            builder.setImage("http://www.yarntomato.com/percentbarmaker/button.php?barPosition="+posF.intValue()+"&leftFill=%2300FF00");
            queue.remove(0);
            int i = 0;
            String link = "";
            for(AudioTrack at : queue) {
                i++;
                if(at.getInfo().isStream) {
                    duration = "STREAM";
                }
                duration = convertLongToString(at.getDuration());
                builder.addField("``["+i+"]`` "+at.getInfo().title, "**Durée :** ``"+duration + "``\n**Auteur :** ``"+at.getInfo().author+"``", false);
                link = at.getInfo().uri;
            }
            if (newPlay) {
                if (link.contains("youtube.com")) {
                    builder.setThumbnail("http://i3.ytimg.com/vi/"+link.split("v=")[1].split("&")[0]+"/maxresdefault.jpg");
                }

            } else {
                if (np.getInfo().uri.contains("youtube.com")) {
                    builder.setThumbnail("http://i3.ytimg.com/vi/"+np.getInfo().uri.split("v=")[1].split("&")[0]+"/maxresdefault.jpg");
                }
            }
        }
    }


    public String convertLongToString(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
