package uk.gov.hmcts.reform.sandl.snlapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.sandl.snlapi.realevents.IntegrationTestWithRealBackend;
import uk.gov.hmcts.reform.sandl.snlapi.security.requests.LoginRequest;
import uk.gov.hmcts.reform.sandl.snlapi.security.responses.JwtAuthenticationResponse;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@Category(IntegrationTestWithRealBackend.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public abstract class BaseIntegrationTest {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    int port;

    @Before
    public void before() {
        RestAssured.baseURI = "http://localhost:" + String.valueOf(port);
        RestAssured.useRelaxedHTTPSValidation();
    }

    public Header signIn(String username, String password) throws Exception {
        Response signInResponse =
            given()
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(new LoginRequest(username, password)))
            .when()
                .post("/security/signin")
            .then()
                .statusCode(200)
                .and()
                .extract().response();

        JwtAuthenticationResponse token = objectMapper.readValue(signInResponse.body().print(),
            JwtAuthenticationResponse.class);

        return new Header("Authorization", "Bearer " + token.getAccessToken());
    }

}
