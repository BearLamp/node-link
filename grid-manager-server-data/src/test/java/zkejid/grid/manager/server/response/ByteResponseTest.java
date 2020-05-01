package zkejid.grid.manager.server.response;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class ByteResponseTest {

    @Test
    public void serialize_emptyValues_correct() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        final ByteResponse byteResponse = new ByteResponse(new byte[0], "");
        final String result = mapper.writer().writeValueAsString(byteResponse);

        Assert.assertEquals("{\"value\":\"\",\"error\":\"\"}", result);
    }

    @Test
    public void deserialize_emptyValues_correct() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        final JsonFactory jsonFactory = new JsonFactory();
        final JsonParser parser = jsonFactory.createParser("{\"value\":\"\",\"error\":\"\"}");
        parser.setCodec(mapper);
        final ByteResponse value = mapper.reader().readValue(parser, ByteResponse.class);
        final ByteResponse expected = new ByteResponse(new byte[0], "");

        Assert.assertNotNull(value);
        Assert.assertEquals(expected.getError(), value.getError());
        Assert.assertArrayEquals(expected.getValue(), value.getValue());
    }

    @Test
    public void serialize_deserialize_noErrorDeserialized() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        final ByteResponse byteResponse = new ByteResponse(new byte[]{0, 1, 2}, null);
        final String result = mapper.writer().writeValueAsString(byteResponse);

        final JsonFactory jsonFactory = new JsonFactory();
        final JsonParser parser = jsonFactory.createParser(result);
        parser.setCodec(mapper);
        final ByteResponse value = mapper.reader().readValue(parser, ByteResponse.class);

        Assert.assertNotNull(value);
        Assert.assertNull(value.getError());
        Assert.assertArrayEquals(new byte[]{0, 1, 2}, value.getValue());
    }

}