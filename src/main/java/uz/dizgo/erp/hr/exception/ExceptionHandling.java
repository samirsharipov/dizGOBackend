package uz.dizgo.erp.hr.exception;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.dizgo.erp.hr.payload.Result;

@ControllerAdvice
public class ExceptionHandling {
    @ExceptionHandler(HRException.class)
    public HttpEntity<Result> benefitGram(HRException e) {
        System.err.println(e.getMessage());
        return ResponseEntity.ok(new Result(false, e.getMessage(), null));
    }
}
