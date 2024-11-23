import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.LoginModel;
import net.datafaker.Faker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

    @RunWith(Parameterized.class)
    public class LoginParameterizedTest {
        private  String login;
        private  String password;


        private static Faker faker = new Faker();
        public  LoginParameterizedTest(String login, String password) {
            this.login = login;
            this.password = password;

        }
        @Before
        public void setUp() {
            RestAssured.baseURI =API.baseURI;
        }

        @Test
        @Description("Проверка авторизации с одним отсутствующим параметром")
        public void oneParameterEmptyTest() {

            LoginModel model = new LoginModel (login, password);
            Response response = given()
                    .header("Content-type", "application/json")
                    .body(model)
                    .when()
                    .post(API.loginPath);
            response.then()
                    .statusCode(400)
                    .and()
                    .assertThat().body("message", equalTo("Недостаточно данных для входа"));
        }
        @Parameterized.Parameters
        public static Object[][] data() {
            return new Object[][]{
                    {faker.lorem().word(), ""},
                    {"", faker.lorem().word()}
            };
        }

    }


