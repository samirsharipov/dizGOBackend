package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    private String message;
    private boolean success;
    private Object object;

    // Constructor with message and success
    public ApiResponse(String message, boolean success) {
        this.message = message.equals("Success") ? "AMALGA OSHIRILDI" : message;
        this.success = success;
    }

    public ApiResponse(String message) {
        this.message = message;
    }

    // Constructor with success
    public ApiResponse(boolean success) {
        this.success = success;
    }

    // Constructor with success and object
    public ApiResponse(boolean success, Object object) {
        this.success = success;
        this.object = object;
    }

    // Constructor with object
    public ApiResponse(Object object) {
        this.object = object;
    }
}
