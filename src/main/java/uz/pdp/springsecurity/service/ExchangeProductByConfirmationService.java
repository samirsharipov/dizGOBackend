package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.NotificationType;
import uz.pdp.springsecurity.enums.Permissions;
import uz.pdp.springsecurity.mapper.ExchangeProductBranchMapper;
import uz.pdp.springsecurity.mapper.ExchangeProductMapper;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ExchangeProductByConfirmationService {

    private final ExchangeProductByConfirmationRepository repository;
    private final ExchangeProductBranchRepository exchangeProductBranchRepository;
    private final ExchangeProductBranchMapper mapper;
    private final ExchangeProductMapper exchangeProductMapper;
    private final CarRepository carRepository;
    private final ExchangeProductRepository exchangeProductRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final ProductRepository productRepository;
    private final ProductTypePriceRepository productTypePriceRepository;
    private final BranchRepository branchRepository;

    public ApiResponse add(ExchangeProductByConfirmationDto byConfirmationDto) {

        ExchangeProductByConfirmation confirmation = new ExchangeProductByConfirmation();

        List<ExchangeProduct> exchangeProductList = new ArrayList<>();
        for (ExchangeProductDTO exchangeProductDTO : byConfirmationDto.getExchangeProductBranchDTO().getExchangeProductDTOS()) {
            ExchangeProduct exchangeProduct = new ExchangeProduct();
            exchangeProduct.setExchangeProductQuantity(exchangeProductDTO.getExchangeProductQuantity());
            if (exchangeProductDTO.getProductExchangeId() != null) {
                Optional<Product> optionalProduct = productRepository.findById(exchangeProductDTO.getProductExchangeId());
                optionalProduct.ifPresent(exchangeProduct::setProduct);
            } else {
                Optional<ProductTypePrice> optionalProductTypePrice = productTypePriceRepository
                        .findById(exchangeProductDTO.getProductTypePriceId());
                optionalProductTypePrice.ifPresent(exchangeProduct::setProductTypePrice);
            }
            exchangeProductList.add(exchangeProduct);
            exchangeProductRepository.save(exchangeProduct);
        }

        ExchangeProductBranch entity = mapper.toEntity(byConfirmationDto.getExchangeProductBranchDTO());
        entity.setExchangeProductList(exchangeProductList);

        ExchangeProductBranch save = exchangeProductBranchRepository.save(entity);
        for (ExchangeProductDTO exchangeProductDTO : byConfirmationDto.getExchangeProductBranchDTO().getExchangeProductDTOS()) {
            ExchangeProduct exchangeProduct = new ExchangeProduct();
            exchangeProduct.setExchangeProductQuantity(exchangeProductDTO.getExchangeProductQuantity());
            if (exchangeProductDTO.getProductExchangeId() != null) {
                Optional<Product> optionalProduct = productRepository.findById(exchangeProductDTO.getProductExchangeId());
                optionalProduct.ifPresent(exchangeProduct::setProduct);
            } else {
                Optional<ProductTypePrice> optionalProductTypePrice = productTypePriceRepository
                        .findById(exchangeProductDTO.getProductTypePriceId());
                optionalProductTypePrice.ifPresent(exchangeProduct::setProductTypePrice);
            }
            exchangeProductRepository.save(exchangeProduct);
        }

        confirmation.setExchangeProductBranch(save);
        ExchangeProductByConfirmation byConfirmation = repository.save(confirmation);


        List<User> allUsers = userRepository.findAllByBranches_Id(byConfirmationDto.
                getExchangeProductBranchDTO().
                getShippedBranchId());


        Set<User> all = getUsers(allUsers, true);
        UUID receivedBranchId = byConfirmationDto.getExchangeProductBranchDTO().getReceivedBranchId();
        UUID shippedBranchId = byConfirmationDto.getExchangeProductBranchDTO().getShippedBranchId();
        Optional<Branch> optionalReceivedBranch = branchRepository.findById(receivedBranchId);

        String branchName = " ";
        if (optionalReceivedBranch.isPresent()) {
            branchName = optionalReceivedBranch.get().getName();
        }

        for (User user : all) {
            if (!byConfirmationDto.getExchangeProductBranchDTO().getUserId().equals(user.getId())) {
                Notification notification = new Notification();
                notification.setRead(false);
                notification.setName("Mahsulotlar o'tqazmasi so'ralyapdi!");
                notification.setMessage(branchName + " fillialdan maxsulotlar so'ralmoqda!");
                notification.setUserTo(user);
                notification.setType(NotificationType.NEW_EXCHANGE_PRODUCT);
                notification.setObjectId(byConfirmation.getId());
                notification.setBranchId(shippedBranchId);
                notificationRepository.save(notification);
            }
        }

        return new ApiResponse("saved", true);
    }

    public ApiResponse get(UUID id) {
        Optional<ExchangeProductByConfirmation> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        ExchangeProductByConfirmation confirmation = optional.get();
        return new ApiResponse("found", true, getConfirmationDto(confirmation));

    }

    public ApiResponse getByBusinessId(UUID businessId) {
        List<ExchangeProductByConfirmation> all = repository.findAllByExchangeProductBranch_BusinessId(businessId);
        if (all.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        return new ApiResponse("all", true, getConfirmationDto(all));
    }

    public ApiResponse edit(UUID id, ExchangeProductByConfirmationDto byConfirmationDto) {
        Optional<ExchangeProductByConfirmation> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        ExchangeProductByConfirmation confirmation = optional.get();

        mapper.update(byConfirmationDto.getExchangeProductBranchDTO(), confirmation.getExchangeProductBranch());
        exchangeProductMapper.update(byConfirmationDto.getExchangeProductBranchDTO().getExchangeProductDTOS(), confirmation.getExchangeProductBranch().getExchangeProductList());
        if (byConfirmationDto.getCarId() != null) {
            Optional<Car> optionalCar = carRepository.findById(byConfirmationDto.getCarId());
            optionalCar.ifPresent(confirmation::setCar);
        }
        confirmation.setConfirmation(byConfirmationDto.getConfirmation() != null ? byConfirmationDto.getConfirmation() : null);
        confirmation.setMessage(byConfirmationDto.getMessage() != null ? byConfirmationDto.getMessage() : "");
        exchangeProductRepository.saveAll(confirmation.getExchangeProductBranch().getExchangeProductList());
        repository.save(confirmation);

        List<User> allUsers = userRepository.findAllByBranches_Id(confirmation.
                getExchangeProductBranch().getReceivedBranch().getId());

        Set<User> all = getUsers(allUsers, true);
        for (User user : all) {
            Notification notification = new Notification();
            notification.setRead(false);
            notification.setName("Mahsulotlar o'tqazmasi tasdiqlandi!");
            notification.setMessage(confirmation.getExchangeProductBranch().getShippedBranch().getName() + " filliali so'ralgan maxsulotlarni tasdiqlandi! " +
                    confirmation.getMessage());
            notification.setUserTo(user);
            notification.setType(NotificationType.CONFIRMED);
            notification.setObjectId(confirmation.getId());
            notificationRepository.save(notification);
        }

        return new ApiResponse("Saqlandi!", true);
    }

    public ApiResponse editConfirmation(UUID id, ConfirmationDto confirmationDto) {
        Optional<ExchangeProductByConfirmation> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        ExchangeProductByConfirmation confirmation = optional.get();
        List<User> all = userRepository.findAllByBranches_Id(confirmation.getExchangeProductBranch().getShippedBranch().getId());
        Set<User> users = getUsers(all, false);

        if (confirmationDto.isConfirmation()) {
            confirmation.setConfirmation(true);
            repository.save(confirmation);

            for (User user : users) {
                if (!confirmationDto.getUserId().equals(user.getId())) {
                    Notification notification = new Notification();
                    notification.setRead(false);
                    notification.setName("Mahsulotlar o'tqazmasi saqlandi!");
                    notification.setMessage(confirmation.getExchangeProductBranch().getReceivedBranch().getName() + " fillial maxsulotlarni tasdiqladi! " +
                            confirmation.getMessage());
                    notification.setUserTo(user);
                    notification.setObjectId(id);
                    notification.setType(NotificationType.CONFIRMED);
                    notificationRepository.save(notification);
                }
                return new ApiResponse("Saqlandi!", true);
            }
        } else {
            confirmation.setConfirmation(false);
            confirmation.setMessage(confirmationDto.getMessage());
            repository.save(confirmation);

            for (User user : users) {
                if (!confirmationDto.getUserId().equals(user.getId())) {
                    Notification notification = new Notification();
                    notification.setRead(false);
                    notification.setName("Mahsulotlar o'tqazmasi bekor qilindi!");
                    notification.setMessage(confirmation.getExchangeProductBranch().getReceivedBranch().getName() + " fillial maxsulotlarni rad etdi! " +
                            confirmation.getMessage());
                    notification.setUserTo(user);
                    notification.setObjectId(id);
                    notification.setType(NotificationType.REJECTION);
                    notificationRepository.save(notification);
                }
            }
        }
        return new ApiResponse("O'tqazma bekor qilindi!", true);
    }


    @NotNull
    private static Set<User> getUsers(List<User> allUsers, boolean b) {
        Set<User> all = new HashSet<>();
        for (User allUser : allUsers) {
            List<Permissions> permissions = allUser.getRole().getPermissions();
            for (Permissions permission : permissions) {
                if (b) {
                    if (permission.name().equals(Permissions.ADD_EXCHANGE.name())) {
                        all.add(allUser);
                    }
                } else {
                    if (permission.name().equals(Permissions.EDIT_EXCHANGE.name())) {
                        all.add(allUser);
                    }
                }
            }
        }
        return all;
    }


    private ExchangeProductByConfirmationDto getConfirmationDto(ExchangeProductByConfirmation confirmation) {
        ExchangeProductByConfirmationDto confirmationDto = new ExchangeProductByConfirmationDto();
        ExchangeProductBranchDTO productBranchDTO = mapper.toDto(confirmation.getExchangeProductBranch());
        List<ExchangeProductDTO> exchangeProductMapperDtoList = exchangeProductMapper.toDtoList(confirmation.getExchangeProductBranch().getExchangeProductList());
        productBranchDTO.setExchangeProductDTOS(exchangeProductMapperDtoList);

        confirmationDto.setId(confirmation.getId());
        confirmationDto.setExchangeProductBranchDTO(productBranchDTO);
        confirmationDto.setCarId(confirmation.getCar() != null ? confirmation.getCar().getId() : null);
        confirmationDto.setMessage(confirmation.getMessage() != null ? confirmation.getMessage() : "");
        confirmationDto.setConfirmation(confirmation.getConfirmation() != null ? confirmation.getConfirmation() : null);

        confirmationDto.setReceivedBranchName(confirmation.getExchangeProductBranch().getReceivedBranch().getName() != null ?
                confirmation.getExchangeProductBranch().getReceivedBranch().getName() : "");

        confirmationDto.setShippedBranchName(confirmation.getExchangeProductBranch().getShippedBranch().getName() != null ?
                confirmation.getExchangeProductBranch().getShippedBranch().getName() : "");

        return confirmationDto;
    }

    private List<ExchangeProductByConfirmationDto> getConfirmationDto(List<ExchangeProductByConfirmation> confirmations) {
        List<ExchangeProductByConfirmationDto> confirmationDtoList = new ArrayList<>();
        for (ExchangeProductByConfirmation confirmation : confirmations) {
            confirmationDtoList.add(getConfirmationDto(confirmation));
        }
        return confirmationDtoList;
    }
}
