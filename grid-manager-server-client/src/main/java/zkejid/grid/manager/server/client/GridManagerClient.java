package zkejid.grid.manager.server.client;

import java.util.Iterator;
import zkejid.api.node.Node;
import zkejid.grid.manager.GridManager;
import zkejid.grid.manager.IdCreationStrategy;
import zkejid.grid.manager.LinkNodeTypes;
import zkejid.grid.manager.processactions.BackgroundAction;
import zkejid.grid.manager.processactions.NodeFilter;
import zkejid.grid.manager.server.response.BooleanResponse;
import zkejid.grid.manager.server.response.IteratorResponse;
import zkejid.grid.manager.server.response.NodeResponse;
import zkejid.grid.manager.server.response.StringResponse;
import zkejid.grid.manager.server.response.VoidResponse;
import zkejid.impl.exceptions.RuntimeServiceException;

public class GridManagerClient implements GridManager {

  private String host;
  private String port;
  private String path;

  public GridManagerClient(String host, String port, String path) {
    this.host = host;
    this.port = port;
    this.path = path;
  }

  @Override
  public Node createNode(String id) {
    final NodeResponse response = createClientConnection()
        .addPathPart("/createNode")
        .addPostParam("nodeId", id)
        .resultPost()
        .parseResponse(NodeResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      final String result = response.getId();
      return new NodeClientImpl(result, host, port, path);
    }
  }

  @Override
  public Node createNode(String id, byte[] payload) {
    final NodeResponse response = createClientConnection()
        .addPathPart("/createNode")
        .addPostParam("nodeId", id)
        .addPostParamBase64Encoded("encodedPayload", payload)
        .resultPost()
        .parseResponse(NodeResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      final String result = response.getId();
      return new NodeClientImpl(result, host, port, path);
    }
  }

  @Override
  public Node createNode(String id, byte[] payload, Node classNode) {
    final NodeResponse response = createClientConnection()
        .addPathPart("/createNode")
        .addPostParam("nodeId", id)
        .addPostParamBase64Encoded("encodedPayload", payload)
        .addPostParam("classNodeId", classNode.getId())
        .resultPost()
        .parseResponse(NodeResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      final String result = response.getId();
      return new NodeClientImpl(result, host, port, path);
    }
  }

  @Override
  public void removeNode(String id) {
    final VoidResponse response = createClientConnection()
        .addPathPart("/removeNode")
        .addGetParam("nodeId", id)
        .result()
        .parseResponse(VoidResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    }
  }

  @Override
  public boolean checkNodeExists(String id) {
    final BooleanResponse response = createClientConnection()
        .addPathPart("/checkNodeExists")
        .addGetParam("nodeId", id)
        .result()
        .parseResponse(BooleanResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      return response.isValue();
    }
  }

  @Override
  public Iterator<Node> getAllNodes() {
    final IteratorResponse response = createClientConnection()
        .addPathPart("/getAllNodes")
        .result()
        .parseResponse(IteratorResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      return new NodeIteratorClient(response.getIteratorKey(), host, port, path);
    }
  }

  @Override
  public Iterator<Node[]> getAllLinks() {
    final IteratorResponse response = createClientConnection()
        .addPathPart("/getAllLinks")
        .result()
        .parseResponse(IteratorResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      return new NodeArrayIteratorClient(response.getIteratorKey(), host, port, path);
    }
  }

  @Override
  public Node getNode(String id) {
    final NodeResponse response = createClientConnection()
        .addPathPart("/getNode")
        .addGetParam("nodeId", id)
        .result()
        .parseResponse(NodeResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      // null values are OK
      final String result = response.getId();
      if (result == null) {
        return null;
      } else {
        return new NodeClientImpl(result, host, port, path);
      }
    }
  }

  @Override
  public boolean linkNodes(Node node1, Node linkNode, Node node2) {
    final BooleanResponse response = createClientConnection()
        .addPathPart("/linkNodes")
        .addGetParam("node1Id", node1.getId())
        .addGetParam("linkNodeId", linkNode.getId())
        .addGetParam("node2Id", node2.getId())
        .result()
        .parseResponse(BooleanResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      return response.isValue();
    }
  }

  @Override
  public boolean deleteLink(Node node1, Node linkNode, Node node2) {
    final BooleanResponse response = createClientConnection()
        .addPathPart("/deleteLink")
        .addGetParam("node1Id", node1.getId())
        .addGetParam("linkNodeId", linkNode.getId())
        .addGetParam("node2Id", node2.getId())
        .result()
        .parseResponse(BooleanResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      return response.isValue();
    }
  }

  @Override
  public Iterator<Node> getLinks(Node node1) {
    final IteratorResponse response = createClientConnection()
        .addPathPart("/getLinks")
        .addGetParam("node1Id", node1.getId())
        .result()
        .parseResponse(IteratorResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      return new NodeIteratorClient(response.getIteratorKey(), host, port, path);
    }
  }

