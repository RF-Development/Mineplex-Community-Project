package club.mineplex.discord.commands.staff;

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

import java.util.Timer;
import java.util.TimerTask;

public class ReloadCommand extends SlashCommand implements IShop.Permissible {

    public ReloadCommand() {
        super("reload");
        this.setBypassChannels(true);
    }

    @Override
    protected InteractionHook run(final DiscordUser sender, final SlashCommandEvent commandEvent) {

        /* Response message */
        final EmbedBuilder eb = new EmbedBuilder()
                .setColor(Main.COLOR_GREEN)
                .setDescription(BotUtil.getEmojiAsMention(Main.yesEmoji)
                                        + "Reloaded configuration files! Check console for any errors.");

        /* Restarting */
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                Main.action(Main.SuperAction.RELOADCONFIG);
            }

        }, 500L);

        return commandEvent.replyEmbeds(eb.build()).complete();
    }

    @Override
    public boolean hasPermission(final DiscordUser user, final Guild guild) {
        return user.hasPermissionInGuild(guild, PermissionHandler.PermissionLevel.ADMIN);
    }

}
