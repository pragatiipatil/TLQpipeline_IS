package lambda.structures;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadCSVRequest {
    private String bucketName;
    private String fileName;
    
    public void validate() {
    	if (this.bucketName == null) {
    		throw new IllegalStateException("Bucket name is null");
    	}
    	
    	if (this.fileName == null) {
    		throw new IllegalStateException("File name is null");
    	}
    }
}
