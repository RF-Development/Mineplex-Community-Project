package club.mineplex.discord.commands.staff.suggestions;

import club.mineplex.discord.BotUtil;
import club.mineplex.discord.Main;
import club.mineplex.discord.commands.IShop;
import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.objects.DiscordUser;
import club.mineplex.discord.objects.PermissionHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlacklistsCommand extends SlashCommand implements IShop.Permissible {

    public BlacklistsCommand() {
        super("blacklists");
        this.setBypassChannels(true);
    }

    @Override
    protected InteractionHook run(final DiscordUser sender, final SlashCommandEvent commandEvent) {

        final Optional<Guild> guildOpt = Optional.ofNullable(commandEvent.getGuild());
        if (!guildOpt.isPresent()) {
            return this.error(commandEvent);
        }

        final Guild guild = guildOpt.get();
        final List<Long> blacklisted = Main.data.getOrDefault("blacklisted", ArrayList.class, new ArrayList<>());

        /* Building the embed */
        final EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(BotUtil.getEmojiAsMention(Main.yesEmoji) + "  Retrieved Blacklisted Users");
        eb.setColor(Main.COLOR_GREEN);

        /* Looping through all blacklisted users */
        final List<String> column1 = new ArrayList<>();
        final List<String> column2 = new ArrayList<>();
        for (final long id : blacklisted) {

            final User user = Main.getJDA().getUserById(id);
            if (user != null && user.getMutualGuilds().contains(guild)) {
                final Member member = guild.getMember(user);
                if (member == null) {
                    continue;
                }

                if (column1.size() <= column2.size()) {
                    column1.add(member.getAsMention());
                } else {
                    column2.add(member.getAsMention());
                }
            }
        }

        /* In case nobody is blacklisted */
        if (column1.isEmpty() && column2.isEmpty()) {
            eb.setDescription("**There are no blacklisted users!**");
        }

        /* Building first column */
        if (!column1.isEmpty()) {
            final StringBuilder str = new StringBuilder();
            for (final String query : column1) {
                str.append(query).append("\n");
            }
            eb.addField("", str.toString(), true);
        }

        /* Buuilding second column */
        if (!column2.isEmpty()) {
            final StringBuilder str = new StringBuilder();
            for (final String query : column2) {
                str.append(query).append("\n");
            }
            eb.addField("", str.toString(), true);
        }

        /* Sending Response */
        return commandEvent.replyEmbeds(eb.build()).setEphemeral(true).complete();
    }

    @Override
    public boolean hasPermission(final DiscordUser user, final Guild guild) {
        return user.hasPermissionInGuild(guild, PermissionHandler.PermissionLevel.STAFF);
    }

}
