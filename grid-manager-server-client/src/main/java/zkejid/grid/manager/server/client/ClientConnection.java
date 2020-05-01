package zkejid.grid.manager.server.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import zkejid.impl.exceptions.RuntimeServiceException;

public class ClientConnection {

  private static final String USER_AGENT = "Mozilla/5.0";

  private String baseUrl;
  private Map<String, String> params;
  private String result;

  public ClientConnection() {
    this("http://localhost", "8080", "/rest/grid/manager");
  }

  public ClientConnection(String domain, String port, String path) {
    baseUrl = domain + ":" + port + path;
    params = new HashMap<>();
  }

  public ClientConnection addPathPart(String pathPart) {
    baseUrl = baseUrl + pathPart;
    return this;
  }

  public ClientConnection addGetParam(String name, String value) {
    final String encodeName;
    final String encodeValue;

    try {
      encodeName = URLEncoder.encode(name, StandardCharsets.UTF_8.name());
      encodeValue = URLEncoder.encode(value, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeServiceException(e);
    }

    params.put(encodeName, encodeValue);
    return this;
  }

  public ClientConnection addGetParamBase64Encoded(String name, byte[] value) {
    final byte[] encoded = Base64.getUrlEncoder().encode(value);
    return addGetParam(name, new String(encoded, StandardCharsets.UTF_8));
  }

  public ClientConnection addPostParam(String name, String value) {
    params.put(name, value);
    return this;
  }

  public ClientConnection addPostParamBase64Encoded(String name, byte[] value) {
    final byte[] encoded = Base64.getUrlEncoder().encode(value);
    return addPostParam(name, new String(encoded, StandardCharsets.UTF_8));
  }

  public ClientConnection result() {
    try {
      String initial;
      if (baseUrl.contains("?")) {
        initial = "";
      } else {
        initial = "?";
      }
      final String urlLine = baseUrl + initial + getParamsString();
      final URLConnection urlConnection = new URL(urlLine).openConnection();
      urlConnection.setRequestProperty("Accept-Charset", StandardCharsets.UTF_8.name());
      InputStream response = urlConnection.getInputStream();
      BufferedReader br = new BufferedReader(
          new InputStreamReader(response, StandardCharsets.UTF_8));

      StringBuilder sb = new StringBuilder();
      String line = br.readLine();
      while (line != null) {
        sb.append(line);
        line = br.readLine();
      }
      this.result = sb.toString();
    } catch (Exception e) {
      throw new RuntimeServiceException("URL: " + baseUrl, e);
    }
    return this;
  }

  private String getParamsString() {
    StringBuilder sb = new StringBuilder("");
    for (Map.Entry<String, String> entry : params.entrySet()) {
      if (sb.length() > 0) {
        sb.append("&");
      }
      sb.append(entry.getKey());
      sb.append("=");
      sb.append(entry.getValue());
    }

    return sb.toString();
  }

  public ClientConnection resultPost() {
    try {
      final String urlLine = baseUrl;
      final HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlLine).openConnection();
      urlConnection.setRequestMethod("POST");
      urlConnection.setRequestProperty("User-Agent", USER_AGENT);
      urlConnection.setRequestProperty("Accept-Charset", StandardCharsets.UTF_8.name());

      urlConnection.setDoOutput(true);
      OutputStream os = urlConnection.getOutputStream();
      os.write(getParamsString().getBytes());
      os.flush();
      os.close();

      InputStream response = urlConnection.getInputStream();
      BufferedReader br = new BufferedReader(
          new InputStreamReader(response, StandardCharsets.UTF_8));

      StringBuilder sb = new StringBuilder();
      String line = br.readLine();
      while (line != null) {
        sb.append(line);
        line = br.readLine();
      }
      this.result = sb.toString();
    } catch (Exception e) {
      throw new RuntimeServiceException("URL: " + baseUrl, e);
    }
    return this;
  }

  public <T> T parseResponse(Class<T> clazz) {
    ObjectMapper objectMapper = new ObjectMapper();
    final T value;
    try {
      value = objectMapper.readValue(result, clazz);
    } catch (IOException e) {
      throw new RuntimeServiceException(e);
    }
    return value;
  }

  public String decodeBase64AsString() {
    final Base64.Decoder decoder = Base64.getUrlDecoder();
    final byte[] decoded = decoder.decode(result);
    return new String(decoded, StandardCharsets.UTF_8);
  }

  public byte[] decodeBase64AsBytes() {
    final Base64.Decoder decoder = Base64.getUrlDecoder();
    return decoder.decode(result);
  }
}
