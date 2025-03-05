package uz.dizgo.erp.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class ProjectStatusDto {

    private UUID uuid;

    private String name;

    private UUID branchId;
}
