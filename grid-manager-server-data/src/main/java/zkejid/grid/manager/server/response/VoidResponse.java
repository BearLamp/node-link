package zkejid.grid.manager.server.response;

public class VoidResponse {

    private String error;

    public VoidResponse() {
    }

    public VoidResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
