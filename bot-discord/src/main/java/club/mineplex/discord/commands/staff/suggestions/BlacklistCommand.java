package club.mineplex.discord.commands.staff.suggestions;

import club.mineplex.discord.BotUtil;
import club.mineplex.discord.Main;
import club.mineplex.discord.commands.IShop;
import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.objects.DiscordUser;
import club.mineplex.discord.objects.PermissionHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlacklistCommand extends SlashCommand implements IShop.Permissible {

    public BlacklistCommand() {
        super("blacklist",
              new OptionData(OptionType.USER, "user", "The user whose blacklist status you wish to toggle", true)
        );
        this.setBypassChannels(true);
    }

    @Override
    protected InteractionHook run(final DiscordUser sender, final SlashCommandEvent commandEvent) {

        final OptionMapping userOpt = Objects.requireNonNull(commandEvent.getOption("user"));
        final User user = userOpt.getAsUser();

        /* Getting user to (un)blacklist and getting current blacklist */
        final List<Long> blacklisted = Main.data.getOrDefault("blacklisted", ArrayList.class, new ArrayList<>());

        /* Building embed */
        final EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Main.COLOR_GREEN);

        /* Checking if user is blacklisted or not */
        if (blacklisted.contains(user.getIdLong())) {
            eb.setDescription(BotUtil.getEmojiAsMention(Main.yesEmoji) + user.getAsMention()
                                      + " has been ``unblacklisted`` from making suggestions.");
            blacklisted.remove(user.getIdLong());
        } else {
            eb.setDescription(BotUtil.getEmojiAsMention(Main.yesEmoji) + user.getAsMention()
                                      + " has been ``blacklisted`` from making suggestions.");
            blacklisted.add(user.getIdLong());
        }

        /* Saving and sending response */
        Main.data.set("blacklisted", blacklisted);
        return commandEvent.replyEmbeds(eb.build()).complete();
    }

    @Override
    public boolean hasPermission(final DiscordUser user, final Guild guild) {
        return user.hasPermissionInGuild(guild, PermissionHandler.PermissionLevel.STAFF);
    }

}