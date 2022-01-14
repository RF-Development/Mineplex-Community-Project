package club.mineplex.api.mineplex.website.status.models.website;

import club.mineplex.api.mineplex.website.status.models.ServerStatus;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class HttpSiteStatus extends ServerStatus {

    @Getter
    private final String url;

    public HttpSiteStatus(@NonNull final String serverName, final String url) {
        super(serverName);
        this.url = url;
    }

}
