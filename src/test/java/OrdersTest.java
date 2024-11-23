import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.CreateCourierRequestModel;
import model.LoginModel;
import net.datafaker.Faker;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrdersTest {
    private static String login;
    private static String password;
    private static int id;
    private static Faker faker = new Faker();

    @Before
    public void setUp() {
        RestAssured.baseURI = API.baseURI;
        login = faker.lorem().word();
        password = faker.lorem().word();
        CreateCourierRequestModel requestModel = new CreateCourierRequestModel(login, password, faker.lorem().word());

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
    @Description("Проверка получения списка заказов")
    public void getOrdersTest(){
        Response response = given()
                .header("Content-type", "application/json")
                .queryParam("courierId", id)
                .when()
                .get(API.getOrdersPath);
        response.then().statusCode(200)
                .and()
                .assertThat().body("orders", notNullValue());
    }
}

