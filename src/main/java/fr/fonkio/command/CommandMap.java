package fr.fonkio.command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.fonkio.inicium.Inicium;
import fr.fonkio.music.MusicCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class CommandMap {
    private final Map<String, SimpleCommand> commands = new HashMap<>();
    private Inicium iniciumBot;

    public MusicCommand getMusicCommand() {
        return musicCommand;
    }

    private MusicCommand musicCommand;

    public CommandMap(Inicium iniciumBot) {
        this.iniciumBot = iniciumBot;
        musicCommand = new MusicCommand(iniciumBot);
        registerCommands(new CommandGeneral(iniciumBot), musicCommand);
    }


    public void disconnect(Guild guild) {
        musicCommand.disconnectQuiet(guild);
    }

    public Collection<SimpleCommand> getCommands(){
        return commands.values();
    }

    public void registerCommands(Object...objects){
        for(Object object : objects) registerCommand(object);
    }

    public void registerCommand(Object object){
        for(Method method : object.getClass().getDeclaredMethods()){
            if(method.isAnnotationPresent(Command.class)){
                Command command = method.getAnnotation(Command.class);
                method.setAccessible(true);
                SimpleCommand simpleCommand = new SimpleCommand(command.name(), command.description(), command.type(), object, method);
                commands.put(command.name(), simpleCommand);
            }
        }
    }

    public void commandConsole(String command){
        Object[] object = getCommand(command);
        if(object[0] == null || ((SimpleCommand)object[0]).getExecutorType() == Command.ExecutorType.USER){
            System.out.println("Commande inconnue.");
            return;
        }
        try{
            execute(((SimpleCommand)object[0]), command, (String[])object[1], null);
        }catch(Exception exception){
            exception.printStackTrace();
            System.out.println("La methode "+((SimpleCommand)object[0]).getMethod().getName()+" n'est pas correctement initialisé.");
        }
    }

    public boolean commandUser(User user, String command, Message message){
        Object[] object = getCommand(command);
        if(object[0] == null || ((SimpleCommand)object[0]).getExecutorType() == Command.ExecutorType.CONSOLE)  {
            if (message != null) {
                message.addReaction("U+2049").queue();
                TimerTask task = new TimerTask() {
                    public void run() {
                        message.delete().queue();
                    }
                };
                Timer timer = new Timer("Timer");

                long delay = 5000L;
                timer.schedule(task, delay);
            }
            return false;
        }
        if (message != null && message.getAuthor() != iniciumBot.getJda().getSelfUser()) {
            TimerTask task = new TimerTask() {
                public void run() {
                    message.addReaction("U+2705").queue();
                }
            };
            Timer timer = new Timer("Timer");

            long delay = 1000L;
            timer.schedule(task, delay);
        }
        try{
            execute(((SimpleCommand)object[0]), command,(String[])object[1], message);
        }catch(Exception exception){
            exception.printStackTrace();
            System.out.println("La methode "+((SimpleCommand)object[0]).getMethod().getName()+" n'est pas correctement initialisé.");
        }
        return true;
    }
    private Object[] getCommand(String command){
        String[] commandSplit = command.split(" ");
        String[] args = new String[commandSplit.length-1];
        for(int i = 1; i < commandSplit.length; i++) args[i-1] = commandSplit[i];
        SimpleCommand simpleCommand = commands.get(commandSplit[0]);
        return new Object[]{simpleCommand, args};
    }

    private void execute(SimpleCommand simpleCommand, String command, String[] args, Message message) throws Exception {
        Parameter[] parameters = simpleCommand.getMethod().getParameters();
        Object[] objects = new Object[parameters.length];
        for(int i = 0; i < parameters.length; i++) {
            if(parameters[i].getType() == String[].class) {
                objects[i] = args;
            } else if(parameters[i].getType() == User.class) {
                objects[i] = message == null ? null : message.getAuthor();
            } else if(parameters[i].getType() == TextChannel.class) {
                objects[i] = message == null ? null : message.getTextChannel();
            } else if(parameters[i].getType() == PrivateChannel.class) {
                objects[i] = message == null ? null : message.getPrivateChannel();
            } else if(parameters[i].getType() == Guild.class) {
                objects[i] = message == null ? null : message.getGuild();
            } else if(parameters[i].getType() == String.class) {
                objects[i] = command;
            } else if(parameters[i].getType() == Message.class) {
                objects[i] = message;
            } else if(parameters[i].getType() == JDA.class) {
                objects[i] = iniciumBot.getJda();
            } else if(parameters[i].getType() == MessageChannel.class) {
                objects[i] = message == null ? null : message.getChannel();
            }
        }
        simpleCommand.getMethod().invoke(simpleCommand.getObject(), objects);;
    }
    public boolean isPause(Guild guild) {
        return musicCommand.isPause(guild);
    }
    public List<AudioTrack> getQueue(Guild guild) {
        return musicCommand.getQueue(guild);
    }
}
