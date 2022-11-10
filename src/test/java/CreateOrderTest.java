import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.io.IOException;
import java.util.Arrays;
import static org.junit.Assert.assertNotEquals;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private static String[] color;
    private int orderTrack;

    public CreateOrderTest(String firstName, String lastName, String address, String metroStation, String phone,
                           int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters (name = "Тестовые данные: {0} {1} {2} {3} {4} {5} {6} {7} {8} ")
    public static Iterable<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {"Кот", "Котович", "Котовская улица", "Алтуфьево", "88006320000", 9, "2022-11-07", "COMMENTS", new String[]{"GREY"}},
                {"Семен", "Семенов", "Калининская, 22", "Бибирево", "+78006320000", 3, "2022-07-07", "Комментарий", new String[]{"BLACK", "GREY"}},
                {"Bob", "Bobov", "Chernaya 10", "Sokolniki", "79623563250", 2, "2021-11-20", "no comments", new String[]{}},
        });
    }


    @Test
    @DisplayName("Тест создания заказа - /api/v1/order")
    public void createOrderOkTest() throws IOException {
        Order order = new Order(firstName, lastName, address, metroStation, phone,
                rentTime, deliveryDate, comment, color);
        orderTrack = OrderApi.getCreateOrder(OrderApi.serialisationOrder(order))
                .extract()
                .path("track");
        assertNotEquals(0, orderTrack);
    }
}
