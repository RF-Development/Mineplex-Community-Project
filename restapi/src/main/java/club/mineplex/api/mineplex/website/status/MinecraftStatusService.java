package club.mineplex.api.mineplex.website.status;

import club.mineplex.api.mineplex.website.status.models.Condition;
import club.mineplex.api.mineplex.website.status.models.minecraft.MinecraftRequestInfo;
import club.mineplex.api.mineplex.website.status.models.minecraft.MinecraftServerStatus;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.nukkitx.protocol.bedrock.BedrockClient;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.dilley.MineStat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Service
@Log4j2
public class MinecraftStatusService {

    final ExecutorService threadPool;
    @Getter
    private final MinecraftServerStatus javaServerStatus = new MinecraftServerStatus("java");
    @Getter
    private final MinecraftServerStatus usBedrockServerStatus = new MinecraftServerStatus("america-bedrock");
    @Getter
    private final MinecraftServerStatus euBedrockServerStatus = new MinecraftServerStatus("europe-bedrock");

    public MinecraftStatusService() {
        final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Server-Status-%d").build();
        final int totalRangeCount =
                this.javaServerStatus.getRanges().size() + this.usBedrockServerStatus.getRanges().size()
                        + this.euBedrockServerStatus.getRanges().size();
        this.threadPool = Executors.newFixedThreadPool(totalRangeCount, threadFactory);
    }

    @Scheduled(initialDelay = 60_000, fixedRate = 60_000 * 5)
    private void updateServerStatus() {
        this.runThreadPoolForRanges(this.javaServerStatus, range -> {
            final MineStat request = new MineStat(range.getAddress(), range.getPort(), 10);
            final String motd = request.getMotd();
            final int players = request.getCurrentPlayers();
            final long ping = request.getLatency();

            range.setMotd(motd);
            range.setOnlinePlayers(players);
            range.setPing(ping);
            range.setOnline(request.isServerUp());
        });

        final InetSocketAddress bindAddress = new InetSocketAddress("0.0.0.0", 12345);
        final BedrockClient client = new BedrockClient(bindAddress);
        client.bind().join();
        this.updateBedrockStatus(client, this.euBedrockServerStatus);
        this.updateBedrockStatus(client, this.usBedrockServerStatus);
        client.close(true);
    }

    private void updateBedrockStatus(final BedrockClient client, final MinecraftServerStatus bedrockStatus) {
        this.runThreadPoolForRanges(bedrockStatus, range -> {
            final InetSocketAddress addressToPing = new InetSocketAddress(range.getAddress(), range.getPort());
            final long requestMillis = System.currentTimeMillis();

            client.ping(addressToPing).whenComplete((pong, throwable) -> {
                final long ping = System.currentTimeMillis() - requestMillis;
                final int players = pong.getPlayerCount();
                final Optional<String> subMotd = Optional.ofNullable(pong.getSubMotd());
                String motd = Optional.ofNullable(pong.getMotd()).orElse("");
                if (subMotd.isPresent()) {
                    motd += "\n" + subMotd.get();
                }

                range.setMotd(motd);
                range.setOnlinePlayers(players);
                range.setPing(ping);
                range.setOnline(throwable == null);
            }).join();
        });
    }

    private void runThreadPoolForRanges(final MinecraftServerStatus status, final Consumer<MinecraftRequestInfo> task) {

        for (final MinecraftRequestInfo range : status.getRanges()) {
            this.threadPool.submit(() -> task.accept(range));
        }

        try {
            this.threadPool.awaitTermination(30000L, TimeUnit.MILLISECONDS);
        } catch (final InterruptedException ignored) {

        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                final int averagePing = (int) status.getRanges().stream()
                                                    .filter(MinecraftRequestInfo::isOnline)
                                                    .mapToLong(MinecraftRequestInfo::getPing)
                                                    .average()
                                                    .orElse(-1);

                final int averagePlayers = (int) status.getRanges().stream()
                                                       .filter(MinecraftRequestInfo::isOnline)
                                                       .mapToLong(MinecraftRequestInfo::getOnlinePlayers)
                                                       .average()
                                                       .orElse(-1);

                status.setAveragePing(-1);
                status.setMotd(null);
                status.setOnlinePlayers(-1);

                status.setOnlinePlayers(averagePlayers);
                status.setAveragePing(averagePing);
                status.getRanges().stream()
                      .filter(MinecraftRequestInfo::isOnline)
                      .filter(request -> request.getMotd() != null)
                      .findAny()
                      .ifPresent(minecraftRequestInfo -> status.setMotd(minecraftRequestInfo.getMotd()));

                status.setCondition(Condition.ofServerRanges(status.getRanges()));
            }
        }, 35000L);

    }

}
