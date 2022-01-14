package club.mineplex.discord.events;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


/*
 * RUN ACTIONS ON BOT READY
 */

public class ReadyActions extends ListenerAdapter {

    @Override
    public void onReady(final ReadyEvent e) {

        System.out.println("-------------------");
        System.out.printf("Logged in as %s (%s)\n", e.getJDA().getSelfUser().getName(),
                          e.getJDA().getGuilds().toArray().length
        );
        System.out.printf("JDA Package Version: %s\n", (e.getClass().getPackage().getImplementationVersion()));
        System.out.printf("Java Version: %s\n", System.getProperty("java.version"));
        System.out.printf("Running On: %s\n", System.getProperty("os.name"));
        System.out.println("-------------------");

    }
}
