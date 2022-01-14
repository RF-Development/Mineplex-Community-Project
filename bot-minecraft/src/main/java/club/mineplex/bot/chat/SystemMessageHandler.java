package club.mineplex.bot.chat;

import club.mineplex.core.util.UtilText;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundChatPacket;
import com.github.steveice10.packetlib.Session;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class SystemMessageHandler extends AbstractMessageHandler {

    public SystemMessageHandler() {
        super("system_message");
    }

    @Override
    public void handle(@NonNull final ClientboundChatPacket packet, @NonNull final Session session) {
        final Component message = packet.getMessage();
        if (!(message instanceof TextComponent)) {
            return;
        }

        final TextComponent text = (TextComponent) message;
        for (final SystemMessage messageType : SystemMessage.values()) {
            messageType.processMessage(UtilText.getRawTextComponent(text));
        }
    }

}
