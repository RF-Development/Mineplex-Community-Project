package club.mineplex.milestones.utilities.objects.permissibles;

import club.mineplex.milestones.Main;
import club.mineplex.milestones.utilities.BotUtil;
import club.mineplex.milestones.utilities.objects.discord.DiscordUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Command extends ListenerAdapter {

    public String prefix = Main.config.PREFIX;
    public String description;
    public String identifier;
    public String[] aliases;
    private final HashMap<Long, Integer> cooldownUsers = new HashMap<>();
    private boolean bypassChannels = false;

    public Command(final String identifier, final String... aliases) {
        this.identifier = identifier;
        this.aliases = aliases;

        this.description =
                Main.langLoader.getOrDefault(this.identifier + "-description", String.class, "Default description");
    }

    @Override
    public void onGuildMessageReceived(final GuildMessageReceivedEvent e) {
        if (e.getAuthor().isBot()) {
            return;
        }
        if (e.isWebhookMessage()) {
            return;
        }

        /* Updating config stuff */
        this.description =
                Main.langLoader.getOrDefault(this.identifier + "-description", String.class, "Default description");

        /* Getting the command */
        final String[] split = e.getMessage().getContentRaw().split(" ");
        if (!split[0].startsWith(this.prefix)) {
            return;
        }
        if (!this.matches(split[0].substring(1))) {
            return;
        }

        /* Checking for permissions */
        final DiscordUser user = new DiscordUser(e.getMessage().getAuthor());
        if (this instanceof IShop.Permissible && !((IShop.Permissible) this).hasPermission(user,
                                                                                           e.getMessage().getGuild()
        )) {
            return;
        }

        /* Checking for cooldowns */
        if (this instanceof IShop.Cooldownable && this.cooldownUsers.containsKey(user.getUser().getIdLong())) {
            final int cooldown = (int) Math.floor(this.cooldownUsers.get(user.getUser().getIdLong()));
            final EmbedBuilder eb = new EmbedBuilder()
                    .setAuthor(user.getUser().getName() + "#" + user.getUser().getDiscriminator(),
                               user.getUser().getAvatarUrl()
                    )
                    .setDescription(BotUtil.getEmojiAsMention(Main.config.noEmoji) + String.format(
                            "You need to wait **%s** more seconds", cooldown))
                    .setColor(Main.config.COLOR_RED);

            e.getMessage().getTextChannel().sendMessage(eb.build()).queue();
            return;
        }

        /* Sending */
        final String[] args = Arrays.copyOfRange(split, 1, split.length);
        final boolean success = this.run(e.getMessage(), user, args);

        /* Applying cooldown, if applies */
        if (this instanceof IShop.Cooldownable && success) {
            this.cooldownUsers.put(user.getUser().getIdLong(),
                                   (int) Math.floor(((IShop.Cooldownable) this).getCooldown())
            );

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    final int toSet = (int) Math.floor(Command.this.cooldownUsers.get(user.getUser().getIdLong())) - 1;

                    /* Cancelling cooldown if it's lesser than 0 */
                    if (toSet < 0) {
                        Command.this.cooldownUsers.remove(user.getUser().getIdLong());
                        this.cancel();
                    } else {
                        Command.this.cooldownUsers.replace(user.getUser().getIdLong(), toSet);
                    }

                }
            }, 0, 1000L);
        }
    }

    protected abstract boolean run(Message msg, DiscordUser sender, String[] args);

    public abstract String usage();

    private boolean matches(final String query) {
        for (final String alias : this.aliases) {
            if (query.equalsIgnoreCase(alias)) {
                return true;
            }
        }
        return query.equalsIgnoreCase(this.identifier);
    }

    protected boolean args(final TextChannel channel, final DiscordUser sender) {
        final EmbedBuilder eb = new EmbedBuilder()
                .setDescription("**Usage:** `" + this.prefix + this.usage() + "`")
                .setColor(Main.config.COLOR_RED);

        channel.sendMessage(eb.build()).queue();
        return false;
    }

    protected void setBypassChannels(final boolean bypassChannels) {
        this.bypassChannels = bypassChannels;
    }

    protected void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

}