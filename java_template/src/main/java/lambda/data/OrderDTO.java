package lambda.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

/**
 * @author Ranjana Bongale Ganesh
 */
@Data
public class OrderDTO {

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy"); 
        
	private String region;
	private String country;
	private String itemType;
    private String salesChannel;
    private String orderPriority;
    private Date orderDate;
    private String orderId;
    private Date shipDate;
    private int unitsSold;
    private float unitPrice;
    private float unitCost;
    private float totalRevenue;
    private float totalCost;
    private float totalProfit;
    private long orderProcessingTime;
    private float grossMargin;
	
    public static OrderDTO build(final String[] input) throws ParseException {
    	final OrderDTO order = new OrderDTO();
    	order.setRegion(input[0]);
    	order.setCountry(input[1]);
    	order.setItemType(input[2]);
    	order.setSalesChannel(input[3]);
    	order.setOrderPriority(input[4]);
    	order.setOrderDate(DATE_FORMAT.parse(input[5]));
    	order.setOrderId(input[6]);
    	order.setShipDate(DATE_FORMAT.parse(input[7]));
    	order.setUnitsSold(Integer.parseInt(input[8]));
    	order.setUnitPrice(Float.parseFloat(input[9]));
    	order.setUnitCost(Float.parseFloat(input[10]));
    	order.setTotalRevenue(Float.parseFloat(input[11]));
    	order.setTotalCost(Float.parseFloat(input[12]));
    	order.setTotalProfit(Float.parseFloat(input[13]));
    	order.setOrderProcessingTime(Long.parseLong(input[14]));
    	order.setGrossMargin(Float.parseFloat(input[15]));
    	return order;
    }
    
}
