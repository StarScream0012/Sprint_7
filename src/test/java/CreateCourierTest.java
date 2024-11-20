import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest {
    CreateCourierRequestModel requestModel;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Step("Проверка создания курьера со всеми полями")
    public void creatCourierTest() {
        requestModel = new CreateCourierRequestModel(generateRandomString(10), generateRandomString(10), generateRandomString(10));

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestModel)
                .when()
                .post("api/v1/courier");
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
                .post("api/v1/courier/login");
        loginResponse.then()
                .statusCode(200);
        int id = loginResponse.path("id");

        Response deleteResponse = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + id);
        deleteResponse.then().statusCode(200);

    }

    @Test
    public void canNotDuplicateCourierTest() {
        requestModel = new CreateCourierRequestModel(generateRandomString(10), generateRandomString(10), generateRandomString(10));

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestModel)
                .when()
                .post("api/v1/courier");
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
        Response responseDuplicateCourier = given()
                .header("Content-type", "application/json")
                .body(requestModel)
                .when()
                .post("api/v1/courier");
        responseDuplicateCourier.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @Test
    public void canNotCreateWithSameLogin() {
        requestModel = new CreateCourierRequestModel(generateRandomString(10), generateRandomString(10), generateRandomString(10));

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestModel)
                .when()
                .post("api/v1/courier");
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
        CreateCourierRequestModel requestWithSameLogin = new CreateCourierRequestModel(requestModel.getLogin(), generateRandomString(10), generateRandomString(10));
        Response responseDuplicateCourier = given()
                .header("Content-type", "application/json")
                .body(requestModel)
                .when()
                .post("api/v1/courier");
        responseDuplicateCourier.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
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
