package lambda;

import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.lang.Object;
import java.util.concurrent.TimeUnit ;
import java.util.*;
import java.time.temporal.ChronoUnit; 
import java.text.ParseException;

/**
 *
 * @author Wes Lloyd
 */
public class Request {
    private String bucketname;
    private String filename;
    private String filename1;
    private String region;
    private String country;
    private String itemType;
    private String salesChannel;
    private String orderPriority;
    private Date orderDate;
    private String orderId;
    private Date shipDate;
    private String unitSold;
    private String unitPrice;
    private String unitCost;
    private String totalRevenue;
    private String totalCost;
    private String totalProfit;
    private long orderProcessingTime;
    private float grossMargin;
    private String header1;
    private String header2;
    
     public void validate() {
    	if (this.bucketname == null) {
    		throw new IllegalStateException("Bucket name is null");
    	}
    	
    	if (this.filename == null) {
    		throw new IllegalStateException("File name is null");
    	}
    }
    
    public String getBucketname() {
        return bucketname;
    }
    
    public Request(){
        
    }
    
    public String getFilename1() {
        return filename1;
    }
       public String getFilename() {
        return filename;
    }
        
    public void setFilename1(String filename1) {
        this.filename1 = filename1;
    }
    
         public String getHeader1() {
        return header1;
    }
        
    public void setHeader2(String header2) {
        this.header2 = header2;
    }
   
        public String getHeader2() {
        return header2;
    }
        
    public void setHeader1(String header1) {
        this.header1 = header1;
    }
   
    public void setBucketname(String bucketname)
    {
        this.bucketname = bucketname;
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
 public void setRegion(String region){
        this.region = region;
    }
      
   public String getRegion() {
        return region;
    }
    
     public void setCountry(String country){
        this.country = country;
    }
      
   public String getCountry() {
        return country;
    }
    
      public void setItemType(String itemType){
        this.itemType = itemType;
    }
      
   public String getItemType() {
        return itemType;
    }
    
       public void setSalesChannel(String salesChannel){
        this.salesChannel = salesChannel;
    }
      
   public String getSalesChannel() {
        return salesChannel;
    }
   
       public void setOrderPriority(String op){
           switch(op)
           {
               case "L":
                   this.orderPriority = "Low";
               case "M":
                   this.orderPriority = "Medium";
               case "H":
                   this.orderPriority = "High";
               case "C" :
                   this.orderPriority = "Critical";
               default:
                    this.orderPriority = op;

           }
          
    }
      
   public String getOrderPriority() {
        return orderPriority;
    }
   
     public void setOrderId(String orderId){
        this.orderId = orderId;
    }
      
   public String getOrderId() {
        return orderId;
    }

      public void setUnitSold(String unitSold){
        this.unitSold = unitSold;
    }
      
   public String getUnitSold() {
        return this.unitSold;
    }

       public void setUnitPrice(String unitPrice){
        this.unitPrice = unitPrice;
    }
      
   public String getUnitPrice() {
        return this.unitPrice;
    }

        public void setUnitCost(String unitCost){
        this.unitCost = unitCost;
    }
      
   public String getUnitCost() {
        return this.unitCost;
    }

            public void setTotalRevenue(String totalRevenue){
        this.totalRevenue = totalRevenue;
    }
      
   public String getTotalRevenue() {
        return this.totalRevenue;
    }

     public void setTotalCost(String totalCost){
        this.totalCost = totalCost;
    }
      
   public String getTotalCost() {
        return this.totalCost;
    }

     public void setTotalProfit(String totalProfit){
        this.totalProfit = totalProfit;
    }
      
   public String getTotalProfit() {
        return this.totalProfit;
    }

      public void setGrossMargin(){
        this.grossMargin = (Float.parseFloat(this.getTotalProfit())) / 
                (Float.parseFloat(this.getTotalRevenue()));
    }
      
   public float getGrossMargin() {
        return this.grossMargin;
    }
   public void setOrderDate(String orderDate) throws ParseException
    {
     SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	Date d1 = format.parse(orderDate);
	this.orderDate = d1;

        
    }
      
   public Date getOrderDate()
    {
        return this.orderDate;
    }

     public void setShipDate(String shipDate) throws ParseException
    {
     
    	SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date d1 = format.parse(shipDate);
		this.shipDate = d1;
        
    }
      
   public Date getShipDate()
    {
        return this.shipDate;
    }

public void setOrderProcessingTime(){

Date d1 = null;
Date d2 = null;
d1 = this.getOrderDate();
d2 = this.getShipDate();
//in milliseconds
long diff = d2.getTime() - d1.getTime();

//long diffSeconds = diff / 1000 % 60;
//long diffMinutes = diff / (60 * 1000) % 60;
//long diffHours = diff / (60 * 60 * 1000) % 24;
long diffDays = diff / (24 * 60 * 60 * 1000);

        this.orderProcessingTime = diffDays;
  }
      
   public long getOrderProcessingTime() {
        return orderProcessingTime;
    }

}
