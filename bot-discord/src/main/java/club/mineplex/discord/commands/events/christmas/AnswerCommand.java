package club.mineplex.discord.commands.events.christmas;

import club.mineplex.discord.Main;
import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.objects.DiscordUser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnswerCommand extends SlashCommand {

    List<Long> alreadyAnswered = new ArrayList<>();

    public AnswerCommand() {
        super("answer",
              new OptionData(OptionType.STRING, "answer", "Your answer to the current trivia question", true)
        );
        this.setBypassChannels(true);
    }

    @Override
    protected InteractionHook run(final DiscordUser sender, final SlashCommandEvent commandEvent) {


        final User user = sender.getUser();
        if (this.alreadyAnswered.contains(user.getIdLong())) {
            return this.error(commandEvent, "You have already voted!");
        }

        final Optional<Guild> guildOpt = Optional.ofNullable(commandEvent.getGuild());
        if (!guildOpt.isPresent()) {
            return this.error(commandEvent);
        }

        final Guild guild = guildOpt.get();
        final TextChannel sendChannel = guild.getTextChannelsByName(Main.christmasTriviaChannel, true).get(0);
        if (sendChannel == null) {
            return this.error(commandEvent);
        }

        final Optional<OptionMapping> answerOpt = Optional.ofNullable(commandEvent.getOption("answer"));
        if (!answerOpt.isPresent()) {
            return this.error(commandEvent);
        }

        final String answer = answerOpt.get().getAsString();
        this.alreadyAnswered.add(user.getIdLong());
        sendChannel.sendMessage(String.format("**%s#%s:** %s", user.getName(), user.getDiscriminator(), answer))
                   .queue();

        return commandEvent.reply("**Successfully submitted your answer!**").setEphemeral(true).complete();
    }
}
