package uk.gov.hmcts.reform.sandl.snlapi;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.sandl.snlapi.realevents.IntegrationTestWithRealEvents;
import uk.gov.hmcts.reform.sandl.snlapi.utils.LoginHelper;

@RunWith(SpringRunner.class)
@Category(IntegrationTestWithRealEvents.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public abstract class BaseIntegrationTest {

    @LocalServerPort
    int port;

    public LoginHelper loginHelper = new LoginHelper();

    @Before
    public void before() {
        RestAssured.baseURI = "http://localhost:" + String.valueOf(port);
        RestAssured.useRelaxedHTTPSValidation();
    }
}
