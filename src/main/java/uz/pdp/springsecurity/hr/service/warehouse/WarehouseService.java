package uz.pdp.springsecurity.hr.service.warehouse;

import org.springframework.http.HttpEntity;
import uz.pdp.springsecurity.hr.payload.RastaConnDto;
import uz.pdp.springsecurity.hr.payload.Result;
import uz.pdp.springsecurity.hr.payload.WarehouseDto;

import java.util.UUID;

public interface WarehouseService {
    HttpEntity<Result> addBuilding(WarehouseDto buildingDto);

    HttpEntity<Result> getBuildingByBranchId(UUID branchId);

    HttpEntity<Result> addFloor(WarehouseDto warehouseDto);

    HttpEntity<Result> getFloorByBuildingId(UUID buildingId);

    HttpEntity<Result> addSector(WarehouseDto warehouseDto);

    HttpEntity<Result> getSectorByFloorId(UUID floorId);

    HttpEntity<Result> addRasta(WarehouseDto warehouseDto);

    HttpEntity<Result> getRastaBySectorId(UUID sectorId);

    HttpEntity<Result> getWarehouseStructure(UUID branchId);

    HttpEntity<Result> rastaConn(RastaConnDto[] rastaConnDto);
}
