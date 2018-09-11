package zkejid.grid.manager.server.response;

public class StringResponse {

    private String value;
    private String error;

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
}
