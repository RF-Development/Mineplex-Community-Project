package club.mineplex.milestones.general;

import club.mineplex.milestones.Main;
import club.mineplex.milestones.utilities.objects.permissibles.IShop;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class UpdateStatus implements Runnable, IShop.Schedulable {

    @Override
    public ScheduledFuture<?> schedule(final ScheduledExecutorService ses) {
        return ses.scheduleAtFixedRate(this, 5, Main.config.changeStatusInterval, TimeUnit.SECONDS);
    }

    @Override
    public void run() {

        // Return if empty
        if (Main.config.botStatuses.size() == 0) {
            return;
        }

        final String status = Main.config.botStatuses.get(new Random().nextInt(Main.config.botStatuses.size()));
        Main.getJDA().getPresence().setActivity(Activity.playing(status));

    }

}
