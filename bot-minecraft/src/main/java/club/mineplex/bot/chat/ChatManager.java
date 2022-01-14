package club.mineplex.bot.chat;

import club.mineplex.bot.common.mineplex.community.CommunityMessageHandler;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {

    private static final ChatManager INSTANCE = new ChatManager();
    private final List<AbstractMessageHandler> handlers = new ArrayList<>();

    private ChatManager() {
        this.handlers.add(new CommunityMessageHandler());
        this.handlers.add(new SystemMessageHandler());
    }

    public static ChatManager getInstance() {
        return INSTANCE;
    }

    public List<AbstractMessageHandler> getHandlers() {
        return this.handlers;
    }

}
