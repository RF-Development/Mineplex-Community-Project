package club.mineplex.milestones.mineplex.birthdays;

import club.mineplex.milestones.Main;
import club.mineplex.milestones.configs.WebhookItem;
import club.mineplex.milestones.mineplex.birthdays.models.ForumsMember;
import club.mineplex.milestones.mineplex.birthdays.parsing.BirthdayList;
import club.mineplex.milestones.mineplex.models.StaffList;
import club.mineplex.milestones.utilities.BotUtil;
import club.mineplex.milestones.utilities.Logger;
import club.mineplex.milestones.utilities.objects.permissibles.IShop;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class UpdateBirthdays implements Runnable, IShop.Schedulable {

    public static StaffList staffList;
    public static BirthdayList birthdayList;

    @Override
    public ScheduledFuture<?> schedule(final ScheduledExecutorService ses) {
        final long initialDelay = BotUtil.getTimeUntil(ZoneId.of(Main.config.timezone), Main.config.birthdayPostHour,
                                                       Main.config.birthdayPostMinute, 0
        );
        Logger.log("Birthdays will start posting in %s seconds.", Logger.LogLevel.DEBUG, String.valueOf(initialDelay));
        return ses.scheduleAtFixedRate(this, initialDelay, 24 * 3600, TimeUnit.SECONDS);
    }

    @Override
    public void run() {

        Logger.log("Now attempting to post birthdays...");

        /*
        Update Milestone Data
         */
        try {
            this.updateWebsiteCache();
        } catch (final Exception ex) {
            Logger.log("Failed to update birthday website data cache: %s", Logger.LogLevel.ERROR, ex.getMessage());
            ex.printStackTrace();
        }

        if (!(birthdayList.forumsMembers.size() > 0)) {
            Logger.log("There were no birthdays today");
            return;
        }

        for (final WebhookItem item : Main.config.webhookItems) {

            if (item.birthdayPost == null) {
                continue;
            }

            if (!item.birthdayPost.enabled) {
                continue;
            }

            final BirthdayMessage birthdayMessage = new BirthdayMessage(
                    (ArrayList<ForumsMember>) birthdayList.forumsMembers.clone(), item.birthdayPost.staffOnly,
                    item.birthdayPost.hiddenUsers
            );

            birthdayMessage.setBirthdayMessage();

            if (birthdayMessage.message == null) {
                Logger.log("There were no birthdays after applying staff-only and ignored-user filters",
                           Logger.LogLevel.WARN
                );
                continue;
            }

            try {
                birthdayMessage.postBirthdayMessage(item.webhookURL);
            } catch (final Exception e) {
                e.printStackTrace();
            }

        }

        Logger.log("Finished attempting to post birthdays...");

    }

    public void updateWebsiteCache() throws NullPointerException {

        final String staffString = BotUtil.scrapeWebsite(Main.config.MINEPLEX_API_URL + "stafflist");
        staffList = new StaffList(new JSONObject(Objects.requireNonNull(staffString)));


        final String birthdayString = BotUtil.scrapeWebsite(Main.config.MINEPLEX_API_URL + "birthdays");
        birthdayList = new BirthdayList(new JSONArray(Objects.requireNonNull(birthdayString)));

    }

}
