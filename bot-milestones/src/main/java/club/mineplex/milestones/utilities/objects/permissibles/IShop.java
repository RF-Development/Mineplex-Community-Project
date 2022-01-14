package club.mineplex.milestones.utilities.objects.permissibles;

import club.mineplex.milestones.utilities.objects.discord.DiscordUser;
import net.dv8tion.jda.api.entities.Guild;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class IShop {

    public interface Permissible {
        boolean hasPermission(DiscordUser user, Guild guild);

    }

    public interface Cooldownable {
        double getCooldown();

    }

    public interface ICacheable {
        boolean updateCache();

    }

    public interface Schedulable {
        ScheduledFuture<?> schedule(ScheduledExecutorService ses);
    }

}
