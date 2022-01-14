package club.mineplex.api.mineplex.mods.clans.deserializers;

import club.mineplex.api.mineplex.mods.clans.models.ReleaseInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Optional;

public class ReleaseInfoInfoDeserializer extends StdDeserializer<ReleaseInfo> {
    private static final long serialVersionUID = -1254437052385129609L;

    public ReleaseInfoInfoDeserializer() {
        super(ReleaseInfo.class);
    }

    private Optional<String> getDownloadUrl(final JsonNode node) {
        final JsonNode firstNode = node.get("assets").get(0);
        if (firstNode == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(firstNode.get("browser_download_url").asText());
    }

    @Override
    public ReleaseInfo deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        // Check if no releases exist
        if (node.isEmpty()) {
            return null;
        }

        final JsonNode lastReleaseNode = node.get(0);
        final String latestVersion = lastReleaseNode.get("tag_name").asText();
        final String changelogUrl = lastReleaseNode.get("html_url").asText();
        final Optional<String> downloadUrl = this.getDownloadUrl(lastReleaseNode);

        return new ReleaseInfo(
                latestVersion,
                changelogUrl,
                downloadUrl.orElse(null)
        );
    }
}
