import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class CreateCourierParameterizedTest {
    private final String login;
    private final String password;
    private final String firstName;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    public CreateCourierParameterizedTest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Test
    @Step("Проверка создания курьера с одним отсутствующим параметром")
    public void oneParameterEmptyTest() {
        CreateCourierRequestModel model = new CreateCourierRequestModel(login, password, firstName);
        Response response = given()
                .header("Content-type", "application/json")
                .body(model)
                .when()
                .post("api/v1/courier");
        response.then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));


    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {generateRandomString(10), generateRandomString(10), ""},
                {generateRandomString(10), "", generateRandomString(10)},
                {"", generateRandomString(10), generateRandomString(10)}
        };
    }

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}

