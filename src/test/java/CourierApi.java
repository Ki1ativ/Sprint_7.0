import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class CourierApi {
    private static final String CREATE = "/courier";
    private static final String DELETE = "/courier/{courierId}";
    private static final String LOGIN = "/courier/login";
    static Courier courier = new Courier();


    //создание учетной записи курьера /api/v1/courier"
    public static ValidatableResponse getCreateCourier(Courier courier){
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(courier)
                .when()
                .post(CREATE)
                .then().log().all();
    }

    //авторизация учетной записи курьера /api/v1/courier/login"
    public static ValidatableResponse getCourierAuthorization (Courier courier) {
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(courier)
                .when()
                .post(LOGIN)
                .then().log().all();
    }

    //удаление учетной записи курьера /api/v1/courier/:id"
    public static ValidatableResponse deleteCourier(int courierId) {
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(courier)
                .pathParam("courierId", courierId)
                .when()
                .delete(DELETE)
                .then().log().all()
                .assertThat()
                .statusCode(200);
    }
}
