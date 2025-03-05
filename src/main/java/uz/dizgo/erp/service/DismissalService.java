package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Business;
import uz.dizgo.erp.entity.Dismissal;
import uz.dizgo.erp.entity.DismissalDescription;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.DismissalDto;
import uz.dizgo.erp.payload.DismissalGetDto;
import uz.dizgo.erp.repository.DismissalDescriptionRepository;
import uz.dizgo.erp.repository.DismissalRepository;
import uz.dizgo.erp.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DismissalService {

    private final DismissalRepository dismissalRepository;
    private final UserRepository userRepository;
    private final DismissalDescriptionRepository dismissalDescriptionRepository;

    public ApiResponse create(DismissalDto dismissalDto) {

        if (dismissalDto.getUserid() == null)
            return new ApiResponse("You must provide a user id", false);

        if (dismissalDto.getDismissalDescriptionId() == null)
            return new ApiResponse("You must provide a dismissal description", false);

        Optional<User> optionalUser = userRepository.findById(dismissalDto.getUserid());
        if (optionalUser.isEmpty())
            return new ApiResponse("User not found", false);

        Optional<DismissalDescription> optionalDismissalDescription = dismissalDescriptionRepository.findById(dismissalDto.getDismissalDescriptionId());
        if (optionalDismissalDescription.isEmpty())
            return new ApiResponse("Dismissal description not found", false);

        User user = optionalUser.get();
        user.setEnabled(false);
        user.setActive(false);
        userRepository.save(user);

        dismissalRepository.save(fromDto(dismissalDto, new Dismissal(), user, optionalDismissalDescription.get(), user.getBusiness()));

        return new ApiResponse("Dismissal created", true);
    }

    public ApiResponse getDismissalUsers(UUID businessId) {
        List<Dismissal> all = dismissalRepository.findAllByBusinessIdOrderByCreatedAtDesc(businessId);
        if (all.isEmpty())
            return new ApiResponse("No dismissals found", false);

        return new ApiResponse("Found " + all.size() + " dismissals", true, toGetDto(all));
    }

    public ApiResponse getById(UUID id) {

        Optional<Dismissal> optionalDismissal = dismissalRepository.findById(id);
        if (optionalDismissal.isEmpty())
            return new ApiResponse("Dismissal not found", false);

        Dismissal dismissal = optionalDismissal.get();

        return new ApiResponse("Dismissal get", true, toGetDto(new DismissalGetDto(), dismissal));
    }


    private static Dismissal fromDto(DismissalDto dismissalDto, Dismissal dismissal, User user, DismissalDescription dismissalDescription, Business business) {
        dismissal.setUser(user);
        dismissal.setDescription(dismissalDescription);
        dismissal.setComment(dismissalDto.getComment());
        dismissal.setMandatory(dismissalDescription.isMandatory());
        dismissal.setBusiness(business);
        return dismissal;
    }

    private static DismissalGetDto toGetDto(DismissalGetDto dismissalGetDto, Dismissal dismissal) {
        dismissalGetDto.setId(dismissal.getId());
        dismissalGetDto.setCreateAt(dismissal.getCreatedAt());
        dismissalGetDto.setDismissalDescriptionId(dismissal.getDescription().getId());
        dismissalGetDto.setUserid(dismissal.getUser().getId());
        dismissalGetDto.setFirstname(dismissal.getUser().getFirstName());
        dismissalGetDto.setLastname(dismissal.getUser().getLastName());
        dismissalGetDto.setDescription(dismissal.getDescription().getDescription());
        dismissalGetDto.setComment(dismissal.getComment());
        dismissalGetDto.setMandatory(dismissal.isMandatory());
        return dismissalGetDto;
    }

    public static List<DismissalGetDto> toGetDto(List<Dismissal> dismissals) {
        List<DismissalGetDto> dismissalGetDtoList = new ArrayList<>();
        for (Dismissal dismissal : dismissals) {
            dismissalGetDtoList.add(toGetDto(new DismissalGetDto(), dismissal));
        }
        return dismissalGetDtoList;
    }
}
