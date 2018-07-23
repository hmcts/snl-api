package uk.gov.hmcts.reform.sandl.snlapi.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.Header;
import io.restassured.response.Response;
import uk.gov.hmcts.reform.sandl.snlapi.security.requests.LoginRequest;
import uk.gov.hmcts.reform.sandl.snlapi.security.responses.JwtAuthenticationResponse;

import static io.restassured.RestAssured.given;

public class LoginHelper {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    public Header signInAsOfficer() throws Exception {
        return signIn("officer1", "asd");
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
