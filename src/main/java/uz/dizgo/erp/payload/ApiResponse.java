package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers.data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;
    private boolean success;
    private Object object;

    // Constructor with message and success
    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ApiResponse(String message) {
        this.message = message;
    }

    // Constructor with success
    public ApiResponse(boolean success) {
        this.success = success;
    }

    // Constructor with object
    public ApiResponse(Object object) {
        this.object = object;
    }

    public ApiResponse(boolean success, Object object) {
        this.success = success;
        this.object = object;
    }

}
