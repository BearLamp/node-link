package zkejid.grid.manager.server.response;

public class VoidResponse {

    private String error;

    public VoidResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
