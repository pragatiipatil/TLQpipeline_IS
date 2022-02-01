package lambda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.sql.PreparedStatement;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import lambda.data.OrderDTO;
import lambda.data.OrdersDAO;
import lambda.data.OrdersDAOAuroraImpl;
import lambda.structures.LoadCSVRequest;
import lombok.NonNull;
import saaf.Inspector;
import saaf.Response;

/**
 * @author Ranjana Bongale Ganesh
 */
public class LoadCSV implements RequestHandler<LoadCSVRequest, HashMap<String, Object>> {

	private static final String COMMA_DELIMETER = ",";
        
	
	private final OrdersDAO ordersDao = new OrdersDAOAuroraImpl();
         
	
    /**
     * Lambda Function Handler
     * 
     * @param request Request POJO with defined variables from Request.java
     * @param context 
     * @return HashMap that Lambda will automatically convert into JSON.
     */
    public HashMap<String, Object> handleRequest(
    		@NonNull final LoadCSVRequest request,
    		@NonNull final Context context) {
        // Collect initial data.
        final Inspector inspector = new Inspector();
        inspector.inspectAll();
        
        //****************START FUNCTION IMPLEMENTATION*************************
        // 1. Retrieve request parameters
        request.validate();
        final String bucketName = request.getBucketName();
        final String fileName = request.getFileName();
        final Response functionResponse = new Response();
        
		context.getLogger()
				.log("Load input bucketName: " + bucketName + "; fileName: " + fileName);

        // 2. Read CSV file
        final AmazonS3 s3Client = buildS3Client();
        final InputStream fileContentStream = s3Client.getObject(bucketName, fileName)
        											  .getObjectContent();
        
        
         // 3. Delete any existing data from the database
        ordersDao.deleteAll();
        
        try (final BufferedReader bufferedReader = new BufferedReader(
        			new InputStreamReader(fileContentStream))) {
        	String line = null;
                PreparedStatement ps = ordersDao.createOrderNew();
							
        	while ((line = bufferedReader.readLine()) != null) {
        		if (line.startsWith("Region")) {
        			// Skip if it is header line
        			continue;
        		}
        		
				// 3. Load into data store (Aurora database)
				ordersDao.addBatchFunction(
							OrderDTO.build(
									line.split(COMMA_DELIMETER)));
        		
        	}
                
                
               ordersDao.executeUpdateFunction();
               
			functionResponse.setValue("Load Status: SUCCESS");
        } catch (final IOException | ParseException ex) {
			functionResponse.setValue("Load Status: FAILURE");
			ex.printStackTrace();
		}
        
        inspector.consumeResponse(functionResponse);
        //****************END FUNCTION IMPLEMENTATION***************************
        
        //Collect final information such as total runtime and cpu deltas.
        inspector.inspectAllDeltas();
        return inspector.finish();
    }
    
    private AmazonS3 buildS3Client() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_2)
                .build();
    }
    
}
