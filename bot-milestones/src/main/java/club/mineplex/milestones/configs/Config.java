package club.mineplex.milestones.configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Config {

    /* COLORS */
    public final int COLOR_RED = 0xcc544b;
    public final int COLOR_GREEN = 0x50b56b;
    public final int COLOR_WHITE = 0xecf0f1;
    public final int COLOR_BLUE = 0x80d9e7;
    public final int COLOR_YELLOW = 0xe7cc12;

    /* EMOJIS */
    public long yesEmoji;
    public long noEmoji;
    public long neutralEmoji;
    public long boostEmoji;
    public long botEmoji;
    public long upvoteEmoji;
    public long downvoteEmoji;

    /* STATIC IDs */
    public ArrayList<Long> STAFF_ROLES;
    public ArrayList<Long> ADMIN_ROLES;
    public ArrayList<Long> ADMIN_USERS;
    public ArrayList<String> staffRanks;

    /* VARIABLES */
    public String PREFIX;
    public String TOKEN;
    public String MINEPLEX_API_URL;
    public int birthdayPostMinute;
    public int birthdayPostHour;
    public int milestonePostHour;
    public int milestonePostMinute;

    public String timezone;

    public ArrayList<String> botStatuses;
    public int changeStatusInterval;

    public ArrayList<String> congratsMessages;
    public ArrayList<WebhookItem> webhookItems;
    public HashMap<String, ArrayList<String>> teamMessages;
    public ArrayList<String> teamMessageIntros;
    public String giftMessage;
    public ArrayList<String> closingMessage;

    public Config(final ConfigLoader config) {

        this.PREFIX = config.getOrDefault("bot-prefix", String.class, "!");
        this.TOKEN = config.get("bot-token", String.class);
        this.MINEPLEX_API_URL = config.getOrDefault("api-base-url", String.class, null);

        this.staffRanks = config.getOrDefault("staff-ranks", ArrayList.class, new ArrayList<>());
        this.birthdayPostHour = config.getOrDefault("birthday-post-hour", Integer.class, 0);
        this.birthdayPostMinute = config.getOrDefault("birthday-post-minute", Integer.class, 0);

        this.milestonePostHour = config.getOrDefault("milestone-post-hour", Integer.class, 0);
        this.milestonePostMinute = config.getOrDefault("milestone-post-minute", Integer.class, 0);

        this.STAFF_ROLES = config.getOrDefault("staff-roles", ArrayList.class, new ArrayList<>());
        this.ADMIN_ROLES = config.getOrDefault("admin-roles", ArrayList.class, new ArrayList<>());
        this.ADMIN_USERS = config.getOrDefault("admin-users", ArrayList.class, new ArrayList<>());

        this.yesEmoji = config.get("yes-emoji", Long.class);
        this.noEmoji = config.get("no-emoji", Long.class);
        this.neutralEmoji = config.get("neutral-emoji", Long.class);
        this.boostEmoji = config.get("boost-emoji", Long.class);
        this.botEmoji = config.get("bot-emoji", Long.class);
        this.upvoteEmoji = config.get("upvote-emoji", Long.class);
        this.downvoteEmoji = config.get("downvote-emoji", Long.class);

        this.timezone = config.getOrDefault("timezone-name", String.class, "America/Montreal");

        this.botStatuses = config.getOrDefault("bot-statuses", ArrayList.class, new ArrayList<>());
        this.changeStatusInterval = Math.max(15, config.getOrDefault("status-update-interval", Integer.class, 60));

        this.congratsMessages = config.get("congrats-messages", ArrayList.class);

        final HashMap<String, HashMap<String, Object>> hashItems = config.get("webhook-items", LinkedHashMap.class);
        final ArrayList<HashMap<String, Object>> rawItems = (hashItems == null) ? null : new ArrayList<>();

        if (rawItems != null) {

            for (final String key : hashItems.keySet()) {
                rawItems.add(hashItems.get(key));
            }

            this.webhookItems = this.getWebhookItems(rawItems);

        }

        this.teamMessages = config.get("team-messages", HashMap.class);
        this.teamMessageIntros = config.get("team-message-intros", ArrayList.class);
        this.giftMessage = config.get("gift-message", String.class);
        this.closingMessage = config.get("closing-message", ArrayList.class);
    }

    public ArrayList<WebhookItem> getWebhookItems(final ArrayList<HashMap<String, Object>> rawItems) {

        final ArrayList<WebhookItem> webhookItems = new ArrayList<>();

        for (final HashMap<String, Object> item : rawItems) {
            webhookItems.add(new WebhookItem(item));

        }

        return webhookItems;

    }

}
