package uz.dizgo.erp.hr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    private boolean ok;
    private String message;
    private Object data;

    public Result(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }
}
