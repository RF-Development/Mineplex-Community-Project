package club.mineplex.discord.commands.all;

import club.mineplex.discord.Main;
import club.mineplex.discord.commands.IShop;
import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.objects.DiscordUser;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.ArrayList;
import java.util.Random;

public class JokeCommand extends SlashCommand implements IShop.Cooldownable {

    public JokeCommand() {
        super("joke");
    }

    @Override
    protected InteractionHook run(final DiscordUser sender, final SlashCommandEvent commandEvent) {
        final ArrayList<String> jokes = Main.jokes.getOrDefault("jokes", ArrayList.class, new ArrayList<>());

        String joke = "There was no jokes to display ):";
        if (!jokes.isEmpty()) {
            joke = jokes.get(new Random().nextInt(jokes.size()));
        }

        return commandEvent.reply(joke).complete();
    }

    @Override
    public double getCooldown() {
        return Main.config.getOrDefault("jokes-cooldown", Integer.class, 10);
    }
}
