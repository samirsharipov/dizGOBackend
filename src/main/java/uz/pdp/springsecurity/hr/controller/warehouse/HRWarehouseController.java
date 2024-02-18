package uz.pdp.springsecurity.hr.controller.warehouse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.hr.payload.RastaConnDto;
import uz.pdp.springsecurity.hr.payload.Result;
import uz.pdp.springsecurity.hr.payload.WarehouseDto;
import uz.pdp.springsecurity.hr.service.warehouse.WarehouseService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hr/warehouse")
@RequiredArgsConstructor
public class HRWarehouseController {
    private final WarehouseService service;

    @PostMapping("/building")
    public HttpEntity<Result> addBuilding(@RequestBody WarehouseDto warehouseDto) {
        return service.addBuilding(warehouseDto);
    }

    @GetMapping("/building/{branchId}")
    public HttpEntity<Result> getBuildingByBranchId(@PathVariable UUID branchId) {
        return service.getBuildingByBranchId(branchId);
    }

    @PostMapping("/floor")
    public HttpEntity<Result> addFloor(@RequestBody WarehouseDto warehouseDto) {
        return service.addFloor(warehouseDto);
    }

    @GetMapping("/floor/{buildingId}")
    public HttpEntity<Result> getFloorByBuildingId(@PathVariable UUID buildingId) {
        return service.getFloorByBuildingId(buildingId);
    }

    @PostMapping("/sector")
    public HttpEntity<Result> addSector(@RequestBody WarehouseDto warehouseDto) {
        return service.addSector(warehouseDto);
    }

    @GetMapping("/sector/{floorId}")
    public HttpEntity<Result> getSectorByFloorId(@PathVariable UUID floorId) {
        return service.getSectorByFloorId(floorId);
    }

    @PostMapping("/rasta")
    public HttpEntity<Result> addRasta(@RequestBody WarehouseDto warehouseDto) {
        return service.addRasta(warehouseDto);
    }

    @GetMapping("/rasta/{sectorId}")
    public HttpEntity<Result> getRastaBySectorId(@PathVariable UUID sectorId) {
        return service.getRastaBySectorId(sectorId);
    }

    @GetMapping("/structure")
    public HttpEntity<Result> getWarehouseStructure(@RequestParam UUID branchId) {
        return service.getWarehouseStructure(branchId);
    }
    @PostMapping("/rasta-conn")
    public HttpEntity<Result> rastaConn(@RequestBody RastaConnDto[] rastaConnDto){
       return service.rastaConn(rastaConnDto);
    }
}
