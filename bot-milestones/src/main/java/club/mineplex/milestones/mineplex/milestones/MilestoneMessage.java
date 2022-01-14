package club.mineplex.milestones.mineplex.milestones;

import club.mineplex.milestones.Main;
import club.mineplex.milestones.mineplex.milestones.models.StaffMember;
import club.mineplex.milestones.utilities.BotUtil;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MilestoneMessage {

    public final StaffMember member;
    public String message;

    public MilestoneMessage(final StaffMember member) {
        this.member = member;
    }

    private static String mapDisplayNames(final String displayName) {
        switch (displayName.toLowerCase(Locale.ROOT)) {
            case "leadership team":
                return "Leader";
            case "mod":
                return "Moderator";
            case "customer support":
                return "Customer Support member";
            case "social media":
                return "Social Media member";
            case "quality assurance":
                return "Quality Assurance member";
            case "forum management":
                return "Forums Manager";
            case "recruitment":
                return "Recruiter";
            case "community management":
                return "Community Manager";
            case "event squad":
                return "Event Squad member";
            default:
                return displayName;
        }
    }

    public static String[] getRandomUniques(final int number, final ArrayList<String> messages,
                                            final ArrayList<String> selectedMessages) {

        final Random r = new Random();

        if (number == 0 || messages.size() < 1) {
            final String[] stockArr = new String[selectedMessages.size()];
            return selectedMessages.toArray(stockArr);
        }

        final int newIndex = r.nextInt(messages.size());
        selectedMessages.add(messages.get(newIndex));
        messages.remove(newIndex);

        return getRandomUniques(number - 1, messages, selectedMessages);

    }

    /**
     * Set the milestone message
     */
    public void setRandomMessage() {
        try {

            this.message = String.format(
                    "`Milestone Notification!`\n\n%s\n\n%s %s\n\n%s",
                    this.randomCongratsMessage(), this.randomPersonalMessage(), Main.config.giftMessage,
                    Main.config.closingMessage.get(new Random().nextInt(Main.config.closingMessage.size()))
            );

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Post the milestone message to a URL
     */
    public void postMilestoneMessage(final String webhookURL) {

        final WebhookClientBuilder builder = new WebhookClientBuilder(webhookURL);
        builder.setThreadFactory((job) -> {
            final Thread thread = new Thread(job);
            thread.setName("Milestone Bot");
            thread.setDaemon(true);
            return thread;
        });

        builder.setWait(false);
        final WebhookClient client = builder.build();

        final WebhookMessageBuilder wmb = new WebhookMessageBuilder();
        wmb.setUsername(String.format("%s's Milestone", this.member.forumsName));
        if (this.member.avatar != null) {
            wmb.setAvatarUrl(this.member.avatar.avatarURL);
        }
        wmb.setContent(this.message);

        try {
            client.send(wmb.build()).get();
        } catch (final Exception ignored) {
        }

        client.close();
    }

    private String randomCongratsMessage() {
        final Random random = new Random();
        return Main.config.congratsMessages.get(random.nextInt(Main.config.congratsMessages.size()))
                                           .replaceAll("%name%", this.member.forumsName)
                                           .replaceAll("%year%", (random.nextInt(2) == 1) ? String.valueOf(
                                                   this.member.milestone) : BotUtil.numberToWordsUnder1K(
                                                   this.member.milestone));
    }

    private String randomPersonalMessage() {
        final LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());
        String displayName = null;
        final int numberOfMessages = ThreadLocalRandom.current().nextInt(2, 4);
        String configKey = null;

        // Get the first one
        for (final String key : Main.config.teamMessages.keySet()) {

            if (!this.member.mainTeams.contains(key)) {
                continue;
            }

            displayName = mapDisplayNames(key);
            configKey = key;
            break;
        }

        // No staff name, a.k.a broken forums tag
        if (displayName == null) {
            displayName = "Staff Member";
            configKey = "Other";
        }

        final String[] uniqueMessages =
                getRandomUniques(numberOfMessages, (ArrayList<String>) Main.config.teamMessages.get(configKey).clone(),
                                 new ArrayList<>()
                );
        final StringBuilder builder = new StringBuilder();

        // Create the unique message
        switch (uniqueMessages.length) {

            case 0:
                builder.append("dedicating themselves to hard work and devotion.");
                break;

            case 1:
                builder.append(uniqueMessages[0]);
                break;

            case 2:
                builder.append(uniqueMessages[0]).append(" and ").append(uniqueMessages[1]);
                break;

            default:

                for (int i = 0; i < uniqueMessages.length; i++) {

                    if (i == uniqueMessages.length - 1) {
                        builder.append("and ").append(uniqueMessages[i]);
                        break;
                    }

                    builder.append(uniqueMessages[i]).append(", ");

                }
        }

        // Parse the data
        return Main.config.teamMessageIntros.get(new Random().nextInt(Main.config.teamMessageIntros.size()))
                                            .replaceAll("%name%", this.member.forumsName)
                                            .replaceAll("%join-year%",
                                                        String.valueOf(currentDate.getYear() - this.member.milestone)
                                            )
                                            .replaceAll("%milestone%", (new Random().nextInt(2) == 1) ? String.valueOf(
                                                    this.member.milestone) : BotUtil.numberToWordsUnder1K(
                                                    this.member.milestone))
                                            .replaceAll("%team-name%", displayName)
                                            .replaceAll("%team-messages%", builder.toString()
                                            );

    }

}
