/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import lambda.structures.LoadCSVRequest;
import org.json.JSONArray;
import saaf.Inspector;
import saaf.Response;
/**
 *
 * @author duy
 */
public class Query implements RequestHandler<Request, HashMap<String, Object>> {
    /**
     * Lambda Function Handler
     * 
     * @param request Request POJO with defined variables from Request.java
     * @param context 
     * @return HashMap that Lambda will automatically convert into JSON.
     */
    public HashMap<String, Object> handleRequest(
    		Request request,
    		Context context) {
        // Create logger
        LambdaLogger logger = context.getLogger(); 
        logger.log("Sucessfully create logger and trigger lambda code");

        // Collect initial data.
        final Inspector inspector = new Inspector();
        //inspector.inspectAll();
        
        // Create object to return result
        Response res = new Response();
    
        //****************ESTABLISH CONNECTION TO RDS SQL***********************
        try  {
            Properties properties = new Properties();
            properties.load(new FileInputStream("db.properties"));
            
            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String driver = properties.getProperty("driver");
            
            // Manually loading the JDBC Driver is commented out
            // No longer required since JDBC 4
            //Class.forName(driver);
            Connection con = DriverManager.getConnection(url,username,password);
            logger.log("Sucessfully connect to RDS");

            //****************PREPARE QUERY 1***********************
            PreparedStatement ps = con.prepareStatement("SELECT (AVG(orderProcessingTime)/24) as Avg_Processing_Days, \n" +
                                                            "AVG(grossMargin) as Avg_Gross_Margin, \n" +
                                                            "AVG(unitsSold) as Avg_Unit_Sold,\n" +
                                                            "MAX(unitsSold) as Max_Unit_Sold,\n" +
                                                            "MIN(unitsSold) as Min_Unit_Sold,\n" +
                                                            "SUM(unitsSold) as Total_Unit_Sold,\n" +
                                                            "SUM(totalRevenue) as Total_Revenue,\n" +
                                                            "SUM(totalProfit) as Total_Profit,\n" +
                                                            "COUNT(orderID) as Number_of_Orders\n" +
                                                            "FROM ORDERS;");
            logger.log("Sucessfully prepare SQL statements");

            ResultSet resultSet = ps.executeQuery();
            logger.log("Sucessfully execute");
            
            JSONArray aggregateResult = new JSONArray();
            String[] colNames = new String[] {"Avg_Processing_Days","Avg_Gross_Margin","Avg_Unit_Sold","Max_Unit_Sold","Min_Unit_Sold","Total_Unit_Sold","Total_Revenue","Total_Profit","Number_of_Orders"};
            JSONArray jArr = new JSONArray(colNames) ;
            //aggregateResult.put(Arrays.toString(colNames));
            aggregateResult.put(jArr);
            while (resultSet.next() ) {
	        JSONArray row = new JSONArray();
            	row.put( resultSet.getDouble("Avg_Processing_Days"));
            	row.put( resultSet.getDouble("Avg_Gross_Margin"));
            	row.put( resultSet.getDouble("Avg_Unit_Sold"));
            	row.put( resultSet.getInt("Max_Unit_Sold"));
                row.put( resultSet.getInt("Min_Unit_Sold"));
                row.put( resultSet.getDouble("Total_Unit_Sold"));
                row.put( resultSet.getDouble("Total_Revenue"));
                row.put( resultSet.getDouble("Total_Profit"));
                row.put( resultSet.getInt("Number_of_Orders"));
                //logger.log(row.toString());
            	aggregateResult.put(row);
                //logger.log(aggregateResult.toString());
            }
            logger.log("Sucessfully extract data");
            //logger.log("aggregateResult.toString(): " + aggregateResult.toString());          
            res.setValue(aggregateResult.toString());
            logger.log("res.getValue(): " + res.getValue());
            
            
            //****************PREPARE QUERY 2*************************
            ps = con.prepareStatement("SELECT country as Country, AVG(unitPrice) as Country_Avg_Unit_Price\n" +
                                        "FROM ORDERS\n" +
                                        "Group BY country;");
            resultSet = ps.executeQuery();
            aggregateResult = new JSONArray();
            colNames = new String[] {"Country","Country_Avg_Unit_Price"};
            jArr = new JSONArray(colNames) ;
            aggregateResult.put(jArr);
            while (resultSet.next() ) {
	        JSONArray row = new JSONArray();
            	row.put( resultSet.getString("Country"));
            	row.put( resultSet.getDouble("Country_Avg_Unit_Price"));
                //logger.log(row.toString());
            	aggregateResult.put(row);
                //logger.log(aggregateResult.toString());
            }
            res.setValue2(aggregateResult.toString());
            
            //****************PREPARE QUERY 3*************************
            ps = con.prepareStatement("SELECT country as Country, count(orderID) as Number_of_Orders\n" +
                                        "FROM ORDERS\n" +
                                        "GROUP BY country;");
            resultSet = ps.executeQuery();
            aggregateResult = new JSONArray();
            colNames = new String[] {"Country","Number_of_Orders"};
            jArr = new JSONArray(colNames) ;
            aggregateResult.put(jArr);
            while (resultSet.next() ) {
	        JSONArray row = new JSONArray();
            	row.put( resultSet.getString("Country"));
            	row.put( resultSet.getInt("Number_of_Orders"));
                //logger.log(row.toString());
            	aggregateResult.put(row);
                //logger.log(aggregateResult.toString());
            }
            res.setValue3(aggregateResult.toString());
            
            //****************PREPARE QUERY 4*************************
            ps = con.prepareStatement("SELECT country as Country, sum(unitsSold) as Total_Unit_Sold, AVG(unitPrice) as Avg_Unit_Price, AVG(unitCost) as Avg_Unit_Cost\n" +
                                        "FROM ORDERS\n" +
                                        "GROUP BY country;");
            resultSet = ps.executeQuery();
            aggregateResult = new JSONArray();
            colNames = new String[] {"Country","Total_Unit_Sold","Avg_Unit_Price","Avg_Unit_Cost"};
            jArr = new JSONArray(colNames) ;
            aggregateResult.put(jArr);
            while (resultSet.next() ) {
	        JSONArray row = new JSONArray();
            	row.put( resultSet.getString("Country"));
            	row.put( resultSet.getInt("Total_Unit_Sold"));
                row.put( resultSet.getDouble("Avg_Unit_Price"));
            	row.put( resultSet.getDouble("Avg_Unit_Cost"));
                //logger.log(row.toString());
            	aggregateResult.put(row);
                //logger.log(aggregateResult.toString());
            }
            res.setValue4(aggregateResult.toString());
            
            //****************PREPARE QUERY 5*************************
            ps = con.prepareStatement("SELECT *\n" +
                                        "FROM ORDERS;");
            resultSet = ps.executeQuery();
            aggregateResult = new JSONArray();
            colNames = new String[] {"region","country","itemType","salesChannel",
            "orderPriority","orderDate","orderId","shipDate",
            "unitsSold","unitPrice","unitCost","totalRevenue",
            "totalCost","totalProfit","orderProcessingTime","grossMargin"};
            jArr = new JSONArray(colNames) ;
            aggregateResult.put(jArr);
            while (resultSet.next() ) {
	        JSONArray row = new JSONArray();
            	row.put( resultSet.getString("region"));
            	row.put( resultSet.getString("country"));
                row.put( resultSet.getString("itemType"));
            	row.put( resultSet.getString("salesChannel"));
                
                row.put( resultSet.getString("orderPriority"));
            	row.put( resultSet.getString("orderDate"));
                row.put( resultSet.getInt("orderId"));
            	row.put( resultSet.getString("shipDate"));
                
                row.put( resultSet.getInt("unitsSold"));
            	row.put( resultSet.getDouble("unitPrice"));
                row.put( resultSet.getDouble("unitCost"));
            	row.put( resultSet.getDouble("totalRevenue"));
                
                row.put( resultSet.getDouble("totalCost"));
            	row.put( resultSet.getDouble("totalProfit"));
                row.put( resultSet.getInt("orderProcessingTime"));
            	row.put( resultSet.getDouble("grossMargin"));
                //logger.log(row.toString());
            	aggregateResult.put(row);
                //logger.log(aggregateResult.toString());
            }
            res.setValue5(aggregateResult.toString());
            
            //***************RETURN QUERY DATA ***********************
            inspector.consumeResponse(res);
            logger.log("Sucessfully put JSON data into the Reponse object");
        }  catch (Exception e){
            logger.log(e.toString());            
            logger.log(e.getMessage());
            logger.log(e.getLocalizedMessage());
        }      
        //Collect final information such as total runtime and cpu deltas.
        //inspector.inspectAllDeltas();
        return inspector.finish();
    }
}
