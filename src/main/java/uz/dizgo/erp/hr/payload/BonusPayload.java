package uz.dizgo.erp.hr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class BonusPayload {

    private String name;

    private String color;

    private String icon;

    private double summa = 0;

    private boolean active = true;

    private boolean delete = false;
}
