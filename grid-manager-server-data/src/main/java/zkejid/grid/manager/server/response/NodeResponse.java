package zkejid.grid.manager.server.response;

/**
 * Ответ от сервера, содержащий данные экземпляра {@link zkejid.api.node.Node}.
 */
public class NodeResponse {

    private String id;
    private String error;

    public NodeResponse() {}

    public NodeResponse(String id, String error) {
        this.id = id;
        this.error = error;
    }

    public String getId() {
        return id;
    }

    public String getError() {
        return error;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setError(String error) {
        this.error = error;
    }
}
