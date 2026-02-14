import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static io.restassured.RestAssured.given;

class DdtApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata/requests.csv", numLinesToSkip = 1)
    void api_ddt(String method,
                 String endpoint,
                 int expectedStatus,
                 String body) {

        var request = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json");

        // agar body bo‘lsa qo‘shamiz
        if (body != null && !body.isBlank()) {
            request.body(body);
        }

        request
                .when()
                .request(method, endpoint)
                .then()
                .statusCode(expectedStatus);
    }
}
