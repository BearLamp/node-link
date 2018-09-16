package zkejid.grid.manager.server.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import zkejid.impl.exceptions.RuntimeServiceException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ClientConnection {

    private String baseUrl;
    private StringBuilder params;
    private String result;

    public ClientConnection() {
        this("localhost", "8080", "/rest/grid/manager");
    }

    public ClientConnection(String domain, String port, String path) {
        baseUrl = domain + ":" + port + path;

        String initial;
        if (baseUrl.contains("?")) {
            initial = "";
        } else {
            initial = "?";
        }

        params = new StringBuilder(initial);
    }

    public ClientConnection addPathPart(String pathPart) {
        baseUrl = baseUrl + pathPart;
        return this;
    }

    public ClientConnection addGetParam(String name, String value) {
        try {
            if (params.length() > 0
                    && params.charAt(params.length() - 1) != '?'
                    && params.charAt(params.length() - 1) != '&') {
                params.append("&");
            }
            params.append(URLEncoder.encode(name, StandardCharsets.UTF_8.name()));
            params.append("=");
            params.append(URLEncoder.encode(value, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeServiceException(e);
        }
        return this;
    }

    public ClientConnection addGetParamBase64Encoded(String name, byte[] value) {
        final byte[] encoded = Base64.getEncoder().encode(value);
        return addGetParam(name, new String(encoded, StandardCharsets.UTF_8));
    }

    public ClientConnection result() {
        try {
            final String urlLine = baseUrl + params.toString();
            final URLConnection urlConnection = new URL(urlLine).openConnection();
            urlConnection.setRequestProperty("Accept-Charset", StandardCharsets.UTF_8.name());
            InputStream response = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            this.result = sb.toString();
        } catch (Exception e) {
            throw new RuntimeServiceException(e);
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
        final Base64.Decoder decoder = Base64.getDecoder();
        final byte[] decoded = decoder.decode(result);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    public byte[] decodeBase64AsBytes() {
        final Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(result);
    }
}
