package club.mineplex.milestones.utilities;

import club.mineplex.milestones.Main;
import club.mineplex.milestones.configs.Config;
import club.mineplex.milestones.configs.ConfigLoader;
import club.mineplex.milestones.general.UpdateStatus;
import club.mineplex.milestones.mineplex.birthdays.UpdateBirthdays;
import club.mineplex.milestones.mineplex.milestones.UpdateMilestones;
import club.mineplex.milestones.utilities.objects.permissibles.Command;
import club.mineplex.milestones.utilities.objects.permissibles.IShop;
import club.mineplex.milestones.utilities.objects.permissibles.PermissionHandler;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

public class BotActions {
    public static void action(final SuperAction action) {
        action(action, true);
    }

    public static void action(final SuperAction action, final boolean response) {
        switch (action) {
            case START:
                BotActions.action(BotActions.SuperAction.RELOADCONFIG);

                final JDABuilder builder = JDABuilder.createDefault(Main.config.TOKEN)
                                                     .setMemberCachePolicy(MemberCachePolicy.ALL)
                                                     .enableIntents(GatewayIntent.GUILD_MEMBERS,
                                                              GatewayIntent.GUILD_PRESENCES,
                                                              GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_MESSAGES,
                                                              GatewayIntent.GUILD_MESSAGE_REACTIONS
                                               );

                builder.addEventListeners(
                        // EVENTS
                        new Main()
                );

                for (final Command command : Main.commands) {
                    if (response) {
                        Logger.log("[CMD] Loaded command '" + command.identifier + "'");
                    }
                    builder.addEventListeners(command);
                }

                try {
                    Main.setJDA(builder.build());
                } catch (final LoginException e) {
                    e.printStackTrace();
                }

                // Tasks to schedule
                final IShop.Schedulable[] tasks = new IShop.Schedulable[]{
                        new UpdateBirthdays(),
                        new UpdateMilestones(),
                        new UpdateStatus()
                };

                // Schedule them
                for (final IShop.Schedulable task : tasks) {
                    Main.tasks.add(task.schedule(Main.scheduler));
                }

                break;

            case SHUTDOWN:
                if (Main.getJDA() != null) {
                    Main.getJDA().shutdownNow();
                    if (response) {
                        Logger.log("Bye! ;)");
                    }
                }

                Main.setJDA(null);

                /* Stopping tasks */
                for (final ScheduledFuture<?> scheduledFuture : Main.tasks) {
                    scheduledFuture.cancel(true);
                }

                Main.tasks.clear();

                break;

            case RESTART:

                action(SuperAction.SHUTDOWN);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        action(SuperAction.START);
                    }
                }, 1500L);

                break;

            case RELOADCONFIG:
                Main.configLoader = new ConfigLoader("config.yml");
                Logger.log("[config.yml] Loaded: " + Main.configLoader.load());
                Main.config = new Config(Main.configLoader);

                Main.langLoader = new ConfigLoader("lang.yml");
                Logger.log("[lang.yml] Loaded: " + Main.langLoader.load());

                PermissionHandler.PermissionLevel.ADMIN.setRoles(Main.config.ADMIN_ROLES);
                PermissionHandler.PermissionLevel.STAFF.setRoles(Main.config.STAFF_ROLES);

                break;
        }
    }

    public enum SuperAction {
        SHUTDOWN("stop", "shutdown"), RELOADCONFIG("reload"),
        START("start"), RESTART("restart");

        private final String name;
        private final HashSet<String> aliases;

        SuperAction(final String name, final String... aliases) {
            this.name = name;
            this.aliases = new HashSet<>(Arrays.asList(aliases));
        }

        public boolean matches(final String command) {
            for (final String a : this.aliases) {
                if (command.equalsIgnoreCase(a)) {
                    return true;
                }
            }
            return command.equalsIgnoreCase(this.name);
        }
    }
}
