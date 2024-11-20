import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrdersTest {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();
    private static String login;
    private static String password;
    private static int id;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        login = generateRandomString(10);
        password = generateRandomString(10);
        CreateCourierRequestModel requestModel = new CreateCourierRequestModel(login, password, generateRandomString(10));

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestModel)
                .when()
                .post("api/v1/courier");
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
        LoginModel loginModel = new LoginModel(login, password);
        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(loginModel)
                .when()
                .post("api/v1/courier/login");
        loginResponse.then()
                .statusCode(200)
                .and()
                .assertThat().body("id", notNullValue());
        id = loginResponse.path("id");
    }
    @Test
    @Step("Проверка получения списка заказов")
    public void getOrdersTest(){
        Response response = given()
                .header("Content-type", "application/json")
                .queryParam("courierId", id)
                .when()
                .get("/api/v1/orders");
        response.then().statusCode(200)
                .and()
                .assertThat().body("orders", notNullValue());
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

