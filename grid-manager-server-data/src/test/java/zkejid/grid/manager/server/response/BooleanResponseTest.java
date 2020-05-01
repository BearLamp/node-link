package zkejid.grid.manager.server.response;

import org.junit.Assert;
import org.junit.Test;

public class BooleanResponseTest {

  @Test
  public void isValue_error_false() throws Exception {
    final BooleanResponse booleanResponse = new BooleanResponse(null, "error msg");

    Assert.assertNull(booleanResponse.isValue());
  }

}