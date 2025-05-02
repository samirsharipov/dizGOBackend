package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Address;
import uz.dizgo.erp.mapper.AddressMapper;
import uz.dizgo.erp.payload.AddressDto;
import uz.dizgo.erp.payload.AddressGetDto;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.repository.AddressRepository;

import java.util.ArrayList;
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



    private Address prepareAddressFromDto(Address address, AddressDto addressDto) {
        addressMapper.updateEntityFromDto(addressDto, address);
        if (addressDto.getPatientId() != null) {
            addressRepository.findById(addressDto.getPatientId())
                    .ifPresent(address::setParentAddress);
        }
        return address;
    }

    public ApiResponse getAddressTreeByBusiness() {
        List<AddressGetDto> addressGetDtoList = buildAddressTreeByBusiness(null);
        if (addressGetDtoList.isEmpty()) {
            return new ApiResponse("No addresses found for this business", false);
        }
        return new ApiResponse("Address tree retrieved successfully", true, addressGetDtoList);
    }

    private List<AddressGetDto> buildAddressTreeByBusiness(UUID parentId) {
        List<Address> childAddresses = addressRepository.findAllByParentAddress_Id(parentId);

        List<AddressGetDto> addressTree = new ArrayList<>();
        for (Address address : childAddresses) {
            AddressGetDto dto = new AddressGetDto(
                    address.getId(),
                    address.getName(),
                    address.getParentAddress() != null ? address.getParentAddress().getId() : null,
                    address.getParentAddress() != null ? address.getParentAddress().getName() : null
            );

            dto.setChildren(buildAddressTreeByBusiness(address.getId()));
            addressTree.add(dto);
        }
        return addressTree;
    }
}