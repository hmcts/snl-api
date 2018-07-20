package integration.fakeevents;

import integration.BaseIntegrationTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
    "communication.eventsUrl=http://localhost:8092",
})
@AutoConfigureWireMock(port = 8092)
public abstract class BaseIntegrationTestWithFakeEvents extends BaseIntegrationTest {
}
