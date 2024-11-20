import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    String colors[];
    public CreateOrderTest(String[] colors) {
        this.colors = colors;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

    }

    @Test
    @Step("Проверка создания заказа")
    public void createOrderTest() {
        List color = List.of(colors);
        CreateOrderModel createOrderModel = new CreateOrderModel(color);
        Response response = given()
                .header("Content-type", "application/json")
                .body(createOrderModel)
                .when()
                .post("api/v1/orders");
        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);

    }

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new String[]{"BLACK", "GRAY"}},
                {new String[]{"BLACK"}},
                {new String[]{"GRAY"}},
                {new String[]{}}
        });
    }
}
