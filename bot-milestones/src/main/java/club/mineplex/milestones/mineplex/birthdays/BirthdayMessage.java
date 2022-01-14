package club.mineplex.milestones.mineplex.birthdays;


import club.mineplex.milestones.Main;
import club.mineplex.milestones.mineplex.birthdays.models.ForumsMember;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;

import java.util.ArrayList;
import java.util.List;

public class BirthdayMessage {

    public final ArrayList<ForumsMember> members;
    public final boolean staffOnly;
    public final List<Integer> hiddenUsers;
    public String message;

    public BirthdayMessage(final ArrayList<ForumsMember> members, final boolean staffOnly, final List<Integer> hiddenUsers) {
        this.members = members;
        this.staffOnly = staffOnly;
        this.hiddenUsers = hiddenUsers;

    }

    public void setBirthdayMessage() {

        if (this.staffOnly) {
            this.members.removeIf(member -> !member.isStaff);
        }

        // Remove hidden members
        this.members.removeIf(member -> this.hiddenUsers.contains(member.id));

        final ArrayList<String> memberStrings = new ArrayList<>();
        for (final ForumsMember member : this.members) {
            memberStrings.add("**" + member.name + "**");
        }

        final StringBuilder birthdayListString = new StringBuilder();

        switch ((memberStrings.size())) {
            case 0:
                return;

            case 1:
                birthdayListString.append(memberStrings.get(0));
                break;

            case 2:
                birthdayListString.append(memberStrings.get(0)).append(" and ").append(memberStrings.get(1));
                break;

            default:

                for (int i = 0; i < memberStrings.size(); i++) {

                    if (i == memberStrings.size() - 1) {
                        birthdayListString.append("and ").append(memberStrings.get(i));
                        break;
                    }

                    birthdayListString.append(memberStrings.get(i)).append(", ");

                }


        }

        this.message = String.format(
                "`Birthday Notification`\n\nWe want to wish %s an amazing and happy birthday!\n\n%s",
                birthdayListString,
                (
                        (this.staffOnly) ?
                                "Thank you for everything you've done so far and we look forward to this next year with you on the team!"
                                :
                                        "It's been a pleasure having you around here at Mineplex and we look forward to this next year of fun & games."
                )
        );

    }

    /**
     * Post the birthday message to a URL
     */
    public void postBirthdayMessage(final String webhookURL) {

        final WebhookClientBuilder builder = new WebhookClientBuilder(webhookURL);
        builder.setThreadFactory((job) -> {
            final Thread thread = new Thread(job);
            thread.setName("Birthday Bot");
            thread.setDaemon(true);
            return thread;
        });

        builder.setWait(false);
        final WebhookClient client = builder.build();

        final WebhookMessageBuilder wmb = new WebhookMessageBuilder();
        wmb.setUsername("Mineplex Birthdays");
        wmb.setAvatarUrl(Main.getJDA().getSelfUser().getAvatarUrl());
        wmb.setContent(this.message);

        try {
            client.send(wmb.build()).get();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        client.close();
    }

}