  @Override
  public Iterator<Node> getLinkedNodes(Node node1, Node linkNode) {
    final IteratorResponse response = createClientConnection()
        .addPathPart("/getLinkedNodes")
        .addGetParam("node1Id", node1.getId())
        .addGetParam("linkNodeId", linkNode.getId())
        .result()
        .parseResponse(IteratorResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      return new NodeIteratorClient(response.getIteratorKey(), host, port, path);
    }
  }

  @Override
  public Iterator<Node> getNodesLinkedTo(Node linkNode, Node node2) {
    final IteratorResponse response = createClientConnection()
        .addPathPart("/getNodesLinkedTo")
        .addGetParam("linkNodeId", linkNode.getId())
        .addGetParam("node2Id", node2.getId())
        .result()
        .parseResponse(IteratorResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      return new NodeIteratorClient(response.getIteratorKey(), host, port, path);
    }
  }

  @Override
  public Iterator<Node> chainFilter(Iterator<Node> original, Node linkNode, Node node2,
      LinkNodeTypes returnType) {
    if (original instanceof NodeIteratorClient) {
      final String key = ((NodeIteratorClient) original).getKey();
      final IteratorResponse response = createClientConnection()
          .addPathPart("/chainFilter")
          .addGetParam("original1", key)
          .addGetParam("linkNodeId", linkNode.getId())
          .addGetParam("node2Id", node2.getId())
          .addGetParam("type", returnType.name())
          .result()
          .parseResponse(IteratorResponse.class);
      if (response.getError() != null) {
        throw new RuntimeServiceException(response.getError());
      } else {
        return new NodeIteratorClient(response.getIteratorKey(), host, port, path);
      }
    } else {
      throw new RuntimeServiceException("Plain iterators not supported.");
    }
  }

  @Override
  public Iterator<Node> chainFilter(Node node1, Iterator<Node> original, Node node2,
      LinkNodeTypes returnType) {
    if (original instanceof NodeIteratorClient) {
      final String key = ((NodeIteratorClient) original).getKey();
      final IteratorResponse response = createClientConnection()
          .addPathPart("/chainFilter")
          .addGetParam("original2", key)
          .addGetParam("node1Id", node1.getId())
          .addGetParam("node2Id", node2.getId())
          .addGetParam("type", returnType.name())
          .result()
          .parseResponse(IteratorResponse.class);
      if (response.getError() != null) {
        throw new RuntimeServiceException(response.getError());
      } else {
        return new NodeIteratorClient(response.getIteratorKey(), host, port, path);
      }
    } else {
      throw new RuntimeServiceException("Plain iterators not supported.");
    }
  }

  @Override
  public Iterator<Node> chainFilter(Node node1, Node linkNode, Iterator<Node> original,
      LinkNodeTypes returnType) {
    if (original instanceof NodeIteratorClient) {
      final String key = ((NodeIteratorClient) original).getKey();
      final IteratorResponse response = createClientConnection()
          .addPathPart("/chainFilter")
          .addGetParam("original3", key)
          .addGetParam("node1Id", node1.getId())
          .addGetParam("linkNodeId", linkNode.getId())
          .addGetParam("type", returnType.name())
          .result()
          .parseResponse(IteratorResponse.class);
      if (response.getError() != null) {
        throw new RuntimeServiceException(response.getError());
      } else {
        return new NodeIteratorClient(response.getIteratorKey(), host, port, path);
      }
    } else {
      throw new RuntimeServiceException("Plain iterators not supported.");
    }
  }

  @Override
  public boolean checkLinkExists(Node node1, Node linkNode, Node node2) {
    final BooleanResponse response = createClientConnection()
        .addPathPart("/checkLinkExists")
        .addGetParam("node1Id", node1.getId())
        .addGetParam("linkNodeId", linkNode.getId())
        .addGetParam("node2Id", node2.getId())
        .result()
        .parseResponse(BooleanResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      return response.isValue();
    }
  }

  @Override
  public boolean checkLinkExistsStrict(Node node1, Node linkNode, Node node2) {
    final BooleanResponse response = createClientConnection()
        .addPathPart("/checkLinkExistsStrict")
        .addGetParam("node1Id", node1.getId())
        .addGetParam("linkNodeId", linkNode.getId())
        .addGetParam("node2Id", node2.getId())
        .result()
        .parseResponse(BooleanResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      return response.isValue();
    }
  }

  @Override
  public String generateId(String version, String domain, String prefix) {
    final StringResponse response = createClientConnection()
        .addPathPart("/generateId")
        .addGetParam("version", version)
        .addGetParam("domain", domain)
        .addGetParam("prefix", prefix)
        .result()
        .parseResponse(StringResponse.class);
    if (response.getError() != null) {
      throw new RuntimeServiceException(response.getError());
    } else {
      return response.getValue();
    }
  }

  @Override
  public void setIdCreationStrategy(IdCreationStrategy strategy) {

  }

  @Override
  public void addAction(NodeFilter filter, BackgroundAction action) {
    throw new RuntimeServiceException("Not implemented");
  }

  private ClientConnection createClientConnection() {
    return new ClientConnection(host, port, path);
  }
}
