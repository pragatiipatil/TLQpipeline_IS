package saaf;

/**
 * A basic Response object that can be consumed by FaaS Inspector
 * to be used as additional output.
 * 
 * @author Wes Lloyd
 * @author Robert Cordingly
 */
public class Response {
    //
    // User Defined Attributes
    //
    //
    // ADD getters and setters for custom attributes here.
    //

    // Return value
    private String value1;
    public String getValue() {
        return value1;
    }
    public void setValue(String value) {
        this.value1 = value;
    }
    
    private String value2 = "2";
    public String getValue2() {
        return value2;
    }
    public void setValue2(String value2) {
        this.value2 = value2;
    }
    
    private String value3 = "3";
    public String getValue3() {
        return value3;
    }
    public void setValue3(String value3) {
        this.value3 = value3;
    }
    
    private String value4 = "4";
    public String getValue4() {
        return value4;
    }
    public void setValue4(String value4) {
        this.value4 = value4;
    }
    
    private String value5 = "5";
    public String getValue5() {
        return value5;
    }
    public void setValue5(String value5) {
        this.value5 = value5;
    }
    
    @Override
    public String toString() {
        return "value=" + this.getValue() + super.toString();
    }
}
