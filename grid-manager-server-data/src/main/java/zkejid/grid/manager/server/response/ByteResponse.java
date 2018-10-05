package zkejid.grid.manager.server.response;

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
