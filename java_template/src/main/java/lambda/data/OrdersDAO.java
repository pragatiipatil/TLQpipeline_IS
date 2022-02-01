package lambda.data;
import java.sql.PreparedStatement;
import java.sql.Connection;

/**
 * @author Ranjana Bongale Ganesh
 */
public interface OrdersDAO {
	public boolean createOrder(final OrderDTO orderDto);
        public PreparedStatement createOrderNew();
        public void addBatchFunction(final OrderDTO orderDto);
        public void executeUpdateFunction();
            public boolean deleteAll();
	
}
