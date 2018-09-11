package zkejid.grid.manager.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zkejid.api.node.Node;
import zkejid.grid.manager.GridManager;
import zkejid.grid.manager.GridManagerImpl;
import zkejid.grid.manager.LinkNodeTypes;
import zkejid.grid.manager.server.iteration.IterationBean;
import zkejid.grid.manager.server.response.*;
import zkejid.impl.persist.api.GridPersister;
import zkejid.impl.persist.impl.GridPersisterImpl;

import java.util.Base64;
import java.util.Iterator;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/rest/grid/manager")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GridRestController {

    @Autowired
    private IterationBean<Node> nodeIterators;

    @Autowired
    private IterationBean<Node[]> linkIterators;

    private GridManager gridManager;

    // avoid GC collection
    @SuppressWarnings("FieldCanBeLocal")
    private GridPersister persister;

    public GridRestController() {
        gridManager = new GridManagerImpl();
        persister = new GridPersisterImpl("grid_persister_file.txt");
        persister.persist(gridManager);
    }

    @RequestMapping("/createNode")
    public NodeResponse createNode(
            @RequestParam(name = "nodeId") String nodeId,
            @RequestParam(name = "encodedPayload", required = false) String encodedPayload,
            @RequestParam(name = "classNodeId", required = false) String classNodeId) {

        try {
            final byte[] payload = getPayloadFromEncodedString(encodedPayload);
            Node result;
            if (classNodeId == null) {
                result = gridManager.createNode(nodeId, payload);
            } else {
                final Node classNode = gridManager.getNode(classNodeId);
                if (classNode == null) {
                    return new NodeResponse(null, "Error. Class node not found: [" + classNodeId + "]");
                }
                result = gridManager.createNode(nodeId, payload, classNode);
            }
            return new NodeResponse(result.getId(), null);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Got exception " + e.getClass().getName() + " without error message.";
            }
            return new NodeResponse(null, message);
        }
    }

    @RequestMapping("/removeNode")
    public VoidResponse removeNode(
            @RequestParam(name = "nodeId") String nodeId) {

        try {
            gridManager.removeNode(nodeId);
            return new VoidResponse(null);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Got exception " + e.getClass().getName() + " without error message.";
            }
            return new VoidResponse(message);
        }
    }

    @RequestMapping("/checkNodeExists")
    public BooleanResponse checkNodeExists(
            @RequestParam(name = "nodeId") String nodeId) {

        try {
            final boolean value = gridManager.checkNodeExists(nodeId);
            return new BooleanResponse(value, null);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Got exception " + e.getClass().getName() + " without error message.";
            }
            return new BooleanResponse(null, message);
        }
    }

    @RequestMapping("/getAllNodes")
    public IteratorResponse getAllNodes() {

        try {
            final Iterator<Node> allNodes = gridManager.getAllNodes();
            final String key = nodeIterators.addIterator(allNodes);
            return new IteratorResponse(key, null);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Got exception " + e.getClass().getName() + " without error message.";
            }
            return new IteratorResponse(null, message);
        }
    }

    @RequestMapping("/getAllLinks")
    public IteratorResponse getAllLinks() {

        try {
            final Iterator<Node[]> allLinks = gridManager.getAllLinks();
            final String key = linkIterators.addIterator(allLinks);
            return new IteratorResponse(key, null);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Got exception " + e.getClass().getName() + " without error message.";
            }
            return new IteratorResponse(null, message);
        }
    }

    @RequestMapping("/getNode")
    public NodeResponse getNode(
            @RequestParam(name = "nodeId") String nodeId) {

        try {
            final Node node = gridManager.getNode(nodeId);
            if (node != null) {
                return new NodeResponse(node.getId(), null);
            } else {
                // null values are OK.
                return new NodeResponse(null, null);
            }
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Got exception " + e.getClass().getName() + " without error message.";
            }
            return new NodeResponse(null, message);
        }
    }

    @RequestMapping("/linkNodes")
    public BooleanResponse linkNodes(
            @RequestParam(name = "node1Id") String node1Id,
            @RequestParam(name = "linkNodeId") String linkNodeId,
            @RequestParam(name = "node2Id") String node2Id) {

        try {
            final Node node1 = gridManager.getNode(node1Id);
            final Node linkNode = gridManager.getNode(linkNodeId);
            final Node node2 = gridManager.getNode(node2Id);
            final boolean result = gridManager.linkNodes(node1, linkNode, node2);

            return new BooleanResponse(result, null);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Got exception " + e.getClass().getName() + " without error message.";
            }
            return new BooleanResponse(null, message);
        }
    }

    @RequestMapping("/getLinks")
    public IteratorResponse getLinks(
            @RequestParam(name = "nodeId") String nodeId) {

        try {
            final Node node = gridManager.getNode(nodeId);
            final Iterator<Node> links = gridManager.getLinks(node);
            final String key = nodeIterators.addIterator(links);

            return new IteratorResponse(key, null);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Got exception " + e.getClass().getName() + " without error message.";
            }
            return new IteratorResponse(null, message);
        }
    }

    @RequestMapping("/getLinkedNodes")
    public IteratorResponse getLinkedNodes(
            @RequestParam(name = "node1Id") String node1Id,
            @RequestParam(name = "linkNodeId") String linkNodeId) {

        try {
            final Node node1 = gridManager.getNode(node1Id);
            final Node linkNode = gridManager.getNode(linkNodeId);
            final Iterator<Node> linkedNodes = gridManager.getLinkedNodes(node1, linkNode);
            final String key = nodeIterators.addIterator(linkedNodes);

            return new IteratorResponse(key, null);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Got exception " + e.getClass().getName() + " without error message.";
            }
            return new IteratorResponse(null, message);
        }
    }

    @RequestMapping("/getNodesLinkedTo")
    public IteratorResponse getNodesLinkedTo(
            @RequestParam(name = "linkNodeId") String linkNodeId,
            @RequestParam(name = "node2Id") String node2Id) {

        try {
            final Node linkNode = gridManager.getNode(linkNodeId);
            final Node node2 = gridManager.getNode(node2Id);
            final Iterator<Node> linkedNodes = gridManager.getNodesLinkedTo(linkNode, node2);
            final String key = nodeIterators.addIterator(linkedNodes);

            return new IteratorResponse(key, null);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Got exception " + e.getClass().getName() + " without error message.";
            }
            return new IteratorResponse(null, message);
        }
    }

    @RequestMapping("/chainFilter")
    public IteratorResponse chainFilter(
            @RequestParam(name = "original1", required = false) String original1,
            @RequestParam(name = "original2", required = false) String original2,
            @RequestParam(name = "original3", required = false) String original3,
            @RequestParam(name = "node1Id", required = false) String node1Id,
            @RequestParam(name = "linkNodeId", required = false) String linkNodeId,
            @RequestParam(name = "node2Id", required = false) String node2Id,
            @RequestParam(name = "type") String type) {

        if (original1 == null && original2 == null && original3 == null) {
            return new IteratorResponse(null, "Error. One chained iterator should be specified.");
        }

        try {
            final LinkNodeTypes linkNodeType = LinkNodeTypes.valueOf(type);
            final Iterator<Node> result;
            if (original1 != null && original2 == null && original3 == null) {
                // case 1
                final Iterator<Node> iterator = nodeIterators.getIterator(original1);
                if (iterator == null) {
                    return new IteratorResponse(null, "Error. Iterator not found: [" + original1 + "].");
                }

                final Node linkedNode = gridManager.getNode(linkNodeId);
                final Node node2 = gridManager.getNode(node2Id);
                result = gridManager.chainFilter(iterator, linkedNode, node2, linkNodeType);
            } else if (original1 == null && original2 != null && original3 == null) {
                // case 2
                final Iterator<Node> iterator = nodeIterators.getIterator(original2);
                if (iterator == null) {
                    return new IteratorResponse(null, "Error. Iterator not found: [" + original2 + "].");
                }

                final Node node1 = gridManager.getNode(node1Id);
                final Node node2 = gridManager.getNode(node2Id);
                result = gridManager.chainFilter(node1, iterator, node2, linkNodeType);
            } else if (original1 == null && original2 == null /* && original3 != null */) {
                // case 3
                final Iterator<Node> iterator = nodeIterators.getIterator(original3);
                if (iterator == null) {
                    return new IteratorResponse(null, "Error. Iterator not found: [" + original3 + "].");
                }

                final Node node1 = gridManager.getNode(node1Id);
                final Node linkedNode = gridManager.getNode(linkNodeId);
                result = gridManager.chainFilter(node1, linkedNode, iterator, linkNodeType);
            } else {
                return new IteratorResponse(null, "Error. Exactly one chained iterator should be specified.");
            }

            final String key = nodeIterators.addIterator(result);
            return new IteratorResponse(key, null);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Got exception " + e.getClass().getName() + " without error message.";
            }
            return new IteratorResponse(null, message);
        }
    }

    @RequestMapping("/checkLinkExists")
    public BooleanResponse checkLinkExists(
            @RequestParam(name = "node1Id", required = false) String node1Id,
            @RequestParam(name = "linkNodeId") String linkNodeId,
            @RequestParam(name = "node2Id", required = false) String node2Id) {

        if (node1Id == null && node2Id == null) {
            return new BooleanResponse(null, "Error. Either node1Id or node2Id (or both) should be specified.");
        }

        try {
            final Node node1 = gridManager.getNode(node1Id);
            final Node linkNode = gridManager.getNode(linkNodeId);
            final Node node2 = gridManager.getNode(node2Id);
            boolean result = gridManager.checkLinkExists(node1, linkNode, node2);

            return new BooleanResponse(result, null);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Got exception " + e.getClass().getName() + " without error message.";
            }
            return new BooleanResponse(null, message);
        }
    }

    @RequestMapping("/checkLinkExistsStrict")
    public BooleanResponse checkLinkExistsStrict(
            @RequestParam(name = "node1Id", required = false) String node1Id,
            @RequestParam(name = "linkNodeId") String linkNodeId,
            @RequestParam(name = "node2Id", required = false) String node2Id) {

        if (node1Id == null && node2Id == null) {
            return new BooleanResponse(null, "Error. Either node1Id or node2Id (or both) should be specified.");
        }

        try {
            final Node node1 = gridManager.getNode(node1Id);
            final Node linkNode = gridManager.getNode(linkNodeId);
            final Node node2 = gridManager.getNode(node2Id);
            boolean result = gridManager.checkLinkExistsStrict(node1, linkNode, node2);

            return new BooleanResponse(result, null);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Got exception " + e.getClass().getName() + " without error message.";
            }
            return new BooleanResponse(null, message);
        }
    }

    @RequestMapping("/generateId")
    public StringResponse generateId(
            @RequestParam(name = "version") String version,
            @RequestParam(name = "domain") String domain,
            @RequestParam(name = "prefix") String prefix) {

        try {
            final String result = gridManager.generateId(version, domain, prefix);

            return new StringResponse(result, null);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Got exception " + e.getClass().getName() + " without error message.";
            }
            return new StringResponse(null, message);
        }
    }

    private byte[] getPayloadFromEncodedString(String encoded) {
        if (encoded == null) {
            return new byte[]{};
        }
        return Base64.getDecoder().decode(encoded);
    }
}
