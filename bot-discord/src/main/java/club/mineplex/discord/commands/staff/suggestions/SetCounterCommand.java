package club.mineplex.discord.commands.staff.suggestions;

import club.mineplex.discord.BotUtil;
import club.mineplex.discord.Main;
import club.mineplex.discord.commands.IShop;
import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.objects.DiscordUser;
import club.mineplex.discord.objects.PermissionHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Objects;

public class SetCounterCommand extends SlashCommand implements IShop.Permissible {

    public SetCounterCommand() {
        super("setcounter", new OptionData(OptionType.INTEGER, "count", "The number to set the counter to", true));
        this.setBypassChannels(true);
    }

    @Override
    protected InteractionHook run(final DiscordUser sender, final SlashCommandEvent commandEvent) {

        final long counter = Objects.requireNonNull(commandEvent.getOption("count")).getAsLong();

        /* Setting counter */
        final int currentIdeas = Main.data.getOrDefault("idea-counter", Integer.class, 0);
        Main.data.set("idea-counter", counter);

        /* Response message */
        final EmbedBuilder eb = new EmbedBuilder()
                .setColor(Main.COLOR_GREEN)
                .setDescription(String.format(BotUtil.getEmojiAsMention(Main.yesEmoji) + " Ideas counter: ``%s ➜ %s``",
                                              currentIdeas,
                                              counter
                                )
                );
        Main.data.reload();

        return commandEvent.replyEmbeds(eb.build()).complete();
    }

    @Override
    public boolean hasPermission(final DiscordUser user, final Guild guild) {
        return user.hasPermissionInGuild(guild, PermissionHandler.PermissionLevel.STAFF);
    }
}
