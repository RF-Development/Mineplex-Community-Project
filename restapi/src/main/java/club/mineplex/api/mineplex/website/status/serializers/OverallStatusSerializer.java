package club.mineplex.api.mineplex.website.status.serializers;

import club.mineplex.api.mineplex.website.status.models.OverallStatus;
import club.mineplex.api.mineplex.website.status.models.ServerStatus;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class OverallStatusSerializer extends StdSerializer<OverallStatus> {

    private static final long serialVersionUID = -5399470389368938681L;

    public OverallStatusSerializer() {
        super(OverallStatus.class);
    }

    @Override
    public void serialize(final OverallStatus overallStatus, final JsonGenerator json, final SerializerProvider serializer) throws IOException {
        json.writeStartObject();
        for (final ServerStatus serverStatus : overallStatus.getServerStatuses()) {
            json.writeObjectField(serverStatus.getServerName() + "-status", serverStatus.getCondition());
        }
        json.writeObjectField(overallStatus.getServerName(), overallStatus.getCondition());
        json.writeEndObject();
    }

}
