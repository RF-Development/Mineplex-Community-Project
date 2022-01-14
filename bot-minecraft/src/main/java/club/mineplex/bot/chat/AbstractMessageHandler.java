package club.mineplex.bot.chat;

import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundChatPacket;
import com.github.steveice10.packetlib.Session;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractMessageHandler {

    private final String identifier;

    public abstract void handle(@NonNull ClientboundChatPacket packet, @NonNull Session session);

}
