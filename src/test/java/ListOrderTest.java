import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;


public class ListOrderTest {

    @Test
    @DisplayName("Список заказов - /api/v1/order")
    public void getListOrderTest() {
        OrderApi.getListOrder();
    }

}
