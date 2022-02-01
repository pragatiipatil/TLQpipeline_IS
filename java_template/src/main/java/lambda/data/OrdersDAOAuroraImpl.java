package lambda.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;  
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat; 
import java.io.*;  

import static lambda.data.OrderDTO.DATE_FORMAT;
/**
 * @author Ranjana Bongale Ganesh
 */
public class OrdersDAOAuroraImpl implements OrdersDAO {

	public static final String MYSQL_INSTANCE_IP = "tcss562-project.cluster-cblp7mbc0hei.us-east-2.rds.amazonaws.com";
	public static final String MYSQL_USERNAME = "admin";
	public static final String MYSQL_PASSWORD = "Pranik2020";
	public static final String MYSQL_DB_NAME = "tcss562";
	public static final String MYSQL_TABLE_NAME = "ORDERS";
	public static final String MYSQL_CONNECTOR = "jdbc:mysql://" + MYSQL_INSTANCE_IP
									+ ":3306/" + MYSQL_DB_NAME
									+ "?user=" + MYSQL_USERNAME
									+ "&password=" + MYSQL_PASSWORD;
        public PreparedStatement myStmt = null;
        public Connection con = null;

	public OrdersDAOAuroraImpl() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (final ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Creates an order record in the Aurora database table based on the given order information
	 * @return
	 */
	public boolean createOrder(final OrderDTO orderDto) {
         
		return executeSqlStatement(String.format(
				"INSERT IGNORE INTO %S VALUES ("
				+ "'%s', \"%s\", '%s', '%s',"
				+ "'%s', STR_TO_DATE('%s', '%s'), '%s', STR_TO_DATE('%s', '%s'),"
				+ "'%s', '%s', '%s', '%s',"
				+ "'%s', '%s', '%s', '%s');",
				MYSQL_TABLE_NAME,
				orderDto.getRegion(),
				orderDto.getCountry(),
				orderDto.getItemType(),
	    		orderDto.getSalesChannel(),
	    		orderDto.getOrderPriority(),
	    		DATE_FORMAT.format(orderDto.getOrderDate()), "%m/%d/%Y",
	    		orderDto.getOrderId(),
	    		DATE_FORMAT.format(orderDto.getShipDate()), "%m/%d/%Y",
	    		orderDto.getUnitsSold(),
	    		orderDto.getUnitCost(),
	    		orderDto.getUnitCost(),
	    		orderDto.getTotalRevenue(),
	    		orderDto.getTotalCost(),
	    		orderDto.getTotalProfit(),
	    		orderDto.getOrderProcessingTime(),
	    		orderDto.getGrossMargin()));
	}

        
        public PreparedStatement createOrderNew ()
        {
           //PreparedStatement myStmt = null; 

            try{
                
            con = DriverManager.getConnection(MYSQL_CONNECTOR);
            //new line
            con.setAutoCommit(false);
            String query = "insert into ORDERS values (?,?,?,?,?,STR_TO_DATE(?,?),?,STR_TO_DATE(?,?),?,?,?,?,?,?,?,?)";
            myStmt = con.prepareStatement(query);
            return myStmt;
            }
            catch(SQLException exception)
            {
                exception.printStackTrace();
            }
            
            return myStmt;
        
        }
        
        public void addBatchFunction(final OrderDTO orderDto)
        {
            //Connection con = null;
            try{
        //con = DriverManager.getConnection(MYSQL_CONNECTOR);
        con.setAutoCommit(false);
        myStmt.setString(1,orderDto.getRegion()); 
        myStmt.setString(2,orderDto.getCountry());     
        myStmt.setString(3,orderDto.getItemType()); 
        myStmt.setString(4,orderDto.getSalesChannel());     
        myStmt.setString(5,orderDto.getOrderPriority());
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); 
        String strDate = dateFormat.format(orderDto.getOrderDate());
        myStmt.setString(6, DATE_FORMAT.format(orderDto.getOrderDate()));
        myStmt.setString(7,"%m/%d/%Y");
        myStmt.setString(8, orderDto.getOrderId()); 
        myStmt.setString(9, DATE_FORMAT.format(orderDto.getShipDate()));
        myStmt.setString(10,"%m/%d/%Y");
        myStmt.setInt(11,orderDto.getUnitsSold()); 
        myStmt.setFloat(12,orderDto.getUnitPrice()); 
        myStmt.setFloat(13,orderDto.getUnitCost());     
        myStmt.setFloat(14,orderDto.getTotalRevenue()); 
        myStmt.setFloat(15,orderDto.getTotalCost());  
        myStmt.setFloat(16,orderDto.getTotalProfit()); 
        myStmt.setFloat(17,orderDto.getOrderProcessingTime());
        myStmt.setFloat(18,orderDto.getGrossMargin());
        //myStmt.executeUpdate();
        myStmt.addBatch();
        //con.commit();
            }
        //catch(SQLException exception)
           // {
              //  exception.printStackTrace();
           // }
           
           catch (SQLException e) {
             e.printStackTrace();
             
      if (con != null) {
        try {
          System.err.print("Transaction is being rolled back");
          con.rollback();
        } catch (SQLException excep) {
          excep.printStackTrace();
        }
      }
    }
       
        }
        
        public void executeUpdateFunction()
        {
            try{
            myStmt.executeBatch();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
       
         /**
     * Deletes all the records in the database table
     * @return
     */
    public boolean deleteAll() {
            return executeSqlStatement("DELETE FROM " + MYSQL_TABLE_NAME);
    }
  
	/**
	 * Executes the given SQL statement
	 * @param sql
	 * @return
	 */
	private boolean executeSqlStatement(final String sql) {
        try (final Connection connection = DriverManager
        		.getConnection(MYSQL_CONNECTOR)) {
    		final Statement sqlStatement = connection.createStatement();
    		sqlStatement.executeUpdate(sql);
                return true;
        } catch (final SQLException ex) {
        	ex.printStackTrace();
            return false;
        }
	}

}
