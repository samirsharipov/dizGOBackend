package uz.dizgo.erp.hr.service.warehouse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.*;
import uz.dizgo.erp.hr.payload.*;
import uz.dizgo.erp.repository.*;
import uz.dizgo.erp.hr.exception.HRException;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final BuildingRepository buildingRepository;
    private final BranchRepository branchRepository;
    private final FloorRepository floorRepository;
    private final SectorRepository sectorRepository;
    private final RastaRepository rastaRepository;
    private final ProductRepository productRepository;

    @Override
    public HttpEntity<Result> addBuilding(WarehouseDto buildingDto) {
        Branch branch = branchRepository.findById(buildingDto.getId()).orElseThrow(() -> new HRException("Filial topilmadi!"));
        buildingRepository.save(WarehouseBuilding.builder()
                .name(buildingDto.getName())
                .branch(branch)
                .active(true)
                .build());
        return ResponseEntity.ok(new Result(true, "Bino saqlandi"));
    }

    @Override
    public HttpEntity<Result> getBuildingByBranchId(UUID branchId) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new HRException("Filial topilmadi!"));
        List<WarehouseResult> results = new LinkedList<>();
        for (WarehouseBuilding warehouseBuilding : buildingRepository.findAllByBranch_IdAndActiveTrue(branch.getId())) {
            results.add(new WarehouseResult(
                    warehouseBuilding.getId(),
                    warehouseBuilding.getName()
            ));
        }
        return ResponseEntity.ok(new Result(true, "Binolar ro'yxati", results));
    }

    @Override
    public HttpEntity<Result> addFloor(WarehouseDto warehouseDto) {
        WarehouseBuilding building = buildingRepository.findById(warehouseDto.getId()).orElseThrow(() -> new HRException("Bino topilmadi!"));
        floorRepository.save(WarehouseFloor.builder()
                .active(true)
                .name(warehouseDto.getName())
                .building(building)
                .build());
        return ResponseEntity.ok(new Result(true, "Etaj saqlandi"));
    }

    @Override
    public HttpEntity<Result> getFloorByBuildingId(UUID buildingId) {
        WarehouseBuilding building = buildingRepository.findById(buildingId).orElseThrow(() -> new HRException("Bino topilmadi!"));
        List<WarehouseResult> results = new LinkedList<>();
        for (WarehouseFloor warehouseFloor : floorRepository.findAllByBuildingIdAndActiveTrue(building.getId())) {
            results.add(new WarehouseResult(
                    warehouseFloor.getId(),
                    warehouseFloor.getName()
            ));
        }
        return ResponseEntity.ok(new Result(true, "Etajlar ro'yxati", results));
    }

    @Override
    public HttpEntity<Result> addSector(WarehouseDto warehouseDto) {
        WarehouseFloor floor = floorRepository.findById(warehouseDto.getId()).orElseThrow(() -> new HRException("Etaj topilmadi!"));
        sectorRepository.save(WarehouseSector.builder()
                .active(true)
                .floor(floor)
                .name(warehouseDto.getName())
                .build());
        return ResponseEntity.ok(new Result(true, "Sektor saqlandi"));
    }

    @Override
    public HttpEntity<Result> getSectorByFloorId(UUID floorId) {
        WarehouseFloor floor = floorRepository.findById(floorId).orElseThrow(() -> new HRException("Etaj topilmadi!"));
        List<WarehouseResult> results = new LinkedList<>();
        for (WarehouseSector warehouseSector : sectorRepository.findAllByFloorIdAndActiveTrue(floor.getId())) {
            results.add(new WarehouseResult(warehouseSector.getId(), warehouseSector.getName()));
        }
        return ResponseEntity.ok(new Result(true, "Sektorlar ro'yxati", results));
    }

    @Override
    public HttpEntity<Result> addRasta(WarehouseDto warehouseDto) {
        WarehouseSector sector = sectorRepository.findById(warehouseDto.getId()).orElseThrow(() -> new HRException("Sektor topilmadi!"));
        rastaRepository.save(WarehouseRasta.builder()
                .sector(sector).active(true).name(warehouseDto.getName()).build());
        return ResponseEntity.ok(new Result(true, "Rasta saqlandi"));
    }

    @Override
    public HttpEntity<Result> getRastaBySectorId(UUID sectorId) {
        WarehouseSector sector = sectorRepository.findById(sectorId).orElseThrow(() -> new HRException("Sektor topilmadi!"));
        List<WarehouseResult> results = new LinkedList<>();
        for (WarehouseRasta warehouseRasta : rastaRepository.findAllBySectorIdAndActiveTrue(sector.getId())) {
            results.add(new WarehouseResult(
                    warehouseRasta.getId(),
                    warehouseRasta.getName()
            ));
        }
        return ResponseEntity.ok(new Result(true, "Rastalar ro'yxati", results));
    }

    @Override
    public HttpEntity<Result> getWarehouseStructure(UUID branchId) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new HRException("Filial topilmadi!"));
        OrgResult orgResult = buildOrgResult(branch);
        return ResponseEntity.ok(new Result(true, "Omborxona xaritasi", orgResult));
    }

    @Override
    public HttpEntity<Result> rastaConn(RastaConnDto[] rastaConnDto) {
        for (RastaConnDto connDto : rastaConnDto) {
            WarehouseRasta rasta = rastaRepository.findById(connDto.getRasta()).orElseThrow(() -> new HRException("Rasta topilmadi!"));

            Product product = productRepository.findById(connDto.getProduct().getValue()).orElseThrow(() -> new HRException("Mahsulot topilmadi!"));

            if (product.getRastaList().isEmpty()) {
                product.getRastaList().add(rasta);
                try {
                    productRepository.save(product);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                for (WarehouseRasta productRasta : product.getRastaList()) {
                    if (!productRasta.getId().equals(rasta.getId())) {
                        product.getRastaList().add(rasta);
                        try {
                            productRepository.save(product);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }

        return ResponseEntity.ok(new Result(true, "Rasta biriktirildi"));
    }

    private OrgResult buildOrgResult(Branch branch) {
        OrgResult orgResult = new OrgResult();
        orgResult.setTradingName(branch.getName());

        List<OrgResult> buildingsResult = new LinkedList<>();
        for (WarehouseBuilding building : buildingRepository.findAllByBranch_IdAndActiveTrue(branch.getId())) {
            OrgResult buildingResult = new OrgResult(building.getName());
            buildingsResult.add(buildingResult);

            List<OrgResult> floorsResult = new LinkedList<>();
            for (WarehouseFloor warehouseFloor : floorRepository.findAllByBuildingIdAndActiveTrue(building.getId())) {
                OrgResult floorResult = new OrgResult(warehouseFloor.getName());
                floorsResult.add(floorResult);

                List<OrgResult> sectorsResult = new LinkedList<>();
                for (WarehouseSector warehouseSector : sectorRepository.findAllByFloorIdAndActiveTrue(warehouseFloor.getId())) {
                    OrgResult sectorResult = new OrgResult(warehouseSector.getName());
                    sectorsResult.add(sectorResult);

                    List<OrgResult> rastaResult = new LinkedList<>();
                    for (WarehouseRasta warehouseRasta : rastaRepository.findAllBySectorIdAndActiveTrue(warehouseSector.getId())) {
                        rastaResult.add(new OrgResult(warehouseRasta.getName()));
                    }

                    sectorResult.setOrganizationChildRelationship(rastaResult);
                }

                floorResult.setOrganizationChildRelationship(sectorsResult);
            }

            buildingResult.setOrganizationChildRelationship(floorsResult);
        }

        orgResult.setOrganizationChildRelationship(buildingsResult);
        return orgResult;
    }
}
