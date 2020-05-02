package zkejid.grid.manager.server.client;

import com.github.jenspiegsa.wiremockextension.ConfigureWireMock;
import com.github.jenspiegsa.wiremockextension.InjectServer;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@DisplayName("NodeArrayIteratorClient test")
@ExtendWith(WireMockExtension.class)
class NodeArrayIteratorClientTest {

  @InjectServer
  WireMockServer serverMock;

  @ConfigureWireMock
  Options options = WireMockConfiguration.wireMockConfig()
      .dynamicPort()
      .notifier(new ConsoleNotifier(true));

  @Test
  public void hasNext_customPath_responseOk() {
    final String customPath = "/hello/path";
    WireMock.givenThat(
        WireMock.get(customPath + "/iteration/node_array/hasNext?key=qwerty")
            .willReturn(WireMock.okJson("{\"value\":true,\"error\":null}"))
    );
    // when
    final NodeArrayIteratorClient nodeArrayIteratorClient =
        new NodeArrayIteratorClient("qwerty", "http://localhost", "" + serverMock.port(),
            customPath);

    final boolean hasNext = nodeArrayIteratorClient.hasNext();

    Assertions.assertTrue(hasNext, "Expect true for hasNext");
  }

}