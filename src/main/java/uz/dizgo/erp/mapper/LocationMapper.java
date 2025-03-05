package uz.dizgo.erp.mapper;

import org.springframework.stereotype.Component;
import uz.dizgo.erp.entity.Location;
import uz.dizgo.erp.payload.LocationDTO;

@Component
public class LocationMapper {

    public LocationDTO toLocationDTO(Location location) {
        return new LocationDTO(
                location.getId(),
                location.getBranchId(),
                location.getLatitude(),
                location.getLongitude(),
                location.getRadius()
        );
    }

    public Location toLocation(LocationDTO locationDTO) {
        Location location = new Location();
        location.setId(locationDTO.getId());
        location.setBranchId(locationDTO.getBranchId());
        location.setLatitude(locationDTO.getLatitude());
        location.setLongitude(locationDTO.getLongitude());
        location.setRadius(locationDTO.getRadius());
        return location;
    }

    public void updateLocation(LocationDTO locationDTO, Location location) {
        location.setBranchId(locationDTO.getBranchId());
        location.setLatitude(locationDTO.getLatitude());
        location.setLongitude(locationDTO.getLongitude());
        location.setRadius(locationDTO.getRadius());
    }
}