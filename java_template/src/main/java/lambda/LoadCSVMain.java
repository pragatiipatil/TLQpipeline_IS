package lambda;

import java.util.HashMap;

import lambda.structures.LoadCSVRequest;
import lambda.test.TestContext;

public class LoadCSVMain {
	
    public static void main(final String[] args) {
    	final LoadCSVRequest request = LoadCSVRequest.builder()
    			.bucketName(args[0])
    			.fileName(args[1])
    			.build();
        System.out.println("cmd-line parameters: " + request.toString());

        final HashMap<String, Object> response = new LoadCSV()
        		.handleRequest(request, new TestContext());
        System.out.println("function result:" + response.toString());
    }
    
}
