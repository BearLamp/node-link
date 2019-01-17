package zkejid.grid.manager.server.response.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import zkejid.grid.manager.server.response.ByteResponse;

import java.io.IOException;
import java.util.Base64;

public class ByteResponseDeserializer extends StdDeserializer<ByteResponse> {

    protected ByteResponseDeserializer() {
        super(ByteResponse.class);
    }

    @Override
    public ByteResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonNode treeNode = jsonParser.readValueAsTree();
        final String text = treeNode.get("value").asText();
        final byte[] decoded = Base64.getDecoder().decode(text);

        final JsonNode errorNode = treeNode.get("error");
        final String error = errorNode.isNull() ? null : errorNode.asText();


        return new ByteResponse(decoded, error);
    }

}
