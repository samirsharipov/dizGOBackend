package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Address;
import uz.pdp.springsecurity.mapper.AddressMapper;
import uz.pdp.springsecurity.payload.AddressDto;
import uz.pdp.springsecurity.payload.AddressGetDto;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.repository.AddressRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private static final String NOT_FOUND = "ADDRESS NOT FOUND";

    public ApiResponse createAddress(AddressDto addressDto) {
        Address address = prepareAddressFromDto(new Address(), addressDto);
        addressRepository.save(address);
        return new ApiResponse("ADDED", true);
    }

    public ApiResponse updateAddress(UUID id, AddressDto addressDto) {
        return addressRepository.findById(id)
                .map(address -> {
                    Address updatedAddress = prepareAddressFromDto(address, addressDto);
                    addressRepository.save(updatedAddress);
                    return new ApiResponse("EDITED", true);
                })
                .orElseGet(() -> new ApiResponse(NOT_FOUND, false));
    }

    public ApiResponse getAddress(UUID id) {
        return addressRepository.findById(id)
                .map(address -> new ApiResponse("FOUND", true, addressMapper.toDto(address)))
                .orElseGet(() -> new ApiResponse(NOT_FOUND, false));
    }

    public ApiResponse getAddresses() {
        List<Address> addresses = addressRepository.findAllByParentAddressIsNull();
        List<AddressGetDto> addressGetDtoList = addressMapper.toDtoList(addresses);
        return new ApiResponse("FOUND", true, addressGetDtoList);
    }

    public ApiResponse deleteAddress(UUID id) {
        return addressRepository.findById(id)
                .map(address -> {
                    address.setActive(false);
                    addressRepository.save(address);
                    return new ApiResponse("DELETED", true);
                })
                .orElseGet(() -> new ApiResponse(NOT_FOUND, false));
    }

    public ApiResponse getByParentId(UUID parentId) {
        List<Address> addresses = addressRepository.findAllByParentAddress_Id(parentId);
        List<AddressGetDto> addressGetDtoList = addressMapper.toDtoList(addresses);
        return new ApiResponse("FOUND", true, addressGetDtoList);
    }


    /**
     * Helper method to prepare Address entity from DTO.
     */
    private Address prepareAddressFromDto(Address address, AddressDto addressDto) {
        addressMapper.updateEntityFromDto(addressDto, address);
        if (addressDto.getPatientId() != null) {
            addressRepository.findById(addressDto.getPatientId())
                    .ifPresent(address::setParentAddress);
        }
        return address;
    }
}