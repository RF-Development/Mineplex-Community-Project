package club.mineplex.api.mineplex.website.status.serializers;

import club.mineplex.api.mineplex.website.status.models.Condition;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ConditionSerializer extends StdSerializer<Condition> {

    private static final long serialVersionUID = -5094929012801846328L;

    public ConditionSerializer() {
        super(Condition.class);
    }

    @Override
    public void serialize(final Condition condition, final JsonGenerator json, final SerializerProvider serializer) throws IOException {
        json.writeObject(condition.getCode());
    }

}
