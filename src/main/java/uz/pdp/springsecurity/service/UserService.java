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
import uz.pdp.springsecurity.utils.Constants;

import java.util.*;
import java.util.stream.Collectors;

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
    private final VerificationCodeService verificationService;


    public ApiResponse add(UserDTO userDto, boolean isNewUser) {
        // Umumiy validatsiya: Business, Role va Username tekshiriladi
        ApiResponse validationResponse = validateUser(userDto);
        if (!validationResponse.isSuccess()) {
            return validationResponse;
        }

        Business business = businessRepository.findById(userDto.getBusinessId()).orElse(null);
        if (business == null) {
            return new ApiResponse("Business does not exist");
        }
        Role role = roleRepository.findById(userDto.getRoleId()).orElse(null);
        if (role == null) {
            return new ApiResponse("Role does not exist");
        }

        // Agar yangi foydalanuvchi bo'lmasa, obuna va rolni tekshirish
        if (!isNewUser) {
            ApiResponse subscriptionCheckResponse = checkSubscriptionAndRole(business, role);
            if (!subscriptionCheckResponse.isSuccess()) {
                return subscriptionCheckResponse;
            }
        }

        // Branchlarni tekshirish va yig'ish
        Set<Branch> branches = collectBranches(userDto.getBranchIds());
        if (branches.isEmpty()) {
            return new ApiResponse("BRANCH NOT FOUND", false);
        }

        // Userni yaratish va mapping qilish (DTO dan Entity ga)
        User user = createUserFromDto(userDto, business, role, branches);
        userRepository.save(user);

        // Agreement qo'shish (foydalanuvchi uchun)
        agreementService.add(user);

        return new ApiResponse("ADDED", true, user.getId());
    }

    private ApiResponse validateUser(UserDTO userDto) {
        // Business mavjudligini tekshirish
        if (businessRepository.findById(userDto.getBusinessId()).isEmpty()) {
            return new ApiResponse("NOT FOUND BUSINESS", false);
        }

        // Role mavjudligini tekshirish
        if (roleRepository.findById(userDto.getRoleId()).isEmpty()) {
            return new ApiResponse("NOT FOUND ROLE", false);
        }

        return new ApiResponse("VALID", true);
    }

    private Set<Branch> collectBranches(Set<UUID> branchIds) {
        Set<Branch> branches = new HashSet<>();
        for (UUID branchId : branchIds) {
            branchRepository.findById(branchId).ifPresent(branches::add);
        }
        return branches;
    }

    private User createUserFromDto(UserDTO userDto, Business business, Role role, Set<Branch> branches) {
        User user = userMapper.toEntity(userDto);
        user.setBusiness(business);
        user.setRole(role);
        user.setBranches(branches);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setActive(true);
        user.setEnabled(true);
        user.setDateOfEmployment(userDto.getDateOfEmployment() != null ? userDto.getDateOfEmployment() : new Date());
        user.setPassportNumber(userDto.getPassportNumber() != null ? userDto.getPassportNumber() : "");

        // Jobni tekshirish va o'rnatish
        if (userDto.getJobId() != null) {
            jobRepository.findById(userDto.getJobId()).ifPresent(user::setJob);
        }

        // Foto faylni o'rnatish
        if (userDto.getPhotoId() != null) {
            user.setPhoto(attachmentRepository.findById(userDto.getPhotoId()).orElseThrow());
        }

        return user;
    }

    public ApiResponse edit(UUID id, UserDTO userDto) {
        // User mavjudligini tekshirish
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return new ApiResponse("USER NOT FOUND", false);
        }
        User user = optionalUser.get();

        // Umumiy validatsiya: Business, Role va Username tekshiriladi
        ApiResponse validationResponse = validateUser(userDto);
        if (!validationResponse.isSuccess()) {
            return validationResponse;
        }

        // Branchlarni tekshirish va yig'ish
        Set<Branch> branches = collectBranches(userDto.getBranchIds());
        if (branches.isEmpty()) {
            return new ApiResponse("BRANCH NOT FOUND", false);
        }

        // Userni yangilash
        updateUserFromDto(user, userDto, branches);

        // Userni saqlash
        userRepository.save(user);

        return new ApiResponse("UPDATED", true, user.getId());
    }

    private void updateUserFromDto(User user, UserDTO userDto, Set<Branch> branches) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setSureName(userDto.getSureName());
        user.setSex(userDto.isSex());
        user.setBirthday(userDto.getBirthday());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setPassportNumber(userDto.getPassportNumber());
        user.setJshshsr(userDto.getJshshsr());
        user.setAddress(userDto.getAddress());
        user.setDepartment(userDto.getDepartment());
        user.setPosition(userDto.getPosition());
        user.setArrivalTime(userDto.getArrivalTime());
        user.setLeaveTime(userDto.getLeaveTime());
        user.setSalaryAmount(userDto.getSalaryAmount());
        user.setSalaryType(userDto.getSalaryType());
        user.setPinCode(userDto.getPinCode());
        user.setShiftType(userDto.getShiftType());
        user.setProbation(userDto.getProbation());
        user.setBranches(branches);
        user.setDateOfEmployment(userDto.getDateOfEmployment() != null ? userDto.getDateOfEmployment() : user.getDateOfEmployment());
        user.setGrossPriceControlOneUser(userDto.isGrossPriceControlOneUser());
        user.setContractNumber(userDto.getContractNumber());
        user.setDateStartContract(userDto.getDateStartContract() != null ? userDto.getDateStartContract() : null);
        user.setDateEndContract(userDto.getDateEndContract() != null ? userDto.getDateEndContract() : null);
        user.setActive(true);
        user.setEnabled(true);

        // Jobni tekshirish va o'rnatish
        if (userDto.getJobId() != null) {
            jobRepository.findById(userDto.getJobId()).ifPresent(user::setJob);
        }

        // Foto faylni o'rnatish
        if (userDto.getPhotoId() != null) {
            user.setPhoto(attachmentRepository.findById(userDto.getPhotoId()).orElse(null));
        }

        if (userDto.getRoleId() != null) {
            roleRepository.findById(userDto.getRoleId()).ifPresent(user::setRole);
        }

        if (userDto.getBusinessId() != null) {
            businessRepository.findById(userDto.getBusinessId()).ifPresent(user::setBusiness);
        }
    }

    private ApiResponse checkSubscriptionAndRole(Business business, Role role) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findByBusinessIdAndActiveTrue(business.getId());
        if (optionalSubscription.isEmpty()) {
            return new ApiResponse("TARIFF AKTIV EMAS", false);
        }

        Subscription subscription = optionalSubscription.get();
        int maxEmployees = subscription.getTariff().getEmployeeAmount();
        int currentEmployeeCount = userRepository.countByBusiness_Id((business.getId()));
        if (maxEmployees > 0 && currentEmployeeCount >= maxEmployees) {
            return new ApiResponse("You have reached the employee limit", false);
        }

        if (Constants.ADMIN.equals(role.getName())) {
            return new ApiResponse("Admin rolelik hodim qo'shaolmaysiz!", false);
        }

        return new ApiResponse("OK", true);
    }

    public ApiResponse getById(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);

        // Foydalanuvchi topilmasa, xatolik qaytarish
        if (optionalUser.isEmpty()) {
            return new ApiResponse("USER NOT FOUND", false);
        }
        User user = optionalUser.get();

        // User entity ni UserDTO ga aylantirish
        UserDTO userDTO = userMapper.toDto(user);
        userDTO.setPhotoId(user.getPhoto()!=null ? user.getPhoto().getId() : null);

        // Branch id larini qoshish
        Set<UUID> branchesIdList = user.getBranches().stream()
                .map(Branch::getId)
                .collect(Collectors.toSet());
        userDTO.setBranchIds(branchesIdList);

        return new ApiResponse("SUCCESS", true, userDTO);
    }

    public ApiResponse delete(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            return new ApiResponse("USER NOT FOUND", false);

        User user = optionalUser.get();
        if (user.getRole().getName().equals(Constants.ADMIN) || user.getRole().getName().equals(Constants.SUPER_ADMIN))
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


    public ApiResponse getAllByBusinessId(UUID businessId, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> all = userRepository.findAllByBusinessId(businessId, pageable);

        if (all.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }

        // Foydalanuvchilarni DTO ga aylantirish
        List<UserDTO> dtoList = all.getContent().stream().map(user -> {
            UserDTO userDto = userMapper.toDto(user);

            // Role Category va Parent Role Category nomini qo'shish
            Optional.ofNullable(user.getRole())
                    .map(Role::getRoleCategory)
                    .ifPresent(roleCategory -> {
                        userDto.setRoleCategoryName(roleCategory.getName());
                        Optional.ofNullable(roleCategory.getParentRoleCategory())
                                .ifPresent(parentRoleCategory -> userDto.setRoleParentName(parentRoleCategory.getName()));
                    });

            // Branch nomlarini qo'shish
            List<String> branchesName = user.getBranches().stream()
                    .map(Branch::getName)
                    .collect(Collectors.toList());
            userDto.setBranchesName(branchesName);

            // Foydalanuvchining rasmi mavjud bo'lsa photoId ni qo'shish
            Optional.ofNullable(user.getPhoto())
                    .ifPresent(photo -> userDto.setPhotoId(photo.getId()));

            return userDto;
        }).collect(Collectors.toList());

        // Javobni tayyorlash
        Map<String, Object> response = Map.of(
                "users", dtoList,
                "total", all.getTotalElements(),
                "size", all.getSize(),
                "page", all.getNumber(),
                "totalPages", all.getTotalPages()
        );

        return new ApiResponse("FOUND", true, response);
    }

    public ApiResponse getAllByBranchId(UUID branch_id) {
        Optional<Branch> optionalBranch = branchRepository.findById(branch_id);
        if (optionalBranch.isPresent()) {
            Optional<Role> optionalRole = roleRepository.findByName(Constants.SUPER_ADMIN);
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

        // Foydalanuvchi nomini split qilib, barcha so'zlarni bitta qidiruvga birlashtirish
        String[] words = name.split("\\s+");
        // Qidiruvga bo'lingan so'zlarni birlashtirish
        String queryName = String.join("|", words);  // so'zlar orasiga "|" qo'yish orqali regex shaklida birlashtiramiz

        // Userlarni qidirish (nomi branch bilan mos va "superadmin" bo'lmagan)
        Page<User> users = userRepository.findAllByFirstNameRegexAndBranchesIdAndUsernameNot(queryName, branchId, "superadmin", pageable);

        // Agar foydalanuvchilar topilmasa
        if (users.isEmpty()) {
            return new ApiResponse("Not found", false);
        }

        // Userlarni DTO ga aylantirish
        List<User> userList = users.getContent();
        List<UserDTO> userDtoList = userMapper.toDto(userList);

        // User DTOlar bilan Page obyektini yaratish
        Page<UserDTO> pages = new PageImpl<>(userDtoList, pageable, users.getTotalElements());

        return new ApiResponse("Found", true, pages);
    }

    public ApiResponse search(String username) {
        // Username bo'yicha qidiruv
        List<User> all = userRepository.findAllByUsernameContainingIgnoreCase(username);

        // Qidirilgan foydalanuvchilarni DTO formatida olish
        List<UserDTO> dtoList = userMapper.toDto(all);

        // Natijani qaytarish
        if (dtoList.isEmpty()) {
            return new ApiResponse("No users found", false);
        }
        return new ApiResponse("Found", true, dtoList);
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

    public ApiResponse forgotPassword(String phoneNumber, String password) {
        boolean isVerified = verificationService.verifyCode(phoneNumber);
        if (!isVerified) {
            return new ApiResponse("Invalid or expired verification code", false);
        }
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }
        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        verificationService.deleteVerificationCode(phoneNumber);
        return new ApiResponse("OK", true);
    }
}
