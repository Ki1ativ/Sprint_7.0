import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.response.ValidatableResponse;
import java.io.IOException;
import static io.restassured.RestAssured.given;

public class OrderApi {
    private static final String GENERATE = "/orders";
    static Order order = new Order();
    private static String jsonOrder;


    //создание заказа /api/v1/orders
    public static ValidatableResponse getCreateOrder(String order){
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(order)
                .when()
                .post(GENERATE)
                .then().log().all()
                .statusCode(201);
    }

    //получение списка заказов /api/v1/orders
    public static ValidatableResponse getListOrder(){
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(order)
                .when()
                .get(GENERATE)
                .then().log().all()
                .and()
                .assertThat()
                .statusCode(200);
    }

    //Сериализация заказов в JSON
    public static String serialisationOrder(Order order) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        jsonOrder = mapper.writeValueAsString(order);
        return jsonOrder;
    }
}
