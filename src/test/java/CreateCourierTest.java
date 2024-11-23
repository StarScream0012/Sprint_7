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

public class CreateCourierTest {
    CreateCourierRequestModel requestModel;
    private static Faker faker = new Faker();

    @Before
    public void setUp() {
        RestAssured.baseURI = API.baseURI;
    }

    @Test
    @Description("Проверка создания курьера со всеми полями")
    public void createCourierTest() {
        requestModel = new CreateCourierRequestModel(faker.lorem().word(), faker.lorem().word(), faker.lorem().word());

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestModel)
                .when()
                .post(API.createCourierPath);
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @After
    public void deleteData() {
        LoginModel login = new LoginModel(requestModel.getLogin(), requestModel.getPassword());
        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(login)
                .when()
                .post(API.loginPath);
        loginResponse.then()
                .statusCode(200);
        int id = loginResponse.path("id");

        Response deleteResponse = given()
                .header("Content-type", "application/json")
                .when()
                .delete(API.deleteCourierPath + id);
        deleteResponse.then().statusCode(200);

    }

    @Test
    public void canNotDuplicateCourierTest() {
        requestModel = new CreateCourierRequestModel(faker.lorem().word(), faker.lorem().word(), faker.lorem().word());

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestModel)
                .when()
                .post(API.createCourierPath);
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
        Response responseDuplicateCourier = given()
                .header("Content-type", "application/json")
                .body(requestModel)
                .when()
                .post(API.createCourierPath);
        responseDuplicateCourier.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @Test
    public void canNotCreateWithSameLogin() {
        requestModel = new CreateCourierRequestModel(faker.lorem().word(), faker.lorem().word(), faker.lorem().word());

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestModel)
                .when()
                .post(API.createCourierPath);
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
        CreateCourierRequestModel requestWithSameLogin = new CreateCourierRequestModel(requestModel.getLogin(), faker.lorem().word(), faker.lorem().word());
        Response responseDuplicateCourier = given()
                .header("Content-type", "application/json")
                .body(requestModel)
                .when()
                .post(API.createCourierPath);
        responseDuplicateCourier.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

}
