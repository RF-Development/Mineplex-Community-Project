package club.mineplex.milestones;

import club.mineplex.milestones.configs.Config;
import club.mineplex.milestones.configs.ConfigLoader;
import club.mineplex.milestones.utilities.BotActions;
import club.mineplex.milestones.utilities.objects.permissibles.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static club.mineplex.milestones.utilities.Logger.log;

public class Main extends ListenerAdapter {

    /* CACHE */
    public static final List<Command> commands = new ArrayList<>();
    public static final Set<ScheduledFuture<?>> tasks = new HashSet<>();
    public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
    /* CONSTANTS */
    public static ConfigLoader configLoader, langLoader;
    public static Config config;
    private static JDA jda;

    /**
     * Start the bot & accept commands in the console
     *
     * @param args Ignored
     */
    public static void main(final String[] args) {

        // Start the bot
        BotActions.action(BotActions.SuperAction.START);

        // Accept arguments
        final Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            final String input = scanner.next();
            for (final BotActions.SuperAction sa : BotActions.SuperAction.values()) {
                if (sa.matches(input)) {
                    BotActions.action(sa);
                }
            }
        }

    }

    /**
     * Get current instance of the JDA
     *
     * @return The JDA instance
     */
    public static JDA getJDA() {
        return jda;
    }

    /**
     * Set current instance of JDA
     *
     * @param jda The new JDA instance
     */
    public static void setJDA(final JDA jda) {
        Main.jda = jda;
    }

    /**
     * Send a message on login
     *
     * @param e JDA ReadyEvent
     */
    @Override
    public void onReady(final ReadyEvent e) {
        log(String.format("Logged in as %s (%s Guilds)", e.getJDA().getSelfUser().getName(),
                          e.getJDA().getGuilds().toArray().length
        ));
    }

}


