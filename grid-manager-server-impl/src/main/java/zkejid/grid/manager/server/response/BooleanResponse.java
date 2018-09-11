package zkejid.grid.manager.server.response;

public class BooleanResponse {

    private Boolean value;
    private String error;

    public BooleanResponse(Boolean value, String error) {
        this.value = value;
        this.error = error;
    }

    public boolean isValue() {
        return value;
    }

    public String getError() {
        return error;
    }
}
