package zkejid.grid.manager.server.client;

import zkejid.api.node.Node;

public class NodeClientImpl implements Node {

    private final String id;

    public NodeClientImpl(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public byte[] getPayload() {
        return new byte[0];
    }

    @Override
    public String getPayload20() {
        return "";
    }
}
