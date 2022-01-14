package club.mineplex.bot.listener;

import club.mineplex.bot.MineplexBot;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;

public class ListenerConnectDisconnect extends SessionAdapter {

    @Override
    public void disconnected(final DisconnectedEvent event) {
        if (event.getCause() != null) {
            MineplexBot.getLogger().error("Disconnected: {}", event.getReason());
            event.getCause().printStackTrace();
        } else {
            MineplexBot.getLogger().info("Disconnected: {}", event.getReason());
        }
    }

    @Override
    public void connected(final ConnectedEvent event) {
        MineplexBot.getLogger().info("Connected to server.");
    }

}
