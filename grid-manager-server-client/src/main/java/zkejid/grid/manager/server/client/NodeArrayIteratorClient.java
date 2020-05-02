package zkejid.grid.manager.server.client;

import java.util.Iterator;
import zkejid.api.node.Node;
import zkejid.grid.manager.server.response.BooleanResponse;
import zkejid.grid.manager.server.response.NodeArrayResponse;
import zkejid.impl.exceptions.RuntimeServiceException;

public class NodeArrayIteratorClient implements Iterator<Node[]> {

  private String host;
  private String port;
  private String path;

  private String key;

  public NodeArrayIteratorClient(String key, String host, String port, String path) {
    this.key = key;
    this.host = host;
    this.port = port;
    this.path = path;
  }

  @Override
  public boolean hasNext() {
    final BooleanResponse response = createClientConnection()
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
    final NodeArrayResponse response = createClientConnection()
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
        result[i] = new NodeClientImpl(ids[i], host, port, path);
      }

      return result;
    }
  }

  private ClientConnection createClientConnection() {
    return new ClientConnection(host, port, path);
  }
}
