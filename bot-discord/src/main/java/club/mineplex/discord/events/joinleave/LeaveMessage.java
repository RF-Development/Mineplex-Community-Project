package club.mineplex.discord.events.joinleave;

import club.mineplex.discord.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;

public class LeaveMessage extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(final GuildMemberRemoveEvent e) {

        // Get send Channel
        if (Main.welcomeAndLeaveChannel == -1) {
            return;
        }

        // Return if not the home guild
        if (!e.getGuild().getId().equals(Main.HOME_GUILD.getId())) {
            return;
        }

        // Check for Leave Message
        if (Main.leaveEmbedMessage.equals("")) {
            return;
        }

        final TextChannel sendChannel = e.getGuild().getTextChannelById(Main.welcomeAndLeaveChannel);
        if (sendChannel == null) {
            System.out.println("[WARN] Welcome channel is null...");
            return;
        }


        // Get Title & Description from Config
        String rawString = Main.leaveEmbedMessage;
        rawString = rawString.replaceAll("%name%", e.getUser().getName());
        rawString = rawString.replaceAll("%id%", e.getUser().getId());
        rawString = rawString.replaceAll("%server%", e.getGuild().getName());

        String[] embedStringArray = rawString.split(";");

        final String embedTitle = embedStringArray[0];
        embedStringArray = Arrays.copyOfRange(embedStringArray, 1, embedStringArray.length);

        final String embedDesc = String.join("\n", embedStringArray);

        // Create The Embed
        final EmbedBuilder eb = new EmbedBuilder()
                .setColor(Main.COLOR_BLUE)
                .setTitle(embedTitle)
                .setDescription(embedDesc)
                .setThumbnail(e.getUser().getAvatarUrl());

        // Send the Message
        assert sendChannel != null;
        sendChannel.sendMessage(eb.build()).queue();
    }

}