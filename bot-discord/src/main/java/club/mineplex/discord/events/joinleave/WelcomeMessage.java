package club.mineplex.discord.events.joinleave;

import club.mineplex.discord.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class WelcomeMessage extends ListenerAdapter {

    public static void sendWelcomeAttachment(final String filePath, final String fileName, final TextChannel sendChannel) {
        final File file = new File(filePath);

        // Send the File
        sendChannel.sendFile(file, fileName).complete();

    }

    @Override
    public void onGuildMemberJoin(final GuildMemberJoinEvent e) {

        // Get the Channel, return if Invalid
        if (Main.welcomeAndLeaveChannel == -1) {
            return;
        }

        // Return if not the home guild
        if (!e.getGuild().getId().equals(Main.HOME_GUILD.getId())) {
            return;
        }

        final TextChannel sendChannel = e.getGuild().getTextChannelById(Main.welcomeAndLeaveChannel);
        if (sendChannel == null) {
            System.out.println("[WARN] Welcome channel is null...");
            return;
        }

        // Initialize String[] with 2 slots
        String[] welcomeImage = new String[2];

        // Create the Welcome Image
        try {
            welcomeImage = CreateWelcomeCard.createImage(
                    e.getUser().getAvatarUrl(),
                    e.getUser().getName(),
                    e.getGuild().getName(),
                    e.getGuild().getMemberCount()

            );

        } catch (final IOException | FontFormatException f) {
            f.printStackTrace();
        }

        // Extract file path & name
        final String filePath = welcomeImage[0];
        final String fileName = welcomeImage[1];

        // Send the Message or Embed
        if (Main.welcomeEmbedMessage.equals("")) {
            sendWelcomeAttachment(filePath, fileName, sendChannel);
        } else {
            this.sendWelcomeEmbed(filePath, fileName, e, sendChannel);
        }

        // Delete the file
        try {
            FileUtils.forceDelete(new File(filePath));
        } catch (final IOException ioException) {
            ioException.printStackTrace();
        }

    }

    private void sendWelcomeEmbed(final String filePath, final String fileName, final GuildMemberJoinEvent e, final TextChannel sendChannel) {
        final File file = new File(filePath);

        // Get Title & Description from Config
        String rawString = Main.welcomeEmbedMessage;
        rawString = rawString.replaceAll("%name%", e.getUser().getName());
        rawString = rawString.replaceAll("%server%", e.getGuild().getName());
        rawString = rawString.replaceAll("%id%", e.getUser().getId());
        String[] embedStringArray = rawString.split(";");

        final String embedTitle = embedStringArray[0];
        embedStringArray = Arrays.copyOfRange(embedStringArray, 1, embedStringArray.length);

        final String embedDesc = String.join("\n", embedStringArray);

        // Create The Embed
        final EmbedBuilder eb = new EmbedBuilder()
                .setColor(Main.COLOR_BLUE)
                .setTitle(embedTitle)
                .setDescription(embedDesc);

        // Add the Attachment
        eb.setImage("attachment://" + fileName);

        // Send the Message
        assert sendChannel != null;
        sendChannel.sendMessage(eb.build()).addFile(file, fileName).complete();
    }


}