import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class DdtApiTest {

    // CRUD context: save qilinadigan qiymatlar (masalan created pet id)
    private static final Map<String, String> ctx = new HashMap<>();

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata/requests.csv", numLinesToSkip = 1)
    void api_ddt(String step,
                 String method,
                 String endpoint,
                 int expectedStatus,
                 String body,
                 String saveVar) {

        // 1) {{petId}} kabi placeholderlarni endpoint/body ichida almashtiramiz
        endpoint = replaceVars(endpoint);
        body = replaceVars(body);

        var req = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);

        // body bo'lsa qo'shamiz
        if (body != null && !body.isBlank() && !body.equalsIgnoreCase("null")) {
            req = req.body(body);
        }

        Response resp = req
                .when()
                .request(method, endpoint)
                .then()
                .statusCode(expectedStatus)
                .extract().response();

        // 2) Agar saveVar berilgan bo'lsa — response'dan id ni saqlaymiz
        // saveVar format: petId=$.id  (jsonPath $.id)
        if (saveVar != null && !saveVar.isBlank() && !saveVar.equalsIgnoreCase("null")) {
            // masalan: petId=$.id
            String[] parts = saveVar.split("=", 2);
            String varName = parts[0].trim();
            String jsonPath = parts.length > 1 ? parts[1].trim() : "$.id";

            // $.id -> id (RestAssured jsonPath shunaqa ishlaydi)
            if (jsonPath.startsWith("$."))
                jsonPath = jsonPath.substring(2);

            String val = resp.jsonPath().getString(jsonPath);
            if (val != null) ctx.put(varName, val);
        }
    }

    private static String replaceVars(String s) {
        if (s == null) return null;
        String out = s;
        for (var e : ctx.entrySet()) {
            out = out.replace("{{" + e.getKey() + "}}", e.getValue());
        }
        return out;
    }
}
