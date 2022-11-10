import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotEquals;

public class LoginCourierTest {
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
            CourierApi.deleteCourier(courierId);             //удаление учетной записи курьера
        }
    }


    @Test
    @DisplayName("Тест успешной авторизации курьера. /api/v1/courier/login")
    public void authorizationCourierOkTest() {
        courier = Courier.getRandomCourier();
        createCourierIsOk = CourierApi.getCreateCourier(courier)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");
        Courier createdCourier = new Courier(courier.getLogin(), courier.getPassword());
        courierId = CourierApi.getCourierAuthorization(createdCourier)
                .assertThat()
                .statusCode(200)
                .extract()
                .path("id");
        assertNotEquals(0, courierId);
    }


    @Test
    @DisplayName("Тест неуспешной авторизации курьера без логина. /api/v1/courier/login")
    public void authorizationCourierWithOutLoginFailTest() {
        courier = Courier.getRandomCourier();
        createCourierIsOk = CourierApi.getCreateCourier(courier)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");
        Courier createdCourier = new Courier("", courier.getPassword());
        CourierApi.getCourierAuthorization(createdCourier)
                .assertThat()
                .statusCode(400)
                .body("message",equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Тест неуспешной авторизации курьера без пароля. /api/v1/courier/login")
    public void authorizationCourierWithOutPasswordFailTest() {
        courier = Courier.getRandomCourier();
        createCourierIsOk = CourierApi.getCreateCourier(courier)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");
        Courier createdCourier = new Courier(courier.getLogin(), "");
        CourierApi.getCourierAuthorization(createdCourier)
                .assertThat()
                .statusCode(400)
                .body("message",equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Тест неуспешной авторизации курьера c несуществующим логином. /api/v1/courier/login")
    public void authorizationCourierWithWrongLoginFailTest() {
        courier = Courier.getRandomCourier();
        createCourierIsOk = CourierApi.getCreateCourier(courier)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");
        Courier createdCourier = new Courier((courier.getLogin() + "1"), courier.getPassword());
        CourierApi.getCourierAuthorization(createdCourier)
                .assertThat()
                .statusCode(404)
                .body("message",equalTo("Учетная запись не найдена"));
    }


    @Test
    @DisplayName("Тест неуспешной авторизации курьера c несуществующим паролем. /api/v1/courier/login")
    public void authorizationCourierWithWrongPasswordFailTest() {
        courier = Courier.getRandomCourier();
        createCourierIsOk = CourierApi.getCreateCourier(courier)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");
        Courier createdCourier = new Courier(courier.getLogin(), (courier.getPassword() + "1"));
        CourierApi.getCourierAuthorization(createdCourier)
                .assertThat()
                .statusCode(404)
                .body("message",equalTo("Учетная запись не найдена"));
    }


    @Test
    @DisplayName("Тест неуспешной авторизации несуществующего курьера. /api/v1/courier/login")
    public void authorizationNonExistentCourierFailTest() {
        courier = Courier.getRandomCourier();
        Courier createdCourier = new Courier(courier.getLogin(), courier.getPassword());
        CourierApi.getCourierAuthorization(createdCourier)
                .assertThat()
                .statusCode(404)
                .body("message",equalTo("Учетная запись не найдена"));
    }
}
