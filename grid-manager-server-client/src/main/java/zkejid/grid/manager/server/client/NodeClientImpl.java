package zkejid.grid.manager.server.client;

import zkejid.api.node.Node;
import zkejid.grid.manager.server.response.ByteResponse;
import zkejid.grid.manager.server.response.StringResponse;
import zkejid.impl.exceptions.RuntimeServiceException;

/**
 * Client class for handling node methods.
 */
class NodeClientImpl implements Node {

  private final String id;

  private final String host;
  private final String port;
  private final String path;

  NodeClientImpl(String id, String host, String port, String path) {
    this.id = id;
    this.host = host;
    this.port = port;
    this.path = path;
  }

  @Override
  public String getId() {
    // it is safe return field value because it comes from server. It is not a user input.
    return id;
  }

  @Override
  public byte[] getPayload() {
    final ByteResponse response = getClientConnection()
        .addPathPart("/node/getPayload")
        .addGetParam("nodeId", id)
        .result()
        .parseResponse(ByteResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      return response.getValue();
    }
  }

  @Override
  public String getPayload20() {
    final StringResponse response = getClientConnection()
        .addPathPart("/node/getPayload20")
        .addGetParam("nodeId", id)
        .result()
        .parseResponse(StringResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      return response.getValue();
    }
  }

  private ClientConnection getClientConnection() {
    return new ClientConnection(host, port, path);
  }
}
