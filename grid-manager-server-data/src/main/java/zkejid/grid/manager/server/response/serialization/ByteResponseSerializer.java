package zkejid.grid.manager.server.response.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import zkejid.grid.manager.server.response.ByteResponse;

public class ByteResponseSerializer extends StdSerializer<ByteResponse> {

  protected ByteResponseSerializer() {
    super(ByteResponse.class);
  }

  @Override
  public void serialize(ByteResponse value, JsonGenerator gen,
      SerializerProvider serializerProvider) throws IOException {
    gen.writeStartObject();
    gen.writeBinaryField("value", value.getValue());
    gen.writeStringField("error", value.getError());
    gen.writeEndObject();
  }

}
