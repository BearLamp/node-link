package zkejid.grid.manager.server.client;

import zkejid.api.node.Node;
import zkejid.grid.manager.server.response.BooleanResponse;
import zkejid.grid.manager.server.response.NodeResponse;
import zkejid.impl.exceptions.RuntimeServiceException;

import java.util.Iterator;

public class NodeIteratorClient implements Iterator<Node> {

    private String key;

    public NodeIteratorClient(String key) {
        this.key = key;
    }

    @Override
    public boolean hasNext() {
        final BooleanResponse response = new ClientConnection()
                .addPathPart("/iteration/node/hasNext")
                .addGetParam("key", key)
                .result()
                .parseResponse(BooleanResponse.class);
        if (response.getError() != null) {
            throw new RuntimeServiceException(response.getError());
        } else {
            return response.isValue();
        }
    }

    @Override
    public Node next() {
        final NodeResponse response = new ClientConnection()
                .addPathPart("/iteration/node/next")
                .addGetParam("key", key)
                .result()
                .parseResponse(NodeResponse.class);
        if (response.getError() != null) {
            throw new RuntimeServiceException(response.getError());
        } else {
            return new NodeClientImpl(response.getId());
        }
    }

    public String getKey() {
        return key;
    }
}
