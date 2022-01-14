package club.mineplex.discord;

import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.commands.all.*;
import club.mineplex.discord.commands.events.christmas.AnswerCommand;
import club.mineplex.discord.commands.events.christmas.RestartTriviaCommand;
import club.mineplex.discord.commands.events.christmas.ResultsCommand;
import club.mineplex.discord.commands.events.christmas.VoteCommand;
import club.mineplex.discord.commands.staff.ReloadCommand;
import club.mineplex.discord.commands.staff.suggestions.BlacklistCommand;
import club.mineplex.discord.commands.staff.suggestions.BlacklistsCommand;
import club.mineplex.discord.commands.staff.suggestions.SetCounterCommand;
import club.mineplex.discord.commands.staff.suggestions.SuggestionsCommand;
import club.mineplex.discord.events.*;
import club.mineplex.discord.events.joinleave.LeaveMessage;
import club.mineplex.discord.events.joinleave.WelcomeMessage;
import club.mineplex.discord.events.reactions.BoardAddEvent;
import club.mineplex.discord.events.reactions.CustomReactions;
import club.mineplex.discord.events.reactions.MemeUpvotes;
import club.mineplex.discord.events.roles.ReactionRoles;
import club.mineplex.discord.objects.PermissionHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class Main extends ListenerAdapter {

    /* COLORS */
    public static final int COLOR_RED = 0xcc544b;
    public static final int COLOR_GREEN = 0x50b56b;
    public static final int COLOR_WHITE = 0xecf0f1;
    public static final int COLOR_BLUE = 0x80d9e7;
    public static final int COLOR_YELLOW = 0xe7cc12;
    /* CACHE */
    private static final List<SlashCommand> commands = new ArrayList<>();
    private static final Set<Timer> tasks = new HashSet<>();
    /* CHRISTMAS */
    public static String christmasTriviaChannel;
    public static Config christmas;
    /* VERIFICATION */
    public static long verifyAlertChannel;
    public static long verifiedRole;
    public static long unverifiedRole;
    /* EMOJIS */
    public static long yesEmoji;
    public static long noEmoji;
    public static long neutralEmoji;
    public static long boostEmoji;
    public static long botEmoji;
    public static long upvoteEmoji;
    public static long downvoteEmoji;
    /* STATIC IDs */
    public static ArrayList<Long> STAFF_ROLES;
    public static ArrayList<Long> ADMIN_ROLES;
    public static ArrayList<Long> ADMIN_USERS;
    public static ArrayList<Long> CMD_CHANNELS;
    public static ArrayList<Long> ARCHIVE_ROLES;
    public static ArrayList<Long> MEME_CHANNELS;
    /* VARIABLES */
    public static String PREFIX;
    public static String TOKEN;
    public static String TAG_PREFIX;
    public static Guild HOME_GUILD;
    public static long HOME_GUILD_ID;
    public static double cacheRefreshMinutes;
    public static int elementsPerPageEmbed;
    public static long blacklistRole;
    public static long welcomeAndLeaveChannel;
    public static long bugReportsChannel;
    public static String welcomeEmbedMessage;
    public static String leaveEmbedMessage;
    public static String identifier;
    public static Config config;
    public static Config data;
    public static Config lang;
    public static Config jokes;
    public static TextFile filter;
    public static double minimumSearchSimilarity = 0.18;
    /* MODULE MANAGEMENT */
    public static boolean MUSIC_ENABLED;
    public static boolean ARCHIVE_ENABLED;
    public static boolean FILTER_ENABLED;
    public static boolean COUNTERS_ENABLED;
    public static boolean GIVEAWAYS_ENABLED;
    public static boolean BUGREPORTS_ENABLED;
    /* CONSTANTS */
    private static JDA jda;

    public static void main(final String[] args) {

        /* ATTEMPTING TO LOG INTO THE BOT */
        action(SuperAction.RELOADCONFIG);

        /* CONSOLE COMMANDS */
        final Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            final String input = scanner.next();
            for (final SuperAction sa : SuperAction.values()) {
                if (sa.matches(input)) {
                    action(sa);
                }
            }
        }

    }

    public static void log(final String text) {
        System.out.println(text);
    }

    public static void action(final SuperAction action) {
        action(action, true);
    }

    public static void action(final SuperAction action, final boolean response) {
        switch (action) {
            case START:
                final JDABuilder builder = JDABuilder.createDefault(TOKEN)
                                                     .setMemberCachePolicy(MemberCachePolicy.ALL)
                                                     .enableIntents(GatewayIntent.GUILD_MEMBERS,
                                                                    GatewayIntent.GUILD_PRESENCES,
                                                                    GatewayIntent.GUILD_EMOJIS,
                                                                    GatewayIntent.GUILD_MESSAGES,
                                                                    GatewayIntent.GUILD_MESSAGE_REACTIONS
                                                     );

                registerAll();

                builder.addEventListeners(
                        new MessageDeleter(),
                        new AutoRoles(),
                        new AutoResponses(),
                        new ReactionRoles(),
                        new BoardAddEvent(),
                        new CustomReactions(),
                        new Filter(),
                        new MemeUpvotes(),
                        new PagedEmbedReactions(),
                        new ReadyActions(),
                        new WelcomeMessage(),
                        new LeaveMessage()
//                        new WrappedListeners()
                );

                for (final SlashCommand command : commands) {
                    if (response) {
                        log("[CMD] Loaded command '" + command.identifier + "'");
                    }
                    builder.addEventListeners(command);
                }

                try {
                    jda = builder.build();
                } catch (final LoginException e) {
                    e.printStackTrace();
                }

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        HOME_GUILD = getJDA().getGuildById(config.get("home-guild-id", Long.class));
                        commands.forEach(cmd -> HOME_GUILD.upsertCommand(cmd.getCommandData()).queue());
                        startAll();
                    }
                }, 5000L);

                break;

            case SHUTDOWN:
                if (jda != null) {
                    jda.shutdownNow();
                    if (response) {
                        log("Bye! ;)");
                    }
                }

                jda = null;

                /* Stopping tasks */
                for (final Timer timer : tasks) {
                    timer.cancel();
                }
                tasks.clear();

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
                config = new Config("config.yml");
                log("[config.yml] Loaded: " + config.load());

                data = new Config("data.yml");
                log("[data.yml] Loaded: " + data.load());

                lang = new Config("lang.yml");
                log("[lang.yml] Loaded: " + lang.load());

                jokes = new Config("jokes.yml");
                log("[jokes.yml] Loaded: " + jokes.load());

                filter = new TextFile("filter.txt");
                log("[filter.txt] Loaded: " + filter.load());

                christmas = new Config("christmas_event.yml");
                log("[christmas_event.yml] Loaded: " + christmas.load());
                christmasTriviaChannel = christmas.get("trivia-channel", String.class);

                HOME_GUILD_ID = config.get("home-guild-id", Long.class);
                identifier = config.get("identifier", String.class);

                PREFIX = config.getOrDefault("bot-prefix", String.class, "!");
                TAG_PREFIX = config.getOrDefault("tag-command-prefix", String.class, "-");
                TOKEN = config.get("bot-token", String.class);
                elementsPerPageEmbed = config.getOrDefault("elements-per-embed-page", Integer.class, 15);
                cacheRefreshMinutes = config.getOrDefault("cache-refresh-minutes", Double.class, 5D);

                STAFF_ROLES = config.getOrDefault("staff-roles", ArrayList.class, new ArrayList<>());
                ADMIN_ROLES = config.getOrDefault("admin-roles", ArrayList.class, new ArrayList<>());
                ADMIN_USERS = config.getOrDefault("admin-users", ArrayList.class, new ArrayList<>());
                ARCHIVE_ROLES = config.getOrDefault("archive-roles", ArrayList.class, new ArrayList<>());
                CMD_CHANNELS = config.getOrDefault("cmd-channels", ArrayList.class, new ArrayList<>());
                MEME_CHANNELS = config.getOrDefault("meme-channels", ArrayList.class, new ArrayList<>());

                FILTER_ENABLED = config.getOrDefault("filter-enabled", Boolean.class, false);
                ARCHIVE_ENABLED = config.getOrDefault("archive-enabled", Boolean.class, false);
                MUSIC_ENABLED = config.getOrDefault("music-enabled", Boolean.class, false);
                COUNTERS_ENABLED = config.getOrDefault("counters-enabled", Boolean.class, false);
                GIVEAWAYS_ENABLED = config.getOrDefault("giveaways-enabled", Boolean.class, false);
                BUGREPORTS_ENABLED = config.getOrDefault("bugreports-enabled", Boolean.class, false);

                blacklistRole = config.getOrDefault("dblacklist-role", Long.class, -1L);
                bugReportsChannel = config.getOrDefault("bugreports-channel", Long.class, -1L);
                welcomeAndLeaveChannel = config.getOrDefault("optional-welcome-leave-channel", Long.class, (long) -1);
                welcomeEmbedMessage = config.getOrDefault("optional-welcome-message", String.class, "");
                leaveEmbedMessage = config.getOrDefault("optional-leave-message", String.class, "");
                yesEmoji = config.get("yes-emoji", Long.class);
                noEmoji = config.get("no-emoji", Long.class);
                neutralEmoji = config.get("neutral-emoji", Long.class);
                boostEmoji = config.get("boost-emoji", Long.class);
                botEmoji = config.get("bot-emoji", Long.class);
                upvoteEmoji = config.get("upvote-emoji", Long.class);
                downvoteEmoji = config.get("downvote-emoji", Long.class);

                PermissionHandler.PermissionLevel.ADMIN.setRoles(ADMIN_ROLES);
                PermissionHandler.PermissionLevel.STAFF.setRoles(STAFF_ROLES);

                Filter.updateFilter();

                action(SuperAction.RESTART, false);

                break;
        }
    }

    private static void registerAll() {
        commands.clear();
        commands.addAll(Arrays.asList(
                new SuggestCommand(),
                new ServerCommand(),
                new UserCommand(),
                new PingCommand(),
                new JokeCommand(),
//                new StaffCommand(),
//                new SubteamsCommand(),
                new BugReportCommand(),

                // CHRISTMAS
                new AnswerCommand(),
                new ResultsCommand(),
                new RestartTriviaCommand(),
                new VoteCommand(),

                //STAFF
                new BlacklistCommand(),
                new BlacklistsCommand(),
                new SetCounterCommand(),
                new SuggestionsCommand(),

                //SUPER
                new ReloadCommand()

        ));

    }

    private static void startAll() {

        /* Get values from config & set minimums*/
        final int voiceChannelUpdateInterval =
                Math.max(305, Main.config.getOrDefault("voice-channel-update-interval", Integer.class, 300));
        final int changeStatusInterval =
                Math.max(15, Main.config.getOrDefault("status-update-interval", Integer.class, 60));

        final boolean includeBedrockCount = Main.config.getOrDefault("include-bedrock-count", Boolean.class, true);

        final DecimalFormat textFormatter = new DecimalFormat("#,###");

        if (COUNTERS_ENABLED && HOME_GUILD != null) {

            /* MINEPLEX PLAYER UPDATES */
            final Timer player = new Timer();
            player.schedule(new TimerTask() {

                @Override
                public void run() {

                    /* Getting mineplex's player count */
                    long playerCount;

                    try {
                        final URL url = new URL("https://api.mcsrvstat.us/2/mineplex.com");
                        final BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                        String str, t = "";
                        while (null != (str = br.readLine())) {
                            t += str;
                        }

                        final JSONObject json = new JSONObject(t);

                        if (!json.getBoolean("online")) {
                            throw new Exception();
                        }

                        playerCount = json.getJSONObject("players").getLong("online");
                    } catch (final Exception e) {
                        playerCount = 0;
                    }

                    if (includeBedrockCount) {

                        try {
                            final URL url =
                                    new URL("https://api.bedrockinfo.com/v1/status?server=mco.mineplex.com&port=19132");
                            final BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                            String str, t = "";
                            while (null != (str = br.readLine())) {
                                t += str;
                            }

                            final JSONObject json = new JSONObject(t);

                            playerCount += json.getInt("Players");

                        } catch (final Exception ignored) {
                        }
                    }

                    /* Checking if player count channel exists */
                    if (HOME_GUILD == null) {
                        return;
                    }
                    if (HOME_GUILD.getVoiceChannels().isEmpty()) {
                        return;
                    }
                    final VoiceChannel channel = HOME_GUILD.getVoiceChannelById(
                            Main.config.getOrDefault("player-count-channel", Long.class, 0L));
                    if (channel == null) {
                        return;
                    }

                    /* Updating name */
                    final String name =
                            Main.config.getOrDefault("player-count-name", String.class, "Player Count: %count%");

                    final String newName = name.replaceAll("%count%", textFormatter.format(playerCount));
                    channel.getManager().setName(newName).queue();
                }

            }, 0, voiceChannelUpdateInterval * 1000L);

            /* DISCORD MEMBER COUNT */
            final Timer member = new Timer();
            member.schedule(new TimerTask() {
                @Override
                public void run() {
                    /* Checking if player count channel exists */
                    if (HOME_GUILD == null) {
                        return;
                    }
                    if (HOME_GUILD.getVoiceChannels().isEmpty()) {
                        return;
                    }
                    final VoiceChannel channel = HOME_GUILD.getVoiceChannelById(
                            Main.config.getOrDefault("member-count-channel", Long.class, 0L));
                    if (channel == null) {
                        return;
                    }

                    /* Updating name */
                    final String name =
                            Main.config.getOrDefault("member-count-name", String.class, "Member Count: %count%");
                    final String newName =
                            name.replaceAll("%count%", textFormatter.format(HOME_GUILD.getMemberCount()));
                    channel.getManager().setName(newName).queue();
                }
            }, 0, voiceChannelUpdateInterval * 1000L);


            /* DISCORD BOT STATUS */
            final Timer status = new Timer();
            final String[] oldStatus = {""};

            status.schedule(new TimerTask() {
                @Override
                public void run() {
                    String newStatus;

                    final ArrayList<String> botStatuses =
                            Main.config.getOrDefault("bot-statuses", ArrayList.class, new ArrayList<>());

                    // Return if empty
                    if (botStatuses.size() == 0) {
                        return;
                    }

                    do {
                        newStatus = botStatuses.get(new Random().nextInt(botStatuses.size()));
                    } while (newStatus.equals(oldStatus[0]));

                    oldStatus[0] = newStatus;

                    jda.getPresence().setActivity(Activity.playing(newStatus));

                }
            }, 0, changeStatusInterval * 1000L);

            tasks.add(player);
            tasks.add(member);
            tasks.add(status);

        }
    }

    public static List<SlashCommand> getCommands() {
        return commands;
    }

    public static JDA getJDA() {
        return jda;
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