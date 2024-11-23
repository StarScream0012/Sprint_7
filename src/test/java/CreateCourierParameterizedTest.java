import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.CreateCourierRequestModel;
import net.datafaker.Faker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
@RunWith(Parameterized.class)
public class CreateCourierParameterizedTest {
    private final String login;
    private final String password;
    private final String firstName;
    private static Faker faker = new Faker();

    @Before
    public void setUp() {
        RestAssured.baseURI =API.baseURI;
    }

    public CreateCourierParameterizedTest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Test
    @Description("Проверка создания курьера с одним отсутствующим параметром")
    public void oneParameterEmptyTest() {
        CreateCourierRequestModel model = new CreateCourierRequestModel(login, password, firstName);
        Response response = given()
                .header("Content-type", "application/json")
                .body(model)
                .when()
                .post(API.createCourierPath);
        response.then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));


    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {faker.lorem().word(),faker.lorem().word(), ""},
                {faker.lorem().word(), "", faker.lorem().word()},
                {"", faker.lorem().word(), faker.lorem().word()}
        };
    }
}

