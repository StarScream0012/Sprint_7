import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.CreateOrderModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
        RestAssured.baseURI = API.baseURI;

    }

    @Test
    @Description("Проверка создания заказа")
    public void createOrderTest() {
        List color = List.of(colors);
        CreateOrderModel createOrderModel = new CreateOrderModel(color);
        Response response = given()
                .header("Content-type", "application/json")
                .body(createOrderModel)
                .when()
                .post(API.createOrderPath);
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
