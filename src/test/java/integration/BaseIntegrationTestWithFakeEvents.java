package integration;

import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

@AutoConfigureWireMock(port = 8092)
public abstract class BaseIntegrationTestWithFakeEvents extends BaseIntegrationTest {
}
