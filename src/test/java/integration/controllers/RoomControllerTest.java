package integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.sandl.snlapi.Application;
import uk.gov.hmcts.reform.sandl.snlapi.security.requests.LoginRequest;
import uk.gov.hmcts.reform.sandl.snlapi.security.responses.JwtAuthenticationResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Application.class)
public class RoomControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8092);

    @Before
    public void before() {
        String appUrl = System.getenv("TEST_URL");
        if (appUrl == null) {
            appUrl = "http://localhost:8090";
        }

        RestAssured.baseURI = appUrl;
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void rooms_shouldCallProperEventsEndpointAndReturnRooms() throws Exception {
        stubFor(get(urlEqualTo("/room"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[\"rooms\"]")));

        Header authenticationHeader = signIn("officer1", "asd");

        given()
            .contentType("application/json")
            .header(authenticationHeader)
            .when()
            .get("/room")
            .then()
            .assertThat()
            .statusCode(200)
            .and().extract().response();

        verify(getRequestedFor(urlMatching("/room")));
    }

    private Header signIn(String username, String password) throws Exception {
        Response signInResponse = given()
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
