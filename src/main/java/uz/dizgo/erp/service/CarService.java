package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Branch;
import uz.dizgo.erp.entity.Car;
import uz.dizgo.erp.entity.CarInvoice;
import uz.dizgo.erp.enums.CarInvoiceType;
import uz.dizgo.erp.mapper.CarMapper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.CarDto;
import uz.dizgo.erp.payload.CarInvoiceDto;
import uz.dizgo.erp.payload.CarInvoiceList;
import uz.dizgo.erp.payload.projections.CarListResult;
import uz.dizgo.erp.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository repository;
    private final CarMapper mapper;
    private final BusinessRepository businessRepository;
    private final AttachmentRepository attachmentRepository;
    private final CarInvoiceRepository carInvoiceRepository;
    private final BranchRepository branchRepository;
    private final PayMethodRepository payMethodRepository;

    public ApiResponse add(CarDto carDto) {
//        repository.save(mapper.toEntity(carDto));
        try {
            Car car;
            if (!carDto.getFile().isEmpty()) {
                car = new Car(
                        carDto.getDriver(),
                        carDto.getModel(),
                        carDto.getColor(),
                        carDto.getCarNumber(),
                        true,
                        businessRepository.findById(carDto.getBusinessId()).orElseThrow(),
                        carDto.getPrice(),
                        attachmentRepository.findById(UUID.fromString(carDto.getFile())).orElseThrow()
                );
            } else {
                car = new Car(
                        carDto.getDriver(),
                        carDto.getModel(),
                        carDto.getColor(),
                        carDto.getCarNumber(),
                        true,
                        businessRepository.findById(carDto.getBusinessId()).orElseThrow(),
                        carDto.getPrice(),
                        null);
            }
            repository.save(car);
            return new ApiResponse("saved", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("error", false);
        }
    }

    public ApiResponse edit(UUID id, CarDto carDto) {
        Optional<Car> optionalCar = repository.findById(id);
        if (optionalCar.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        Car car = optionalCar.get();
        mapper.update(carDto, car);
        repository.save(car);
        return new ApiResponse("successfully edited", true);
    }

    public ApiResponse get(UUID businessId) {
        List<CarListResult> carDtoList = repository.findAllByBusinessId(businessId).stream()
                .map(car -> {
                    double amount = car.getPrice();
                    for (CarInvoice invoice : carInvoiceRepository.findAllByCarId(car.getId())) {
                        if (invoice.getType().equals(CarInvoiceType.INCOME)) {
                            amount = amount - invoice.getAmount();
                        } else if (invoice.getType().equals(CarInvoiceType.EXPENSIVE)) {
                            amount = amount + invoice.getAmount();
                        }
                    }
                    System.err.println(amount);
                    String fileId = (car.getFile() != null) ? car.getFile().getId().toString() : null;
                    return new CarListResult(
                            car.getId(),
                            car.getDriver(),
                            car.getModel(),
                            car.getColor(),
                            car.getCarNumber(),
                            car.getPrice(),
                            fileId,
                            amount
                    );
                })
                .collect(Collectors.toList());

        return new ApiResponse("all", true, carDtoList);
    }

    public ApiResponse getById(UUID id) {
        Optional<Car> optionalCar = repository.findById(id);
        if (optionalCar.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        Car car = optionalCar.get();
        return new ApiResponse("found", true, mapper.toDto(car));
    }

    public ApiResponse addCarInvoice(CarInvoiceDto carInvoiceDto) {
        Car car = repository.findById(carInvoiceDto.getId()).orElseThrow();
        carInvoiceRepository.save(new CarInvoice(
                carInvoiceDto.getType(),
                carInvoiceDto.getAmount(),
                car,
                branchRepository.findById(carInvoiceDto.getBranch()).orElseThrow(),
                carInvoiceDto.getDescription(),
                payMethodRepository.findById(carInvoiceDto.getPay()).orElseThrow(),
                carInvoiceDto.getMileage()
        ));
        return new ApiResponse("Saved", true);
    }

    public ApiResponse getInvoiceInfo(UUID id, Integer page, Integer limit) {
        Car car = repository.findById(id).orElseThrow();
        List<CarInvoiceList> invoiceLists = new LinkedList<>();
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<CarInvoice> invoices = carInvoiceRepository.findAllByCarIdOrderByCreatedAtDesc(car.getId(), pageable);
        for (CarInvoice carInvoice : invoices.getContent()) {
            invoiceLists.add(new CarInvoiceList(
                    carInvoice.getId(),
                    carInvoice.getType(),
                    carInvoice.getCreatedAt(),
                    carInvoice.getAmount(),
                    carInvoice.getDescription(),
                    carInvoice.getPaymentMethod().getType(),
                    carInvoice.getBranch().getName(),
                    carInvoice.getMileage()
            ));
        }
        Map<String, Object> carDetails = new HashMap<>();
        carDetails.put("id", car.getId());
        carDetails.put("driver", car.getDriver());
        carDetails.put("number", car.getCarNumber());
        if (car.getFile() == null) {
            carDetails.put("file", null);
        } else {
            carDetails.put("file", car.getFile().getId());
        }
        carDetails.put("model", car.getModel());
        Map<String, Object> data = new HashMap<>();
        data.put("car", carDetails);
        data.put("list", invoiceLists);
        data.put("pages", invoices.getTotalPages());
        return new ApiResponse("invoice info", true, data);
    }

    public ApiResponse getInvoiceListByBranch(UUID id, Integer page, Integer limit, Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            Branch branch = branchRepository.findById(id).orElseThrow();
            Page<CarInvoice> carInvoicePage = carInvoiceRepository.findAllByBranchIdAndTypeEqualsIgnoreCaseOrderByCreatedAtDesc(branch.getId(), CarInvoiceType.EXPENSIVE.name(), PageRequest.of(page - 1, limit));
            List<CarInvoiceList> carInvoiceLists = new LinkedList<>();
            for (CarInvoice carInvoice : carInvoicePage.getContent()) {
                carInvoiceLists.add(new CarInvoiceList(
                        carInvoice.getId(),
                        carInvoice.getType(),
                        carInvoice.getCreatedAt(),
                        carInvoice.getAmount(),
                        carInvoice.getDescription(),
                        carInvoice.getPaymentMethod().getType(),
                        carInvoice.getBranch().getName(),
                        carInvoice.getMileage()
                ));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("list", carInvoiceLists);
            data.put("totalSum", carInvoiceRepository.getTotalAmountByBranchIdAndTypeIgnoreCase(branch.getId(), CarInvoiceType.EXPENSIVE.name()));
            data.put("totalPages", carInvoicePage.getTotalPages());
            return new ApiResponse("TRANSPORT HARAJATLARI", true, data);
        } else {
            Branch branch = branchRepository.findById(id).orElseThrow();
            Page<CarInvoice> carInvoicePage = carInvoiceRepository.findAllByBranchIdAndTypeEqualsIgnoreCaseAndCreatedAtBetween(branch.getId(), CarInvoiceType.EXPENSIVE.name(), startDate, endDate, PageRequest.of(page - 1, limit));
            List<CarInvoiceList> carInvoiceLists = new LinkedList<>();
            for (CarInvoice carInvoice : carInvoicePage.getContent()) {
                carInvoiceLists.add(new CarInvoiceList(
                        carInvoice.getId(),
                        carInvoice.getType(),
                        carInvoice.getCreatedAt(),
                        carInvoice.getAmount(),
                        carInvoice.getDescription(),
                        carInvoice.getPaymentMethod().getType(),
                        carInvoice.getBranch().getName(),
                        carInvoice.getMileage()
                ));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("list", carInvoiceLists);
            data.put("totalSum", carInvoiceRepository.getTotalAmountByBranchIdAndTypeIgnoreCaseAndCreatedAtBetween(branch.getId(), CarInvoiceType.EXPENSIVE.name(), startDate, endDate));
            data.put("totalPages", carInvoicePage.getTotalPages());
            return new ApiResponse("TRANSPORT HARAJATLARI", true, data);
        }
    }
}
