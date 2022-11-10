import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

public class CreateCourierTest {

    private int courierId;
    private boolean createCourierIsOk = false; //признак создания учетной записи курьера

    Courier courier;

    @After
    public void tearDown(){
        if (createCourierIsOk) {
            Courier createdCourier = new Courier(courier.getLogin(), courier.getPassword());
            //получение id созданной учетной записи курьера
            courierId = CourierApi.getCourierAuthorization(createdCourier)
                    .assertThat()
                    .statusCode(200)
                    .extract()
                    .path("id");
            CourierApi.deleteCourier(courierId);  //удаление учетной записи курьера
        }
    }

    @Test
    @DisplayName("Тест создания учетной записи курьера /api/v1/courier")
    public void createCourierOkTest(){
        courier = Courier.getRandomCourier();
        createCourierIsOk = CourierApi.getCreateCourier(courier)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");
        assertTrue(createCourierIsOk);
    }

    @Test
    @DisplayName("Тест неуспешного создания курьера без логина /api/v1/courier")
    public void createCourierWithOutLoginFailTest(){
        courier = Courier.getWithoutLogin();
        CourierApi.getCreateCourier(courier)
                .assertThat()
                .statusCode(400)
                .body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Тест неуспешного создания курьера без пароля /api/v1/courier")
    public void createCourierWithOutPasswordFailTest() throws JsonProcessingException {
        courier = Courier.getWithoutPassword();
        CourierApi.getCreateCourier(courier)
                .assertThat()
                .statusCode(400)
                .body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Тест неуспешного создания курьера с повторяющимся логином. Тест упал - response не соответствует документации API /api/v1/courier")
    public void createCourierRecurringLoginFailTest() {
        courier = Courier.getRandomCourier();
        createCourierIsOk =  CourierApi.getCreateCourier(courier)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");
        CourierApi.getCreateCourier(courier)
                .assertThat()
                .statusCode(409)
                .body("message",equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Тест создания курьера с повторяющимся логином. Тест с фактическим сообщением ответа /api/v1/courier")
    public void createCourierRecurringLoginNotFailTest(){
        courier = Courier.getRandomCourier();
        createCourierIsOk =  CourierApi.getCreateCourier(courier)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");
        CourierApi.getCreateCourier(courier)
                .assertThat()
                .statusCode(409)
                .body("message",equalTo("Этот логин уже используется. Попробуйте другой."));
    }

}
