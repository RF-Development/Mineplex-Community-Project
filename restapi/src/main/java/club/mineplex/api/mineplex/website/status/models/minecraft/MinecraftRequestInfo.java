package club.mineplex.api.mineplex.website.status.models.minecraft;

import club.mineplex.api.mineplex.website.status.models.ServerRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MinecraftRequestInfo extends ServerRequest {
    private final String address;
    private final int port;
    private boolean online;

    @JsonIgnore
    private int onlinePlayers = -1;
    @JsonIgnore
    private String motd = null;

    public MinecraftRequestInfo(@JsonProperty("address") final String address, @JsonProperty("port") final int port) {
        this.address = address;
        this.port = port;
    }

    public static MinecraftRequestInfo ofDomain(final String domain) {
        return new MinecraftRequestInfo(domain, 25565);
    }
}
