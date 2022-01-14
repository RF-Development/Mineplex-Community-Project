package club.mineplex.discord.events.joinleave;

import club.mineplex.discord.BotUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class CreateWelcomeCard {

    public static String[] createImage(final String userAvatarURL, final String userName, final String serverName,
                                       final int serverMemberCount) throws IOException, FontFormatException {
        System.setProperty("http.agent",
                           "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36"
        );
        final int width = 800;
        final int height = 200;

        // Create Main Image
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2d = bufferedImage.createGraphics();

        // Get Profile Picture
        final int profilePictureSize = Math.round(height / (float) 1.3);
        final BufferedImage profileImage =
                BotUtil.BufferedImageUtils.getAndScaleProfilePicture(new URL(userAvatarURL), profilePictureSize);

        // Paste Profile Picture
        final int profilePictureOffset = Math.round((float) height / 2 - (float) profilePictureSize / 2);
        BotUtil.BufferedImageUtils.addImage(bufferedImage, profileImage, 1, profilePictureOffset, profilePictureOffset);

        // Get Font
        final FileInputStream myStream = new FileInputStream("./resources/font.ttf");
        final Font drawFont = Font.createFont(
                Font.TRUETYPE_FONT, myStream).deriveFont(Font.PLAIN, 38);  // Get Font
        myStream.close();

        // Draw Text on Screen
        final int textWidthOffset = profilePictureOffset + profilePictureSize + 56;
        drawTextOnImage(bufferedImage, drawFont, userName, serverName, serverMemberCount, textWidthOffset, 63, 50);

        g2d.dispose();

        // Save File
        final String imageKey = new Random().nextInt(100000000) + ".png";
        final String filePath = "./resources/" + imageKey;
        final File file = new File(filePath);
        ImageIO.write(bufferedImage, "png", file);

        return new String[]{filePath, imageKey};

    }

    public static void drawTextOnImage(
            final BufferedImage image,
            final Font drawFont,
            String userName,
            String serverName,
            final int serverMemberCount,
            final int textWidthOffset,
            final int textHeightOffset,
            final int textLineSpacing
    ) {
        final Graphics2D g2d = image.createGraphics();

        g2d.setFont(drawFont);

        // username limit -> 13
        final int userNameLengthLimit = 13;
        if (userName.length() > userNameLengthLimit) {
            userName = userName.substring(0, userNameLengthLimit - 1) + "...";
        }

        // server name limit -> 28
        final int serverNameLengthLimit = 28;
        if (serverName.length() > serverNameLengthLimit) {
            serverName = serverName.substring(0, serverNameLengthLimit - 1) + "...";
        }

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.drawString(String.format("Welcome, %s to", userName), textWidthOffset, textHeightOffset);
        g2d.drawString(String.format("%s!", serverName), textWidthOffset, textHeightOffset + textLineSpacing);
        g2d.drawString(String.format(
                               "You are the %s member.", BotUtil.BufferedImageUtils.getFormattedPosition(serverMemberCount)),
                       textWidthOffset, textHeightOffset + (int) textLineSpacing * 2
        );

        g2d.dispose();

    }
}