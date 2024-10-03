package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.mapper.UserMapper;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.util.Constants;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BranchRepository branchRepository;
    private final BusinessRepository businessRepository;
    private final AttachmentRepository attachmentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final RoleRepository roleRepository;
    private final AgreementService agreementService;
    private final UserMapper userMapper;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TradeRepository tradeRepository;
    private final JobRepository jobRepository;


    public ApiResponse add(UserDto userDto, boolean isNewUser) {
        UUID businessId = userDto.getBusinessId();
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("NOT FOUND BUSINESS", false);
        }
        Business business = optionalBusiness.get();

        List<User> allUser = userRepository.findAllByBusiness_Id(businessId);
        int size = allUser.size();

        Optional<Role> optionalRole = roleRepository.findById(userDto.getRoleId());
        if (optionalRole.isEmpty()) {
            return new ApiResponse("not found role", false);
        }

        if (!isNewUser) {
            Optional<Subscription> optionalSubscription = subscriptionRepository.findByBusinessIdAndActiveTrue(business.getId());
            if (optionalSubscription.isEmpty()) {
                return new ApiResponse("tariff aktiv emas", false);
            }
            Subscription subscription = optionalSubscription.get();
            if (subscription.getTariff().getEmployeeAmount() >= size || subscription.getTariff().getEmployeeAmount() == 0) {

            } else {
                return new ApiResponse("You have opened a sufficient branch according to the employee", false);
            }

            Role role = optionalRole.get();
            if (role.getName().equals(Constants.ADMIN)) {
                return new ApiResponse("Admin rolelik hodim qo'shaolmaysiz!", false);
            }
        }

        boolean b = userRepository.existsByUsernameIgnoreCase(userDto.getUsername());
        if (b) return new ApiResponse("USER ALREADY EXISTS", false);


        HashSet<Branch> branches = new HashSet<>();
        for (UUID branchId : userDto.getBranchId()) {
            Optional<Branch> optionalBranch = branchRepository.findById(branchId);
            if (optionalBranch.isPresent()) {
                branches.add(optionalBranch.get());
            } else {
                return new ApiResponse("BRANCH NOT FOUND", false);
            }
        }

        User user = userMapper.toEntity(userDto);
        if (userDto.getJobId() != null) {
            Optional<Job> optionalJob = jobRepository.findById(userDto.getJobId());
            optionalJob.ifPresent(user::setJob);
        }
        user.setActive(true);
        user.setBranches(branches);
        user.setBirthday(userDto.getBirthday());
        user.setPassportNumber(userDto.getPassportNumber() != null ? userDto.getPassportNumber() : "");
        user.setDateOfEmployment(userDto.getDateOfEmployment() != null ? userDto.getDateOfEmployment() : new Date());
        if (userDto.getPhotoId() != null) {
            user.setPhoto(attachmentRepository.findById(userDto.getPhotoId()).orElseThrow());
        }
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(optionalRole.get());
        userRepository.save(user);
        agreementService.add(user);
        return new ApiResponse("ADDED", true, user.getId());
    }

    public ApiResponse edit(UUID id, UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) return new ApiResponse("USER NOT FOUND", false);

        if (!optionalUser.get().getUsername().equals(userDto.getUsername())) {
            boolean b = userRepository.existsByUsernameIgnoreCase(userDto.getUsername());
            if (b) return new ApiResponse("USERNAME ALREADY EXISTS", false);
        }


        Optional<Role> optionalRole = roleRepository.findById(userDto.getRoleId());

        User user = optionalUser.get();
        userMapper.update(userDto, user);
        assert userDto.getPassword() != null;
        if (!userDto.getPassword().isEmpty()) {
            if (userDto.getPassword().length() > 2) {
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }
        }

        optionalRole.ifPresent(user::setRole);

        if (userDto.getJobId() != null) {
            Optional<Job> optionalJob = jobRepository.findById(userDto.getJobId());
            if (optionalJob.isPresent()) {
                optionalJob.ifPresent(user::setJob);
            }
        }

        Set<Branch> branches = new HashSet<>();
        for (UUID branchId : userDto.getBranchId()) {
            Optional<Branch> byId = branchRepository.findById(branchId);
            if (byId.isPresent()) {
                branches.add(byId.get());
            } else {
                return new ApiResponse("BRANCH NOT FOUND", false);
            }
        }
        List<Branch> sortedList = new ArrayList<>(branches);
        sortedList.sort(Comparator.comparing(Branch::getCreatedAt));
        user.setBranches(branches);

        if (businessRepository.findById(userDto.getBusinessId()).isEmpty()) {
            return new ApiResponse("BUSINESS NOT FOUND", false);
        }

        UUID photoId = userDto.getPhotoId();
        if (photoId != null) {
            Optional<Attachment> optionalPhoto = attachmentRepository.findById(photoId);
            if (optionalPhoto.isEmpty()) return new ApiResponse("PHOTO NOT FOUND", false);
            user.setPhoto(optionalPhoto.get());
        }

        if (userDto.getDateOfEmployment() != null) {
            user.setDateOfEmployment(userDto.getDateOfEmployment());
        }
        user.setPassportNumber(userDto.getPassportNumber());

        userRepository.save(user);
        return new ApiResponse("EDITED", true);
    }

    public ApiResponse get(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }

        User user = optionalUser.get();
        UserDto dto = userMapper.toDto(user);
        if (user.getPhoto() != null) {
            dto.setPhotoId(user.getPhoto().getId());
        }
        Set<BranchGetDto> branchGetDtos = new HashSet<>();
        for (Branch branch : user.getBranches()) {
            BranchGetDto branchGetDto = new BranchGetDto();
            branchGetDto.setId(branch.getId());
            branchGetDto.setName(branch.getName());
            branchGetDtos.add(branchGetDto);
        }
        dto.setBranches(branchGetDtos);

        return new ApiResponse("FOUND", true, dto);
    }

    public ApiResponse delete(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            return new ApiResponse("USER NOT FOUND", false);

        User user = optionalUser.get();
        if (user.getRole().getName().equals(Constants.ADMIN) || user.getRole().getName().equals(Constants.SUPERADMIN))
            return new ApiResponse("ADMINNI O'CHIRIB BO'LMAYDI", false);

        user.setActive(false);
        user.setEnabled(false);
        userRepository.save(user);
        return new ApiResponse("DELETED", true);
    }

    public ApiResponse editMyProfile(User user, ProfileDto profileDto) {
        UUID id = user.getId();
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            return new ApiResponse("NOT FOUND USER");

        if (!optionalUser.get().getUsername().equals(profileDto.getUsername())) {
            Optional<User> optional = userRepository.findByUsername(profileDto.getUsername());
            if (optional.isPresent()) {
                if (!optional.get().getId().equals(optionalUser.get().getId())) {
                    return new ApiResponse("USERNAME ALREADY EXISTS", false);
                }
            }
        }

        if (!profileDto.getPassword().equals(profileDto.getPrePassword()))
            return new ApiResponse("PASSWORDS ARE NOT COMPATIBLE", false);


        user.setFirstName(profileDto.getFirstName());
        user.setLastName(profileDto.getLastName());
        user.setUsername(profileDto.getUsername());
        user.setEmail(profileDto.getEmail());

        if (profileDto.getPassword() != null && !profileDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(profileDto.getPassword()));
        }

        if (profileDto.getPhotoId() != null) {
            Optional<Attachment> optionalPhoto = attachmentRepository.findById(profileDto.getPhotoId());
            if (optionalPhoto.isEmpty()) return new ApiResponse("PHOTO NOT FOUND", false);
            user.setPhoto(optionalPhoto.get());
        }


        userRepository.save(user);
        return new ApiResponse("UPDATED", true);
    }

    public ApiResponse getByRole(UUID role_id) {
        List<User> allByRoleId = userRepository.findAllByRole_Id(role_id);
        if (allByRoleId.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }
        return new ApiResponse("FOUND", true, userMapper.toDto(allByRoleId));
    }

    public ApiResponse getAllByBusinessId(UUID business_id) {
        Optional<Role> optionalRole = roleRepository.findByName(Constants.SUPERADMIN);
        if (optionalRole.isEmpty()) return new ApiResponse("ERROR", false);
        Role superAdmin = optionalRole.get();
        List<User> allByBusiness_id = userRepository.findAllByBusiness_IdAndRoleIsNotAndActiveIsTrue(business_id, superAdmin);
        if (allByBusiness_id.isEmpty()) return new ApiResponse("BUSINESS NOT FOUND", false);
        List<UserDto> dtoList = new ArrayList<>();
        for (User user : allByBusiness_id) {
            UserDto userDto = userMapper.toDto(user);
            if (user.getPhoto() != null) {
                userDto.setPhotoId(user.getPhoto().getId());
            }
            dtoList.add(userDto);
        }
        return new ApiResponse("FOUND", true, dtoList);
    }

    public ApiResponse getAllByBranchId(UUID branch_id) {
        Optional<Branch> optionalBranch = branchRepository.findById(branch_id);
        if (optionalBranch.isPresent()) {
            Optional<Role> optionalRole = roleRepository.findByName(Constants.SUPERADMIN);
            if (optionalRole.isEmpty()) return new ApiResponse("ERROR", false);
            Role superAdmin = optionalRole.get();
            List<User> allByBranch_id = userRepository.findAllByBranchesIdAndRoleIsNotAndActiveIsTrue(branch_id, superAdmin);
            return new ApiResponse("FOUND", true, userMapper.toDto(allByBranch_id));
        }
        return new ApiResponse("NOT FOUND", false);
    }


    public ApiResponse getByPatron(UUID userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ApiResponse("not found User", false);
        }
        UserDtoForPatron userDtoForPatron = new UserDtoForPatron();
        userDtoForPatron.setFio(user.getFirstName() + " " + user.getLastName());
        if (user.getPhoto() != null) {
            userDtoForPatron.setPhotosId(user.getPhoto().getId());
        }
        userDtoForPatron.setRole(user.getRole().getName());


        int total = projectRepository.countAllByUsersId(userId);
        int completed1 = projectRepository.countAllByProjectStatus_NameAndUsersId("Completed", userId);
        int expired = projectRepository.countAllByExpiredTrue();
        int process = projectRepository.countAllByProjectStatus_NameAndUsersId("Process", userId);
        ProjectInfoDto projectInfoDto = new ProjectInfoDto();
        projectInfoDto.setTotal(total);
        projectInfoDto.setCompleted(completed1);
        projectInfoDto.setProcess(process);
        projectInfoDto.setExpired(expired);
        userDtoForPatron.setProjectInfoDto(projectInfoDto);

        int taskAmount = 0;

        List<Task> taskList = taskRepository.findTasksByUserId(userId);
        Set<Task> taskSet = new HashSet<>(taskList);
        taskAmount = taskSet.size();

        int completed = taskRepository.countTasksByTaskStatusOriginalNameAndUserId("Completed", userId);
        int expiredIsTrue = taskRepository.countExpiredTasksByUserId(userId);
        TaskInfoGetDto taskInfoGetDto = new TaskInfoGetDto();
        taskInfoGetDto.setTaskAmount(taskAmount);
        taskInfoGetDto.setDoneTaskAmount(completed);
        taskInfoGetDto.setNotDoneDeadlineAmount(expiredIsTrue);

        userDtoForPatron.setTaskInfoGetDto(taskInfoGetDto);

        List<Bonus> bonusList = user.getBonuses();
        List<BonusGetMetDto> bonusGetMetDto = new ArrayList<>();
        for (Bonus bonus : bonusList) {
            BonusGetMetDto bonusDto = new BonusGetMetDto();
            bonusDto.setName(bonus.getName());
            bonusDto.setIcon(bonus.getIcon());
            bonusGetMetDto.add(bonusDto);
        }
        userDtoForPatron.setBonusGetMetDtoList(bonusGetMetDto);

        List<Trade> allTrade = tradeRepository.findAllByTrader_Id(userId);

        if (!allTrade.isEmpty()) {

            double totalSumma = 0;
            for (Trade trade : allTrade) {
                totalSumma += trade.getTotalSum();
            }

            TradeResultDto tradeResultDto = new TradeResultDto();
            tradeResultDto.setTotalTrade(allTrade.size());
            tradeResultDto.setTotalTradeSumma(totalSumma);
            userDtoForPatron.setTradeResultDto(tradeResultDto);
        }

        return new ApiResponse("found", true, userDtoForPatron);
    }

    public ApiResponse getAllByName(UUID branchId, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String[] words = name.split("\\s+");
        Page<User> users = null;
        for (String word : words) {
            users = userRepository.findAllByFirstNameContainingIgnoreCaseAndBranchesIdAndUsernameNot(word, branchId, "superadmin", pageable);
        }
        if (users == null) {
            return new ApiResponse("Not found", false);
        }
        List<User> userList = users.getContent();
        List<UserDto> userDtoList = userMapper.toDto(userList);
        Page<UserDto> pages = new PageImpl<>(userDtoList, pageable, userDtoList.size());
        return new ApiResponse("Found", true, pages);
    }

    public ApiResponse search(String username) {
        List<User> all = userRepository.findAllByUsernameContainingIgnoreCase(username);
        Set<User> userSet = new HashSet<>(all);

        List<UserDto> dtoList = new ArrayList<>();
        for (User user : userSet) {
            UserDto userDto = userMapper.toDto(user);
            dtoList.add(userDto);
        }
        return new ApiResponse("found", true, dtoList);
    }


    public ApiResponse editPassword(UUID id, String password) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        if (password.isEmpty()) {
            return new ApiResponse("parol kiriting!", false);
        }

        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        return new ApiResponse("parol saqlandi!", true);
    }

    public HttpEntity<?> forGrossPriceControlFuncService(UUID userId) {
        List<UserGrossPriceControlDto> result = new ArrayList<>();
        User user = userRepository.findById(userId).get();
        List<User> allByBusinessIdAndId = userRepository.findAllByBusiness_IdAndId(user.getBusiness().getId(), userId);
        for (User user1 : allByBusinessIdAndId) {
            UserGrossPriceControlDto userGrossPriceControlDto = new UserGrossPriceControlDto();
            userGrossPriceControlDto.setUserId(user1.getId());
            userGrossPriceControlDto.setFirstName(user1.getFirstName());
            userGrossPriceControlDto.setLastName(user1.getLastName());
            userGrossPriceControlDto.setGrossPriceControlOneUser(user1.isGrossPriceControlOneUser());
            result.add(userGrossPriceControlDto);
        }
        return ResponseEntity.ok(result);
    }

    public ApiResponse forGrossPriceControlEditeOneState(UUID userId, Boolean checked) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }
        User user = optionalUser.get();
        user.setGrossPriceControlOneUser(checked);
        userRepository.save(user);
        return new ApiResponse("UPDATED", true);
    }
}
