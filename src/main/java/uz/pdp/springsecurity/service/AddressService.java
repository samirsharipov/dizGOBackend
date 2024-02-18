package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Address;
import uz.pdp.springsecurity.payload.AddressDto;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.repository.AddressRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    public static final String NOT_FOUND = "ADDRESS NOT FOUND";

    public ApiResponse addAddress(AddressDto addressDto) {
        Address address = new Address();
        address.setCity(addressDto.getCity());
        address.setDistrict(addressDto.getDistrict());
        address.setStreet(addressDto.getStreet());
        address.setHome(addressDto.getHome());

        addressRepository.save(address);
        return new ApiResponse("ADDED", true);
    }

    public ApiResponse editAddress(UUID id, AddressDto addressDto) {
        if (!addressRepository.existsById(id)) return new ApiResponse(NOT_FOUND, false);
        Address address = addressRepository.getById(id);
        address.setCity(addressDto.getCity());
        address.setDistrict(addressDto.getDistrict());
        address.setStreet(addressDto.getStreet());
        address.setHome(addressDto.getHome());

        addressRepository.save(address);
        return new ApiResponse("EDITED", true);
    }

    public ApiResponse getAddress(UUID id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        return optionalAddress.map(address -> new ApiResponse("FOUND", true, address)).orElseGet(() -> new ApiResponse(NOT_FOUND, false));
    }

    public ApiResponse getAddresses() {
        return new ApiResponse("FOUND", true, addressRepository.findAll());
    }

    public ApiResponse deleteAddress(UUID id) {
        if (!addressRepository.existsById(id)) return new ApiResponse(NOT_FOUND, false);
        addressRepository.deleteById(id);
        return new ApiResponse("DELETED", true);
    }
}
