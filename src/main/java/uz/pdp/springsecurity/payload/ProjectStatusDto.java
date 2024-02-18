package uz.pdp.springsecurity.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class ProjectStatusDto {

    private UUID uuid;

    private String name;

    private UUID branchId;
}
