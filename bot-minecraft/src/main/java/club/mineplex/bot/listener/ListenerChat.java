package club.mineplex.bot.listener;

import club.mineplex.bot.MineplexBot;
import club.mineplex.bot.chat.AbstractMessageHandler;
import club.mineplex.bot.chat.ChatManager;
import club.mineplex.bot.util.UtilFile;
import club.mineplex.core.util.UtilText;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundChatPacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.extern.log4j.Log4j2;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Log4j2(topic = "MinecraftChat")
public class ListenerChat extends SessionAdapter {

    private static final File FILTER_FILE = UtilFile.getAppResource("filtered_messages.json");

    private final List<String> filteredMessages = new ArrayList<>();

    public ListenerChat() {
        try {
            final JsonMapper mapper = new JsonMapper();
            this.filteredMessages.addAll(mapper.readValue(FILTER_FILE, new TypeReference<Collection<String>>() {
            }));
        } catch (final IOException e) {
            MineplexBot.getLogger().error("There was an error reading '{}'", FILTER_FILE.getName());
            e.printStackTrace();
        }
    }

    @Override
    public void packetReceived(final Session session, final Packet packet) {
        if (packet instanceof ClientboundChatPacket) {
            final Component message = ((ClientboundChatPacket) packet).getMessage();
            final TextComponent text = (TextComponent) message;

            final String rawText = UtilText.getRawTextComponent(text);
            if (this.filteredMessages.contains(rawText)) {
                return;
            }

            log.info(rawText);

            for (final AbstractMessageHandler handler : ChatManager.getInstance().getHandlers()) {
                handler.handle(((ClientboundChatPacket) packet), session);
            }
        }
    }
}
