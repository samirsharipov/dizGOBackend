package uz.pdp.springsecurity.exeptions;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@NoArgsConstructor
@AllArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RescuersNotFoundEx extends RuntimeException{

    private String resourceName; // role

    private String resourceField;// name

    private Object object; // admin, user ....


}
