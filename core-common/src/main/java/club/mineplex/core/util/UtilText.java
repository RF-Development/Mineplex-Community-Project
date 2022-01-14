package club.mineplex.core.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.awt.*;
import java.util.UUID;

@UtilityClass
public class UtilText {

    private final char[] discordCodes = {'*', '_', '`', '>', '~'};

    public String getRawTextComponent(final TextComponent text) {
        final StringBuilder stringBuilder = new StringBuilder(text.content());
        for (final Component child : text.children()) {
            if (child instanceof TextComponent) {
                stringBuilder.append(getRawTextComponent(((TextComponent) child)));
            }
        }
        return stringBuilder.toString();
    }

    public UUID uuidWithNoDashes(final String uuid) {
        return UUID.fromString(uuid.replaceFirst(
                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
        ));
    }

    public String getDiscordCompatibleText(final String text) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            final char character = text.charAt(i);
            for (final char discordCode : discordCodes) {
                if (character == discordCode) {
                    stringBuilder.append('\\');
                    break;
                }
            }
            stringBuilder.append(character);
        }
        return stringBuilder.toString();
    }

    public int getDecimalFromHex(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        final String digits = "0123456789ABCDEF";
        hex = hex.toUpperCase();
        int val = 0;
        for (int i = 0; i < hex.length(); i++) {
            final char c = hex.charAt(i);
            final int d = digits.indexOf(c);
            val += d * (Math.pow(16, hex.length() - 1 - i));
        }
        return val;
    }

    public int getDecimalFromColor(final Color color) {
        return getDecimalFromHex(Integer.toHexString(color.getRGB()).substring(2));
    }

}
