package uz.dizgo.erp.payload.projections;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductActivityLogDto {

    private Timestamp createdAt;

    private String createdByName;

    private String actionType;

    private String oldData;

    private String newData;

    private String extraData;

}