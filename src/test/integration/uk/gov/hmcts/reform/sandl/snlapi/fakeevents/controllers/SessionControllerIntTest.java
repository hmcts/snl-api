package uk.gov.hmcts.reform.sandl.snlapi.fakeevents.controllers;

import io.restassured.http.Header;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.sandl.snlapi.fakeevents.BaseIntegrationTestWithFakeEvents;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static io.restassured.RestAssured.given;

public class SessionControllerIntTest extends BaseIntegrationTestWithFakeEvents {

    @Test
    public void updateSessions_shouldResponseWithConflict_whenSessionLocked() throws Exception {
        stubFor(put(urlEqualTo("/sessions/update"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.CONFLICT.value())
                .withHeader("Content-Type", "application/json")
                .withBody("[]")));

        Header authenticationHeader = loginHelper.signInAsOfficer();

        given()
            .contentType("application/json")
            .header(authenticationHeader)
            .body("{}")
        .when()
            .put("/sessions/update")
        .then()
            .assertThat()
            .statusCode(HttpStatus.CONFLICT.value())
            .and();

        verify(putRequestedFor(urlEqualTo("/sessions/update")));
    }
}
