package integration.realevents.controllers;

import integration.BaseIntegrationTest;
import io.restassured.http.Header;
import org.junit.Ignore;
import org.junit.Test;

import static io.restassured.RestAssured.given;

@Ignore
public class RoomControllerIntRealTest extends BaseIntegrationTest {

    @Test
    public void rooms_shouldCallProperEventsEndpointAndReturnRooms() throws Exception {
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

    }
}
