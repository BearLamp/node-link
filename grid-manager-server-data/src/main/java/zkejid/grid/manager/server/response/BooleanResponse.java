package zkejid.grid.manager.server.response;

public class BooleanResponse {

  private Boolean value;
  private String error;

  public BooleanResponse() {
  }

  public BooleanResponse(Boolean value, String error) {
    this.value = value;
    this.error = error;
  }

  public Boolean isValue() {
    return value;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public void setValue(Boolean value) {
    this.value = value;
  }
}
