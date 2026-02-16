import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class DdtApiTest {

    private static final Map<String, String> ctx = new HashMap<>();

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata/requests.csv", numLinesToSkip = 1)
    void api_ddt(String method,
                 String endpoint,
                 int expectedStatus,
                 String body,
                 String saveVar) {

        // 1) placeholderlarni endpoint va body ichida almashtiramiz
        endpoint = replaceVars(endpoint);
        body = replaceVars(body);

        var req = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json");

        if (body != null && !body.isBlank() && !"null".equalsIgnoreCase(body)) {
            req = req.body(body);
        }

        Response res = req.when().request(method, endpoint);

        // 2) status tekshiramiz
        res.then().statusCode(expectedStatus);

        // 3) kerak bo‘lsa response’dan id olib saqlaymiz (saveVar = petId)
        if (saveVar != null && !saveVar.isBlank() && !"null".equalsIgnoreCase(saveVar)) {
            // Pet create/update response’da odatda "id" qaytadi
            Integer id = res.jsonPath().getInt("id");
            if (id != null) {
                ctx.put(saveVar, String.valueOf(id));
            }
        }
    }

    private static String replaceVars(String s) {
        if (s == null) return null;
        String out = s;
        for (Map.Entry<String, String> e : ctx.entrySet()) {
            out = out.replace("{" + e.getKey() + "}", e.getValue());
        }
        return out;
    }
}