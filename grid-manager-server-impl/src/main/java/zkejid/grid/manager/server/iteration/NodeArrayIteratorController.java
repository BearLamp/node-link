package zkejid.grid.manager.server.iteration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zkejid.api.node.Node;
import zkejid.grid.manager.server.response.BooleanResponse;
import zkejid.grid.manager.server.response.NodeArrayResponse;
import zkejid.grid.manager.server.response.NodeResponse;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.IntFunction;

@RestController
@RequestMapping("/rest/grid/manager/iteration/node_array")
public class NodeArrayIteratorController {

    @Autowired
    private IterationBean iteration;

    @RequestMapping("/hasNext")
    public BooleanResponse hasNext(
            @RequestParam(name = "key") String key) {

        try {
            final Iterator iterator = iteration.getIterator(key);
            if (iterator == null) {
                return new BooleanResponse(null, "Error. Wrong key, or key timed out.");
            }
            final boolean value = iterator.hasNext();
            return new BooleanResponse(value, null);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Got exception " + e.getClass().getName() + " without error message.";
            }
            return new BooleanResponse(null, message);
        }
    }

    @RequestMapping("/next")
    public NodeArrayResponse next(
            @RequestParam(name = "key") String key) {

        try {
            final Iterator iterator = iteration.getIterator(key);
            if (iterator == null) {
                return new NodeArrayResponse(null, "Error. Wrong key, or key timed out.");
            }
            final Node[] value = (Node[]) iterator.next();
            if (value == null) {
                return new NodeArrayResponse(null, "Error. Iterator returned null value.");
            }
            final Function<Node, String> toIdentifier = Node::getId;
            final IntFunction<String[]> createNewArray = String[]::new;
            return new NodeArrayResponse(Arrays.stream(value).map(toIdentifier).toArray(createNewArray), null);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Got exception " + e.getClass().getName() + " without error message.";
            }
            return new NodeArrayResponse(null, message);
        }
    }
}
