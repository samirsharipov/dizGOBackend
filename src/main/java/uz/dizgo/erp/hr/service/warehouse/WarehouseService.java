package uz.dizgo.erp.hr.service.warehouse;

import org.springframework.http.HttpEntity;
import uz.dizgo.erp.hr.payload.RastaConnDto;
import uz.dizgo.erp.hr.payload.Result;
import uz.dizgo.erp.hr.payload.WarehouseDto;

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
