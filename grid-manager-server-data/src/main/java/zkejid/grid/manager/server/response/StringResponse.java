package zkejid.grid.manager.server.response;

public class StringResponse {

    private String value;
    private String error;

    public StringResponse() {
    }

    public StringResponse(String value, String error) {
        this.value = value;
        this.error = error;
    }

    public String getValue() {
        return value;
    }

    public String getError() {
        return error;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setError(String error) {
        this.error = error;
    }
}
