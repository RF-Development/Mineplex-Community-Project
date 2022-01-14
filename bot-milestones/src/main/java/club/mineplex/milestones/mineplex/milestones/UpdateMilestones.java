package club.mineplex.milestones.mineplex.milestones;

import club.mineplex.milestones.Main;
import club.mineplex.milestones.configs.WebhookItem;
import club.mineplex.milestones.mineplex.milestones.models.StaffMember;
import club.mineplex.milestones.mineplex.milestones.parsing.MilestoneList;
import club.mineplex.milestones.mineplex.milestones.parsing.SubteamList;
import club.mineplex.milestones.mineplex.models.StaffList;
import club.mineplex.milestones.utilities.BotUtil;
import club.mineplex.milestones.utilities.Logger;
import club.mineplex.milestones.utilities.objects.permissibles.IShop;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZoneId;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class UpdateMilestones implements Runnable, IShop.Schedulable {

    public static StaffList staffList;
    public static SubteamList subteamList;
    public static MilestoneList milestoneList;

    @Override
    public ScheduledFuture<?> schedule(final ScheduledExecutorService ses) {
        final long initialDelay = BotUtil.getTimeUntil(ZoneId.of(Main.config.timezone), Main.config.milestonePostHour,
                                                       Main.config.milestonePostMinute, 0
        );
        Logger.log("Milestones will start posting in %s seconds.", Logger.LogLevel.DEBUG, String.valueOf(initialDelay));
        return ses.scheduleAtFixedRate(this, initialDelay, 24 * 3600, TimeUnit.SECONDS);
    }

    @Override
    public void run() {

        Logger.log("Now attempting to post milestones...");

        /*
        Update Milestone Data
         */
        try {
            this.updateWebsiteCache();
            milestoneList.fillMembers();
        } catch (final Exception ex) {
            Logger.log("Failed to update milestone website data cache: %s", Logger.LogLevel.ERROR, ex.getMessage());
            ex.printStackTrace();
        }

        if (milestoneList.staffMemberList.size() == 0) {
            Logger.log("There were no milestones today");
            return;
        }

        /*
        Get & post the milestone messages for each member
         */
        for (final StaffMember staffMember : milestoneList.staffMemberList) {
            final MilestoneMessage milestoneMessage = new MilestoneMessage(staffMember);
            milestoneMessage.setRandomMessage();

            for (final WebhookItem item : Main.config.webhookItems) {

                if (item.milestonePost == null) {
                    continue;
                }

                if (!item.milestonePost.enabled) {
                    continue;
                }

                milestoneMessage.postMilestoneMessage(item.webhookURL);

            }

        }

        Logger.log("Finished attempting to post milestones...");

    }

    public void updateWebsiteCache() throws NullPointerException {

        final String staffString = BotUtil.scrapeWebsite(Main.config.MINEPLEX_API_URL + "stafflist");
        staffList = new StaffList(new JSONObject(Objects.requireNonNull(staffString)));

        final String subteamString = BotUtil.scrapeWebsite(Main.config.MINEPLEX_API_URL + "subteams");
        subteamList = new SubteamList(new JSONArray(Objects.requireNonNull(subteamString)));

        final String milestoneString = BotUtil.scrapeWebsite(Main.config.MINEPLEX_API_URL + "milestones");
        milestoneList = new MilestoneList(new JSONObject(Objects.requireNonNull(milestoneString)));

    }

}
