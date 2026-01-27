import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

class SmokeApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://httpbin.org";
    }

    @Test
    void smoke_shouldReturn200() {
        given()
                .header("User-Agent", "Mozilla/5.0")
                .header("Accept", "application/json")
                .when()
                .get("/get")
                .then()
                .statusCode(200);
    }
}
