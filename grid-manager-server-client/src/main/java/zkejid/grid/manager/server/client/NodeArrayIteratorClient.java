package zkejid.grid.manager.server.client;

import zkejid.api.node.Node;
import zkejid.grid.manager.server.response.BooleanResponse;
import zkejid.grid.manager.server.response.NodeArrayResponse;
import zkejid.grid.manager.server.response.NodeResponse;
import zkejid.impl.exceptions.RuntimeServiceException;

import java.util.Iterator;

public class NodeArrayIteratorClient implements Iterator<Node[]> {

    private String key;

    public NodeArrayIteratorClient(String key) {
        this.key = key;
    }

    @Override
    public boolean hasNext() {
        final BooleanResponse response = new ClientConnection()
                .addPathPart("/iteration/node_array/hasNext")
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
    public Node[] next() {
        final NodeArrayResponse response = new ClientConnection()
                .addPathPart("/iteration/node_array/next")
                .addGetParam("key", key)
                .result()
                .parseResponse(NodeArrayResponse.class);
        if (response.getError() != null) {
            throw new RuntimeServiceException(response.getError());
        } else {
            final String[] ids = response.getIds();
            Node[] result = new Node[ids.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = new NodeClientImpl(ids[i]);
            }

            return result;
        }
    }
}
