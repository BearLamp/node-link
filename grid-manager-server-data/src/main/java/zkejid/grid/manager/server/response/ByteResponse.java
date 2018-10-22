package zkejid.grid.manager.server.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import zkejid.grid.manager.server.response.serialization.ByteResponseDeserializer;
import zkejid.grid.manager.server.response.serialization.ByteResponseSerializer;

@JsonSerialize(using = ByteResponseSerializer.class)
@JsonDeserialize(using = ByteResponseDeserializer.class)
@JsonInclude(content = JsonInclude.Include.NON_NULL, value = JsonInclude.Include.NON_EMPTY)
public class ByteResponse {

    private byte[] value;
    private String error;

    public ByteResponse() {
    }

    public ByteResponse(byte[] value, String error) {
        this.value = value;
        this.error = error;
    }

    public byte[] getValue() {
        return value;
    }

    public String getError() {
        return error;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public void setError(String error) {
        this.error = error;
    }
}
