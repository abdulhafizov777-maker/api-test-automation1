import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static io.restassured.RestAssured.given;

class DdtApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://httpbin.org";
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata/requests.csv", numLinesToSkip = 1)
    void get_endpoints_ddt(String endpoint, int expectedStatus) {

        given()
                .header("User-Agent", "Mozilla/5.0")
                .header("Accept", "application/json")
                .when()
                .get(endpoint)
                .then()
                .statusCode(expectedStatus);
    }
}
