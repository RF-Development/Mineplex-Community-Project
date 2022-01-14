package club.mineplex.discord.commands;

import club.mineplex.discord.objects.DiscordUser;
import net.dv8tion.jda.api.entities.Guild;

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

}
