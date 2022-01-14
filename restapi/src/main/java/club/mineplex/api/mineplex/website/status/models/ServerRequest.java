package club.mineplex.api.mineplex.website.status.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ServerRequest {
    private long ping = -1;
}
