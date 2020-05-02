package zkejid.grid.manager.server.client;

import java.util.Iterator;
import zkejid.api.node.Node;
import zkejid.grid.manager.server.response.BooleanResponse;
import zkejid.grid.manager.server.response.NodeResponse;
import zkejid.impl.exceptions.RuntimeServiceException;

/**
 * Client class for handling iteration over nodes.
 */
class NodeIteratorClient implements Iterator<Node> {

  private String host;
  private String port;
  private String path;

  private String key;

  NodeIteratorClient(String key, String host, String port, String path) {
    this.key = key;
    this.host = host;
    this.port = port;
    this.path = path;
  }

  @Override
  public boolean hasNext() {
    final BooleanResponse response = getClientConnection()
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
    final NodeResponse response = getClientConnection()
        .addPathPart("/iteration/node/next")
        .addGetParam("key", key)
        .result()
        .parseResponse(NodeResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      return new NodeClientImpl(response.getId(), host, port, path);
    }
  }

  public String getKey() {
    return key;
  }

  private ClientConnection getClientConnection() {
    return new ClientConnection(host, port, path);
  }
}
