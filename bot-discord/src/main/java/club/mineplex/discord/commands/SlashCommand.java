package club.mineplex.discord.commands;

import club.mineplex.discord.BotUtil;
import club.mineplex.discord.Main;
import club.mineplex.discord.objects.DiscordUser;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public abstract class SlashCommand extends ListenerAdapter {

    protected final CommandData commandData;
    private final HashMap<Long, Integer> cooldownUsers = new HashMap<>();
    public String description;
    public String identifier;
    private boolean bypassChannels = false;

    public SlashCommand(final String identifier, final OptionData... options) {
        this.identifier = identifier;

        this.description =
                Main.lang.getOrDefault(this.identifier + "-description", String.class, "Default description");
        this.commandData = new CommandData(identifier, this.description);
        if (options != null && options.length != 0) {
            this.commandData.addOptions(options);
        }
    }

    @Override
    public void onSlashCommand(final SlashCommandEvent e) {
        if (e.getUser().isBot()) {
            return;
        }
        if (!e.getName().equalsIgnoreCase(this.identifier)) {
            return;
        }

        /* Command channels check */
        if (!this.bypassChannels && !BotUtil.getCommandChannels().contains(e.getTextChannel().getIdLong())) {
            e.reply(BotUtil.getEmojiAsMention(Main.noEmoji) + " Please use this command in a commands channel!")
             .setEphemeral(true)
             .queue();
            return;
        }

        /* Checking for permissions */
        final DiscordUser user = new DiscordUser(e.getUser());
        if (this instanceof IShop.Permissible && !((IShop.Permissible) this).hasPermission(user, e.getGuild())) {
            e.reply(BotUtil.getEmojiAsMention(Main.noEmoji) + " You do not have permission to do this!")
             .setEphemeral(true)
             .queue();
            return;
        }

        /* Checking for cooldowns */
        if (this instanceof IShop.Cooldownable && this.cooldownUsers.containsKey(user.getUser().getIdLong())) {
            final int cooldown = (int) Math.floor(this.cooldownUsers.get(user.getUser().getIdLong()));
            e.reply(BotUtil.getEmojiAsMention(Main.noEmoji) + String.format("You need to wait **%s** more seconds",
                                                                            cooldown
             ))
             .setEphemeral(true)
             .queue();
            return;
        }

        /* Sending */
        this.run(user, e);

        /* Applying cooldown, if applies */
        if (this instanceof IShop.Cooldownable) {
            this.cooldownUsers.put(user.getUser().getIdLong(),
                                   (int) Math.floor(((IShop.Cooldownable) this).getCooldown())
            );

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    final int toSet = (int) Math.floor(SlashCommand.this.cooldownUsers.get(user.getUser().getIdLong())) - 1;

                    /* Cancelling cooldown if it's lesser than 0 */
                    if (toSet < 0) {
                        SlashCommand.this.cooldownUsers.remove(user.getUser().getIdLong());
                        this.cancel();
                    } else {
                        SlashCommand.this.cooldownUsers.replace(user.getUser().getIdLong(), toSet);
                    }

                }
            }, 0, 1000L);
        }
    }

    protected abstract InteractionHook run(DiscordUser sender, SlashCommandEvent commandEvent);

    protected InteractionHook error(final SlashCommandEvent commandEvent) {
        return this.error(commandEvent, "An internal error has occurred!");
    }

    protected InteractionHook error(final SlashCommandEvent commandEvent, final String error) {
        final String text = BotUtil.getEmojiAsMention(Main.noEmoji) + " " + error;
        commandEvent.reply(text).setEphemeral(true).queue(
                interactionHook -> {
                },
                throwable -> commandEvent.getHook().editOriginal(text).queue()
        );
        return commandEvent.getHook();
    }

    protected void setBypassChannels(final boolean bypassChannels) {
        this.bypassChannels = bypassChannels;
    }

    public CommandData getCommandData() {
        return this.commandData;
    }
}