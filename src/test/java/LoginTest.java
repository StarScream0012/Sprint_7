import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.CreateCourierRequestModel;
import model.LoginModel;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginTest {
    private static String login;
    private static String password;
    private static int id;
    private static Faker faker = new Faker();

    @Before
    @Description("Подготовка для теста. Создание курьера для авторизации")
    public void setUp() {
        RestAssured.baseURI = API.baseURI;
        login = faker.lorem().word();
        password = faker.lorem().word();
        CreateCourierRequestModel
                requestModel = new CreateCourierRequestModel(login, password, faker.lorem().word());

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestModel)
                .when()
                .post(API.createCourierPath);
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
        LoginModel loginModel = new LoginModel(login, password);
        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(loginModel)
                .when()
                .post(API.loginPath);
        loginResponse.then()
                .statusCode(200)
                .and()
                .assertThat().body("id", notNullValue());
        id = loginResponse.path("id");
    }

    @Test
    @Description("Проверка создания курьера")
    public void loginTest() {
        LoginModel loginModel = new LoginModel(login, password);
        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(loginModel)
                .when()
                .post(API.loginPath);
        loginResponse.then()
                .statusCode(200)
                .and()
                .assertThat().body("id", notNullValue());

    }

    @Test
    public void incorrectLogin() {
        LoginModel loginModel = new LoginModel(faker.lorem().word(), password);
        Response incorrectResponse = given()
                .header("Content-type", "application/json")
                .body(loginModel)
                .when()
                .post(API.loginPath);
        incorrectResponse.then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    public void invorrectPassword() {
        LoginModel loginModel = new LoginModel(login, faker.lorem().word());
        Response incorrectResponse = given()
                .header("Content-type", "application/json")
                .body(loginModel)
                .when()
                .post(API.loginPath);
        incorrectResponse.then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void userDesNotExist() {
        LoginModel loginModel = new LoginModel(faker.lorem().word(), faker.lorem().word());
        Response incorrectResponse = given()
                .header("Content-type", "application/json")
                .body(loginModel)
                .when()
                .post(API.loginPath);
        incorrectResponse.then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void deleteData() {

        Response deleteResponse = given()
                .header("Content-type", "application/json")
                .when()
                .delete(API.deleteCourierPath + id);
        deleteResponse.then().statusCode(200);

    }
    
}


