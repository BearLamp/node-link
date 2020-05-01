package zkejid.grid.manager.server.response;

public class IteratorResponse {

  private String iteratorKey;
  private String error;

  public IteratorResponse() {
  }

  public IteratorResponse(String iteratorKey, String error) {
    this.iteratorKey = iteratorKey;
    this.error = error;
  }

  public String getIteratorKey() {
    return iteratorKey;
  }

  public void setIteratorKey(String iteratorKey) {
    this.iteratorKey = iteratorKey;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
