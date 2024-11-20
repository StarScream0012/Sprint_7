import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

    @RunWith(Parameterized.class)
    public class LoginParameterizedTest {
        private  String login;
        private  String password;


        private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        private static final Random RANDOM = new Random();
        public  LoginParameterizedTest(String login, String password) {
            this.login = login;
            this.password = password;

        }
        @Before
        public void setUp() {
            RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        }

        @Test
        @Step("Проверка авторизации с одним отсутствующим параметром")
        public void oneParameterEmptyTest() {

            LoginModel model = new LoginModel (login, password);
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
                    {generateRandomString(10), ""},
                    {"", generateRandomString(10)}
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


