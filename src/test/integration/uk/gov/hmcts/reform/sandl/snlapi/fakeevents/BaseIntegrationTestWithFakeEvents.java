package uk.gov.hmcts.reform.sandl.snlapi.fakeevents;

import org.junit.experimental.categories.Category;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.sandl.snlapi.BaseIntegrationTest;

@TestPropertySource(properties = {
    "communication.eventsUrl=http://localhost:8092",
})
@Category(IntegrationTestWithFakeBackend.class)
@AutoConfigureWireMock(port = 8092)
public abstract class BaseIntegrationTestWithFakeEvents extends BaseIntegrationTest {
}
