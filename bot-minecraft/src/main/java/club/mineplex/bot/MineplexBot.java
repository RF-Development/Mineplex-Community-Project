package club.mineplex.bot;

import club.mineplex.bot.common.mineplex.community.CommunityCache;
import club.mineplex.bot.common.player.PlayerCache;
import club.mineplex.bot.listener.ListenerChat;
import club.mineplex.bot.listener.ListenerConnectDisconnect;
import club.mineplex.bot.util.UtilFile;
import club.mineplex.core.cache.GlobalCacheRepository;
import club.mineplex.core.database.Database;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.AuthenticationService;
import com.github.steveice10.mc.auth.service.MojangAuthenticationService;
import com.github.steveice10.mc.auth.service.MsaAuthenticationService;
import com.github.steveice10.mc.auth.service.SessionService;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoHandler;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatPacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.tcp.TcpClientSession;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Log4j2(topic = "MCProtocol")
public class MineplexBot {

    // TODO: Add to config
    private static final String HOST = "clans.mineplex.com";
    private static final int PORT = 25565;
    private static final Proxy AUTH_PROXY = Proxy.NO_PROXY;
    // TODO: Add to config
    private static final String MS_CLIENT_ID = "53bfe87d-54e2-4ade-a6a9-6662f17170a8";
    private static final ExecutorService threadService = Executors.newFixedThreadPool(2);
    private static final File CONFIG_FOLDER;
    private static Session client;
    private static MinecraftProtocol protocol;

    static {
        CONFIG_FOLDER = new File("config");
        if (!CONFIG_FOLDER.exists()) {
            final URL url = MineplexBot.class
                    .getClassLoader()
                    .getResource("config");

            if (url == null) {
                throw new IllegalArgumentException("Config folder not found");
            }

            try {
                CONFIG_FOLDER.mkdir();
                FileUtils.copyDirectory(new File(url.toURI()), CONFIG_FOLDER);
            } catch (final URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(final String[] args) {
        ThreadContext.put("serverIP", String.join(":", HOST, String.valueOf(PORT)));
        Database.getInstance().init(UtilFile.getAppResource("database.json"));

        login();
        fetch(tcpClientSession -> connect());

        GlobalCacheRepository.register(new CommunityCache());
        GlobalCacheRepository.register(new PlayerCache());

        final Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            final String nextLine = scanner.nextLine();
            client.send(new ServerboundChatPacket(nextLine));
        }
    }

    private static void fetch(final Consumer<TcpClientSession> future) {
        final SessionService sessionService = new SessionService();
        sessionService.setProxy(AUTH_PROXY);

        final MinecraftProtocol protocol = new MinecraftProtocol();
        final TcpClientSession serverSession = new TcpClientSession(HOST, PORT, protocol);
        serverSession.setFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService);
        serverSession.setFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY, (ServerInfoHandler) (session, info) -> {
            log.info("Fetching server...");
            log.info("Version: {} - Protocol: {}",
                     info.getVersionInfo().getVersionName(),
                     info.getVersionInfo().getProtocolVersion()
            );
        });

        threadService.execute(serverSession::connect);
        while (!serverSession.isConnected()) {
            try {
                Thread.sleep(5);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
        future.accept(serverSession);
    }

    @SneakyThrows
    private static void login() {
        final ObjectMapper objectMapper = new ObjectMapper();
        final MinecraftAccount account =
                objectMapper.readValue(new File(getConfigFolder(), "credentials.json"), MinecraftAccount.class);

        if (account.getPassword() != null && account.getPassword().length() > 0) {
            try {

                final AuthenticationService authService =
                        account.getAccountType().equals(MinecraftAccount.AccountType.MICROSOFT)
                                ? new MsaAuthenticationService(MS_CLIENT_ID)
                                : new MojangAuthenticationService();

                authService.setUsername(account.getUsername());
                authService.setPassword(account.getPassword());
                authService.setProxy(AUTH_PROXY);
                authService.login();

                protocol = new MinecraftProtocol(authService.getSelectedProfile(), authService.getAccessToken());
                log.info("Successfully authenticated user: '{}'", protocol.getProfile().getName());
            } catch (final RequestException e) {
                e.printStackTrace();
                return;
            }
        } else {
            protocol = new MinecraftProtocol(account.getUsername());
            log.info("Created user: '{}'", protocol.getProfile().getName());
        }
    }

    private static void connect() {
        log.info("Attempting to connect to {}:{}", HOST, PORT);
        final SessionService sessionService = new SessionService();
        sessionService.setProxy(AUTH_PROXY);

        client = new TcpClientSession(HOST, PORT, protocol);
        client.setFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService);
        client.addListener(new ListenerChat());
        client.addListener(new ListenerConnectDisconnect());

        client.connect();
    }

    public static Session getClient() {
        return client;
    }

    public static File getConfigFolder() {
        return CONFIG_FOLDER;
    }

    public static Logger getLogger() {
        return log;
    }
}
