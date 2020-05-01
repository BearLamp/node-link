package zkejid.grid.manager.server.iteration;

import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zkejid.api.node.Node;
import zkejid.grid.manager.server.response.BooleanResponse;
import zkejid.grid.manager.server.response.NodeResponse;

@RestController
@RequestMapping("/rest/grid/manager/iteration/node")
public class NodeIteratorController {

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
  public NodeResponse next(
      @RequestParam(name = "key") String key) {

    try {
      final Iterator iterator = iteration.getIterator(key);
      if (iterator == null) {
        return new NodeResponse(null, "Error. Wrong key, or key timed out.");
      }
      final Node value = (Node) iterator.next();
      if (value == null) {
        return new NodeResponse(null, "Error. Iterator returned null value.");
      }
      return new NodeResponse(value.getId(), null);
    } catch (Exception e) {
      String message = e.getMessage();
      if (message == null) {
        message = "Got exception " + e.getClass().getName() + " without error message.";
      }
      return new NodeResponse(null, message);
    }
  }
}
