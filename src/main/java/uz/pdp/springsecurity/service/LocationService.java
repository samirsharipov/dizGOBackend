package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Location;
import uz.pdp.springsecurity.mapper.LocationMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.LocationDTO;
import uz.pdp.springsecurity.repository.LocationRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    // CREATE
    public ApiResponse create(LocationDTO locationDTO) {
        Location location = locationMapper.toLocation(locationDTO);
        locationRepository.save(location);
        return new ApiResponse("Location muvaffaqiyatli qo'shildi", true);
    }

    // READ (Single location by ID)
    public ApiResponse getById(UUID id) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isEmpty()) {
            return new ApiResponse("Location topilmadi", false);
        }
        LocationDTO locationDTO = locationMapper.toLocationDTO(location.get());
        return new ApiResponse("Location muvaffaqiyatli topildi", true, locationDTO);
    }

    public ApiResponse getByBranchId(UUID branchId) {
        Optional<Location> optionalLocation = locationRepository.findByBranchId(branchId);
        return optionalLocation
                .map(location -> new ApiResponse("Location topilmadi", true, locationMapper.toLocationDTO(location)))
                .orElse(new ApiResponse("Location muvaffaqiyatli topildi", false));
    }

    // UPDATE
    public ApiResponse update(UUID id, LocationDTO locationDTO) {
        Optional<Location> existingLocation = locationRepository.findById(id);
        if (existingLocation.isEmpty()) {
            return new ApiResponse("Location topilmadi", false);
        }
        Location location = locationMapper.toLocation(locationDTO);
        locationRepository.save(location);
        return new ApiResponse("Location muvaffaqiyatli yangilandi", true);
    }

    // DELETE
    public ApiResponse delete(UUID id) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isEmpty()) {
            return new ApiResponse("Location topilmadi", false);
        }
        locationRepository.deleteById(id);
        return new ApiResponse("Location muvaffaqiyatli o'chirildi", true);
    }
}