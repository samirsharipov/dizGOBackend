package uz.dizgo.erp.hr.exception;

public class HRException extends RuntimeException {
    public HRException(String message) {
        super(message);
    }

    public HRException(String message, Throwable cause) {
        super(message, cause);
    }
}
