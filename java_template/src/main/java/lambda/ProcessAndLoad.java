//package lambda;
//
//import com.amazonaws.services.lambda.runtime.Context;
//        import com.amazonaws.services.lambda.runtime.RequestHandler;
//        import saaf.Inspector;
//        import saaf.Response;
//        import java.util.HashMap;
//        import java.io.Writer;
//        import java.util.*;
//        import java.io.*;
//        import java.nio.charset.StandardCharsets;
//        import  com.amazonaws.services.s3.model.ObjectMetadata ;
//        import  com.amazonaws.services.s3.AmazonS3ClientBuilder;
////import com.amazonaws.services.s3;
//
//        import com.amazonaws.services.s3.AmazonS3Client;
//        import com.amazonaws.services.s3.AmazonS3;
//        import com.amazonaws.services.lambda.runtime.LambdaLogger;
//        import com.amazonaws.services.s3.model.S3Object;
//        import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//        import com.amazonaws.services.s3.model.GetObjectRequest;
//        import java.util.Date;
//        import java.text.SimpleDateFormat;
//        import java.text.DateFormat;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.text.ParseException;
//import java.util.HashMap;
//
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//
//        import lambda.data.OrderDTO;
//        import lambda.data.OrdersDAO;
//        import lambda.data.OrdersDAOAuroraImpl;
//        import lambda.structures.LoadCSVRequest;
//        import lombok.NonNull;
//        import saaf.Inspector;
//        import saaf.Response;
//
//
///**
// *
// * @author Pragati Patil
//
// */
//public class ProcessAndLoad implements RequestHandler<Request, HashMap<String, Object>> {
//    private static final String COMMA_DELIMETER = ",";
//    private final OrdersDAO ordersDao = new OrdersDAOAuroraImpl();
//
//    /**
//     * Lambda Function Handler
//     *
//     * @param request Request POJO with defined variables from Request.java
//     * @param context
//     * @return HashMap that Lambda will automatically convert into JSON.
//     */
//    public HashMap<String, Object> handleRequest(Request request, Context context) {
//
//        //Collect inital data.
//        Inspector inspector = new Inspector();
//        inspector.inspectAll();
//        request.validate();
//
//// Create new file on S3
//        AmazonS3 s3Client1 = AmazonS3ClientBuilder.standard().build();
////s3Client1.putObject(bucketname, filename, is, meta);
//
//        final String srcBucket = request.getBucketname();
//        final String srcKey = request.getFilename();
//        String srcKey1 = request.getFilename1();
//        String newColumn1 = request.getHeader1();
//        String newColumn2 = request.getHeader2();
//
//        final Response functionResponse = new Response();
//
//        context.getLogger()
//                .log("Load input bucketName: " + srcBucket + "; srcKey: " + srcKey);
//
////get object file using source bucket and srcKey name
//        S3Object s3Object = s3Client1.getObject(new GetObjectRequest(srcBucket, srcKey));
////get content of the file
//        InputStream objectData = s3Object.getObjectContent();
////scanning data line by line
//        String text = "";
//        String line = "";
//        Scanner scanner = new Scanner(objectData);
//        int i = 0;
//        int index = 0;
//        LambdaLogger logger = context.getLogger();
////Request[] req = new Request[10];
//        List<Request> req = new ArrayList<Request>();
//        Map<String, Integer> map = new HashMap<>();
//        String[] header = new String[14];
//        int count = 1;
//        while (scanner.hasNext()) {
//
//            line = scanner.nextLine();
//            String[] arr = line.split(",");
//
//            if(i == 0)
//            {
//                header = line.split(",");
//            }
//
//            if( i != 0 && !map.containsKey(arr[6]))
//            {
//                map.put(arr[6],1);
//                try{
//
//                    Request tmp = new Request();
//                    //req[i] = new Request()
//                    tmp.setRegion(arr[0]);
//                    tmp.setCountry(arr[1]);
//                    tmp.setItemType(arr[2]);
//                    tmp.setSalesChannel(arr[3]);
//                    tmp.setOrderPriority(arr[4]);
//                    tmp.setOrderDate(arr[5]);
//                    tmp.setOrderId(arr[6]);
//                    tmp.setShipDate(arr[7]);
//                    tmp.setUnitSold(arr[8]);
//                    tmp.setUnitPrice(arr[9]);
//                    tmp.setUnitCost(arr[10]);
//                    tmp.setTotalRevenue(arr[11]);
//                    tmp.setTotalCost(arr[12]);
//                    tmp.setTotalProfit(arr[13]);
//                    tmp.setOrderProcessingTime();
//                    tmp.setGrossMargin();
//
//                    logger.log(count + ": " + "Order Priority:  " + tmp.getOrderPriority() + " ");
//                    logger.log("Order Processing Time:  " + tmp.getOrderProcessingTime() + " ");
//                    logger.log("Order Gross Margin:  " + tmp.getGrossMargin() + "\n");
//                    count++;
//
//                    req.add(tmp);
//                }
//                catch(Exception e)
//                {
//                    logger.log( "in catch block" + e);
//                }
//            }
//
//            text += line;
//            i++;
//        }
//        scanner.close();
//
//        logger.log("ProcessCSV bucketname:" + srcBucket + " filename:" + srcKey);
//
//        StringWriter sw = new StringWriter();
//        for(int x = 0; x < header.length; x++)
//        {
//            sw.append(header[x]);
//            sw.append(",");
//        }
//        sw.append(newColumn1 + ",");
//        sw.append(newColumn2 + "\n");
//
//
//        for (int z=0;z<req.size();z++)
//        {
//            sw.append(req.get(z).getRegion());
//            sw.append(",");
//            sw.append(req.get(z).getCountry());
//            sw.append(",");
//            sw.append(req.get(z).getItemType());
//            sw.append(",");
//            sw.append(req.get(z).getSalesChannel());
//            sw.append(",");
//            sw.append(req.get(z).getOrderPriority());
//            sw.append(",");
//            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//            String strDate = dateFormat.format(req.get(z).getOrderDate());
//            sw.append(strDate);
//            sw.append(",");
//            sw.append(req.get(z).getOrderId());
//            sw.append(",");
//            String endDate = dateFormat.format(req.get(z).getShipDate());
//            sw.append(endDate);
//            sw.append(",");
//            sw.append(Integer.toString(req.get(z).getUnitSold()));
//            sw.append(",");
//            sw.append(Float.toString(req.get(z).getUnitPrice()));
//            sw.append(",");
//            sw.append(Float.toString(req.get(z).getUnitCost()));
//            sw.append(",");
//            sw.append(Float.toString(req.get(z).getTotalRevenue()));
//            sw.append(",");
//            sw.append(Float.toString(req.get(z).getTotalCost()));
//            sw.append(",");
//            sw.append(Float.toString(req.get(z).getTotalProfit()));
//            sw.append(",");
//            sw.append(Long.toString(req.get(z).getOrderProcessingTime()));
//            sw.append(",");
//            sw.append(Float.toString(req.get(z).getGrossMargin()));
//            sw.append("\n");
//
//
//        }
//
//
//        logger.log("sw is: " + sw.toString());
//        logger.log("file name is: " + srcKey1);
//        logger.log("header1 name is: " + newColumn1);
//        logger.log("header2 name is: " + newColumn2);
//
//
//
//        byte[] bytes = sw.toString().getBytes(StandardCharsets.UTF_8);
//        InputStream is = new ByteArrayInputStream(bytes);
//        ObjectMetadata meta = new ObjectMetadata();
//        meta.setContentLength(bytes.length);
//        meta.setContentType("text/plain");
//// Create new file on S3
//        final AmazonS3 s3Client = buildS3Client();
//        s3Client.putObject(srcBucket, srcKey1, is, meta);
//        //todo: write CSV file to /tmp folder
//
//        functionResponse.setValue("Bucket: " + srcBucket + " filename:" + srcKey + " processed.");
//
//        //todo: try to read from CSV file in /tmp folder first
//        final InputStream fileContentStream = s3Client.getObject(srcBucket, srcKey1)
//                .getObjectContent();
//
//        try (final BufferedReader bufferedReader = new BufferedReader(
//                new InputStreamReader(fileContentStream))) {
//            line = null;
//            while ((line = bufferedReader.readLine()) != null) {
//                if (line.startsWith("Region")) {
//                    // Skip if it is header line
//                    continue;
//                }
//                // 3. Load into data store (Aurora database)
//                ordersDao.createOrder(
//                        OrderDTO.build(
//                                line.split(COMMA_DELIMETER)));
//            }
//            functionResponse.setValue("Load Status: SUCCESS");
//        } catch (final IOException | ParseException ex) {
//            functionResponse.setValue("Load Status: FAILURE");
//            ex.printStackTrace();
//        }
//
//        inspector.consumeResponse(functionResponse);
//        //****************END FUNCTION IMPLEMENTATION***************************
//
//        //Collect final information such as total runtime and cpu deltas.
//        inspector.inspectAllDeltas();
//        return inspector.finish();
//    }
//
//    private AmazonS3 buildS3Client() {
//        return AmazonS3ClientBuilder.standard()
//                .withRegion(Regions.US_EAST_2)
//                .build();
//    }
//}