package zkejid.grid.manager.server.response;

/**
 * Ответ от сервера, содержащий список идентификаторов {@link zkejid.api.node.Node}.
 */
public class NodeArrayResponse {

    private String[] ids;
    private String error;

    public NodeArrayResponse() {
    }

    public NodeArrayResponse(String[] ids, String error) {
        this.ids = ids;
        this.error = error;
    }

    public NodeArrayResponse(String error, String... ids) {
        this.ids = ids;
        this.error = error;
    }

    public String[] getIds() {
        return ids;
    }

    public String getError() {
        return error;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public void setError(String error) {
        this.error = error;
    }
}
