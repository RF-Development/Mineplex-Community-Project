package club.mineplex.milestones.configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WebhookItem {

    public final String webhookURL;
    public final BirthdayPost birthdayPost;
    public final MilestonePost milestonePost;
    public WebhookItem(final HashMap<String, Object> configItem) {

        this.webhookURL = (String) configItem.get("webhook-link");

        /*
        Parse birthday posts
         */
        if (configItem.containsKey("birthday-posts")) {
            final HashMap<String, Object> birthdayMap = (HashMap<String, Object>) configItem.get("birthday-posts");
            final boolean staffOnly = (boolean) birthdayMap.getOrDefault("staff-only", false);
            final boolean enabled = (boolean) birthdayMap.getOrDefault("enabled", true);
            final List<Integer> ignoredUsers =
                    (ArrayList<Integer>) birthdayMap.getOrDefault("ignored-users", new ArrayList<Integer>());
            this.birthdayPost = new BirthdayPost(staffOnly, ignoredUsers, enabled);
        } else {
            this.birthdayPost = null;
        }

        /*
        Parse milestone posts
         */
        if (configItem.containsKey("milestone-posts")) {
            final HashMap<String, Object> milestoneMap = (HashMap<String, Object>) configItem.get("milestone-posts");
            final boolean enabled = (boolean) milestoneMap.getOrDefault("enabled", true);
            this.milestonePost = new MilestonePost(enabled);

        } else {
            this.milestonePost = null;
        }

    }

    public static class BirthdayPost {

        public final boolean staffOnly;
        public final List<Integer> hiddenUsers;
        public final boolean enabled;

        public BirthdayPost(final boolean staffOnly, final List<Integer> arrayList, final boolean enabled) {
            this.staffOnly = staffOnly;
            this.hiddenUsers = arrayList;
            this.enabled = enabled;
        }

    }

    public static class MilestonePost {

        public final boolean enabled;

        public MilestonePost(final boolean enabled) {
            this.enabled = enabled;
        }

    }

}
