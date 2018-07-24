package uk.gov.hmcts.reform.sandl.snlapi.fakeevents;

import org.junit.experimental.categories.Category;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.sandl.snlapi.BaseIntegrationTest;
import uk.gov.hmcts.reform.sandl.snlapi.testcategories.IntegrationTestWithFakeEvents;

@TestPropertySource(properties = {
    "communication.eventsUrl=http://localhost:8192",
})
@Category(IntegrationTestWithFakeEvents.class)
@AutoConfigureWireMock(port = 8192)
@DirtiesContext
public abstract class BaseIntegrationTestWithFakeEvents extends BaseIntegrationTest {
}
