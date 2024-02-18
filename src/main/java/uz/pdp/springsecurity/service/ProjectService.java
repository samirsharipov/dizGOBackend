package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.NotificationType;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    BonusRepository bonusRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    ProjectTypeRepository projectTypeRepository;

    @Autowired
    ProjectStatusRepository projectStatusRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskStatusRepository taskStatusRepository;

    @Autowired
    FileDateRepository fileDateRepository;
    private final NotificationRepository notificationRepository;

    public ApiResponse add(ProjectDto projectDto) {
        Optional<Branch> optionalBranch = branchRepository.findById(projectDto.getBranchId());
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found", false);
        }

        Optional<Customer> optionalCustomer = customerRepository.findById(projectDto.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            return new ApiResponse("Customer Not Found", false);
        }

        Project project = new Project();
        project.setName(projectDto.getName());
        project.setStartDate(projectDto.getStartDate());
        project.setEndDate(projectDto.getEndDate());
        project.setDeadline(projectDto.getDeadline());
        if (projectDto.getProjectTypeId() == null) {
            return new ApiResponse("You need to add project type");
        } else {
            Optional<ProjectType> optionalProjectType = projectTypeRepository.findById(projectDto.getProjectTypeId());
            optionalProjectType.ifPresent(project::setProjectType);
        }
        project.setCustomer(optionalCustomer.get());
        project.setDescription(projectDto.getDescription());

        List<User> userList = new ArrayList<>();
        for (UUID uuid : projectDto.getUserList()) {
            Optional<User> optionalUser = userRepository.findById(uuid);
            optionalUser.ifPresent(userList::add);
        }
        project.setUsers(userList);

        ProjectStatus uncompleted = projectStatusRepository.findByNameAndBranchId("Uncompleted", optionalBranch.get().getId());
        project.setProjectStatus(uncompleted);

        List<FileData> fileDataList = new ArrayList<>();
        if (projectDto.getFileDateList() != null) {
            for (UUID uuid : projectDto.getFileDateList()) {
                if (uuid != null) {
                    Optional<FileData> optionalFileData = fileDateRepository.findById(uuid);
                    if (optionalFileData.isPresent()) {
                        FileData fileData = optionalFileData.get();
                        fileDataList.add(fileData);
                    }
                }
            }
        }
        project.setFileDataList(fileDataList);

        project.setBudget(projectDto.getBudget());
        List<Stage> stageList = new ArrayList<>();
        if (projectDto.getStages() != null) {
            for (String stages : projectDto.getStages()) {
                Stage stage = new Stage();
                stage.setName(stages);
                stage.setBranch(optionalBranch.get());
                stageList.add(stage);
            }
        }
        List<Stage> list = stageRepository.saveAll(stageList);
        project.setStageList(list);
        project.setProduction(projectDto.isProduction());
        project.setBranch(optionalBranch.get());
        projectRepository.save(project);

        List<User> users = project.getUsers();
        for (User user : users) {
            Notification notification = new Notification();
            notification.setRead(false);
            notification.setName("A new project has been assigned to you!");
            notification.setMessage("You can see Project !");
            notification.setType(NotificationType.NEW_PROJECT);
            notification.setObjectId(project.getId());
            notification.setUserTo(user);
            notificationRepository.save(notification);
        }

        return new ApiResponse("Added", true);
    }

    public ApiResponse edit(UUID id, ProjectDto projectDto) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isEmpty()) {
            return new ApiResponse("Project Not Found", false);
        }
        Project project = optionalProject.get();
        project.setName(projectDto.getName());
        project.setStartDate(projectDto.getStartDate());
        project.setEndDate(projectDto.getEndDate());
        project.setDeadline(projectDto.getDeadline());

        Optional<ProjectType> optionalProjectType = projectTypeRepository.findById(projectDto.getProjectTypeId());
        optionalProjectType.ifPresent(project::setProjectType);

        Optional<Customer> optionalCustomer = customerRepository.findById(projectDto.getCustomerId());
        optionalCustomer.ifPresent(project::setCustomer);

        project.setDescription(projectDto.getDescription());
        List<User> userList = new ArrayList<>();
        if (!projectDto.getUserList().isEmpty()) {
            for (UUID uuid : projectDto.getUserList()) {
                Optional<User> optionalUser = userRepository.findById(uuid);
                optionalUser.ifPresent(userList::add);
            }
        }
        project.setUsers(userList);

        List<FileData> fileDataList = new ArrayList<>();
        if (!projectDto.getFileDateList().isEmpty()) {
            for (UUID uuid : projectDto.getFileDateList()) {
                Optional<FileData> optionalAttachment = fileDateRepository.findById(uuid);
                optionalAttachment.ifPresent(fileDataList::add);
            }
        }

        List<String> stageNames = projectDto.getStages();
        List<Stage> stages = project.getStageList();
        for (int i = 0; i < stageNames.size(); i++) {
            if (i < stages.size()) {
                Stage stage = stages.get(i);
                if (stageNames.get(i) != null) {
                    stage.setName(stageNames.get(i));
                    stageRepository.save(stage);
                }
            } else {
                if (stageNames.get(i) != null) {
                    Stage stage = new Stage();
                    stage.setName(stageNames.get(i));
                    stage.setBranch(project.getBranch());
                    stages.add(stage);
                    stageRepository.save(stage);
                }
            }
        }

        List<Stage> stagesToDelete = new ArrayList<>();
        for (int i = stageNames.size(); i < stages.size(); i++) {
            stagesToDelete.add(stages.get(i));
        }
        stages.removeAll(stagesToDelete);
        try {
            stageRepository.deleteAll(stagesToDelete);
        } catch (Exception e) {
            return new ApiResponse("Unable to update or delete the stage record due to a foreign key constraint violation in the task table !", false);
        }


        project.setStageList(stages);


        project.setFileDataList(fileDataList);

        project.setBudget(projectDto.getBudget());

        project.setProduction(projectDto.isProduction());

        Optional<Branch> optionalBranch = branchRepository.findById(projectDto.getBranchId());
        optionalBranch.ifPresent(project::setBranch);
        projectRepository.save(project);

        return new ApiResponse("Edited", true);
    }

    public ApiResponse get(UUID id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        return optionalProject.map(project -> new ApiResponse("Found", true, project)).orElseGet(() -> new ApiResponse("Project Not Found", false));
    }

    public ApiResponse getOne(UUID id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isEmpty()) {
            return new ApiResponse("Not found", false);
        }
        int totalTask = taskRepository.countAllByProjectId(id);
        int completed = taskRepository.countAllByTaskStatus_OrginalNameAndProjectId("Completed", id);
        int expired = taskRepository.countAllByProjectIdAndExpiredTrue(id);
        List<Task> tasks = taskRepository.findAllByProjectId(id);
        double sum = 0;
        List<TaskGetOne> taskGetOneList = new ArrayList<>();
        for (Task task : tasks) {
            TaskGetOne taskGetOne = new TaskGetOne();
            taskGetOne.setTaskName(task.getName());
            taskGetOne.setTaskStatusName(task.getTaskStatus().getName());
            taskGetOne.setDeadline(task.getDeadLine());
            taskGetOne.setTaskStatusColor(task.getTaskStatus().getColor());
            long status = taskStatusRepository.countByBranchId(optionalProject.get().getBranch().getId()) - 1;
            if (task.getTaskStatus().getOrginalName() != null) {
                if (task.getTaskStatus().getOrginalName().equals("Uncompleted")) {
                    taskGetOne.setPercent(0);
                } else if (task.getTaskStatus().getOrginalName().equals("Completed")) {
                    taskGetOne.setPercent(100);
                }
            } else {
                taskGetOne.setPercent(((task.getTaskStatus().getRowNumber() - 1) * 100) / status);
            }

            taskGetOneList.add(taskGetOne);
            sum += task.getTaskPrice();
        }

        int process = 0;
        if (completed > 0) {
            process = completed * 100 / totalTask;
        }


        ProjectGetOne projectGetOne = new ProjectGetOne();
        Project project = optionalProject.get();
        projectGetOne.setProjectName(project.getName());
        projectGetOne.setProjectTypeName(project.getProjectType().getName());
        projectGetOne.setProjectBudget(project.getBudget());
        projectGetOne.setTotalTask(totalTask);
        projectGetOne.setCompletedTask(completed);
        projectGetOne.setExpiredTask(expired);
        projectGetOne.setTotalTaskSum(sum);
        projectGetOne.setCustomerName(project.getCustomer().getName());
        projectGetOne.setProjectPercent(process);
        projectGetOne.setStartDate(project.getStartDate());
        projectGetOne.setEndDate(project.getEndDate());
        projectGetOne.setDeadline(project.getDeadline());
        projectGetOne.setTaskGetOneList(taskGetOneList);

        List<FileData> fileDataList = project.getFileDataList();
        List<FileDataDto> fileDataDtoList = new ArrayList<>();
        for (FileData fileData : fileDataList) {
            FileDataDto fileDataDto = new FileDataDto();
            fileDataDto.setId(fileData.getId());
            fileDataDto.setName(fileData.getFileName());
            fileDataDto.setSize(fileData.getSize());
            fileDataDtoList.add(fileDataDto);
        }
        projectGetOne.setFileDataList(fileDataDtoList);
        List<Stage> stageList = project.getStageList();
        List<StageProject> stageProjectList = new ArrayList<>();
        for (Stage stage : stageList) {
            int stageAmount = taskRepository.countAllByStageId(stage.getId());
            int completedTasks = taskRepository.countAllByStageIdAndTaskStatus_OrginalName(stage.getId(), "Completed");
            StageProject stageProject = new StageProject();
            stageProject.setStageName(stage.getName());
            stageProject.setStageTasks(stageAmount);
            int percent = 0;
            if (completedTasks > 0) {
                percent = (100 * completedTasks) / stageAmount;
            }
            stageProject.setStagePercent(percent);
            stageProjectList.add(stageProject);
        }
        projectGetOne.setStageProjectList(stageProjectList);

        List<User> userList = project.getUsers();
        List<UserProject> userProjectList = new ArrayList<>();
        for (User user : userList) {
            UserProject userProject = new UserProject();
            userProject.setUserId(user.getId());
            if (user.getPhoto() != null) {
                userProject.setPhotoId(user.getPhoto().getId());
            }
            userProject.setFirstname(user.getFirstName());
            userProject.setLastname(user.getLastName());
            userProjectList.add(userProject);
        }
        projectGetOne.setUserProjectList(userProjectList);

        List<ContentProject> contentProjectList = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isProductions()) {
                ContentProject contentProject = new ContentProject();
                contentProject.setContentName(task.getContent().getProduct().getName());
                contentProject.setMeasurement(task.getContent().getProduct().getMeasurement().getName());
                contentProject.setGoalAmount(task.getGoalAmount());
                if (task.getContent().getProduct() != null) {
                    if (task.getContent().getProduct().getPhoto() != null) {
                        contentProject.setPhotoId(task.getContent().getProduct().getPhoto().getId());
                    }
                } else if (task.getContent().getProductTypePrice() != null) {
                    if (task.getContent().getProductTypePrice().getPhoto() != null) {
                        contentProject.setPhotoId(task.getContent().getProductTypePrice().getPhoto().getId());
                    }
                } else {
                    contentProject.setPhotoId(null);
                }
                contentProjectList.add(contentProject);
            }
        }
        projectGetOne.setContentProjectList(contentProjectList);

        return new ApiResponse("Found", true, projectGetOne);
    }

    public ApiResponse delete(UUID id) {
        boolean exists = projectRepository.existsById(id);
        if (!exists) {
            return new ApiResponse("Project Not Found", false);
        }
        projectRepository.deleteById(id);
        return new ApiResponse("Deleted", true);
    }

    public ApiResponse getAllByBranch(UUID branchId) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch not found", false);
        }
        List<Project> projectList = projectRepository.findAllByBranchId(branchId);
        if (projectList.isEmpty()) {
            return new ApiResponse("Project not found", false);
        }
        return new ApiResponse("Found", true, projectList);
    }

    public ApiResponse updateProjectStatus(UUID projectId, UUID statusId) {

        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isEmpty()) {
            return new ApiResponse("Not found", false);
        }
        Project project = optionalProject.get();


        Optional<ProjectStatus> optionalProjectStatus = projectStatusRepository.findById(statusId);
        if (optionalProjectStatus.isEmpty()) {
            return new ApiResponse("Not found", false);
        }
        ProjectStatus projectStatus = optionalProjectStatus.get();
        if (projectStatus.getName().equals("Completed")) {
            Date deadline = project.getDeadline();
            Date endDate = new Date();
            project.setExpired(!deadline.after(endDate));
            project.setEndDate(endDate);
        } else if (projectStatus.getName().equals("Uncompleted")) {
            project.setEndDate(null);
        } else if (projectStatus.getName().equals("process")) {
            project.setEndDate(null);
        }
        project.setProjectStatus(projectStatus);
        projectRepository.save(project);

        return new ApiResponse("Edited", true);
    }

    public ApiResponse getAllByBranchId(UUID branchId, UUID userId, UUID typeId, UUID customerId, UUID projectStatusId, Date expired, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Project> projectList;

        boolean checkingUser = userId != null;
        boolean checkingType = typeId != null;
        boolean checkingCustomer = customerId != null;
        boolean checkingProjectStatus = projectStatusId != null;
        boolean checkingExpired = expired != null;
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch not found", false);
        }

        if (checkingType && checkingCustomer && checkingProjectStatus && checkingExpired) {
            if (checkingUser)
                projectList = projectRepository.findAllByUsersIdAndProjectTypeIdAndCustomerIdAndProjectStatusIdAndExpiredTrue(userId, typeId, customerId, projectStatusId, pageable);
            else
                projectList = projectRepository.findAllByProjectTypeIdAndCustomerIdAndProjectStatusIdAndExpiredTrue(typeId, customerId, projectStatusId, pageable);
        } else if (checkingType && checkingCustomer && checkingProjectStatus) {
            if (checkingUser)
                projectList = projectRepository.findAllByUsersIdAndProjectTypeIdAndCustomerIdAndProjectStatusId(userId, typeId, customerId, projectStatusId, pageable);
            else
                projectList = projectRepository.findAllByProjectTypeIdAndCustomerIdAndProjectStatusId(typeId, customerId, projectStatusId, pageable);
        } else if (checkingType && checkingCustomer && checkingExpired) {
            if (checkingUser)
                projectList = projectRepository.findAllByUsersIdAndProjectTypeIdAndCustomerIdAndExpiredTrue(userId, typeId, customerId, pageable);
            else
                projectList = projectRepository.findAllByProjectTypeIdAndCustomerIdAndExpiredTrue(typeId, customerId, pageable);
        } else if (checkingType && checkingProjectStatus && checkingExpired) {
            if (checkingUser)
                projectList = projectRepository.findAllByUsersIdAndProjectTypeIdAndProjectStatusIdAndExpiredTrue(userId, typeId, projectStatusId, pageable);
            else
                projectList = projectRepository.findAllByProjectTypeIdAndProjectStatusIdAndExpiredTrue(typeId, projectStatusId, pageable);
        } else if (checkingCustomer && checkingProjectStatus && checkingExpired) {
            if (checkingUser)
                projectList = projectRepository.findAllByUsersIdAndCustomerIdAndProjectStatusIdAndExpiredTrue(userId, customerId, projectStatusId, pageable);
            else
                projectList = projectRepository.findAllByCustomerIdAndProjectStatusIdAndExpiredTrue(customerId, projectStatusId, pageable);

        } else if (checkingType && checkingCustomer) {
            if (checkingUser)
                projectList = projectRepository.findAllByUsersIdAndProjectTypeIdAndCustomerId(userId, typeId, customerId, pageable);
            else
                projectList = projectRepository.findAllByProjectTypeIdAndCustomerId(typeId, customerId, pageable);
        } else if (checkingCustomer && checkingExpired) {
            if (checkingUser)
                projectList = projectRepository.findAllByCustomerIdAndExpiredTrue(customerId, pageable);
            else
                projectList = projectRepository.findAllByCustomerIdAndExpiredTrue(customerId, pageable);
        } else if (checkingType && checkingExpired) {
            if (checkingUser)
                projectList = projectRepository.findAllByUsersIdAndProjectTypeIdAndExpiredTrue(userId, typeId, pageable);
            else
                projectList = projectRepository.findAllByProjectTypeIdAndExpiredTrue(typeId, pageable);
        } else if (checkingProjectStatus && checkingExpired) {
            if (checkingUser)
                projectList = projectRepository.findAllByUsersIdAndProjectStatusIdAndExpiredTrue(userId, projectStatusId, pageable);
            else
                projectList = projectRepository.findAllByProjectStatusIdAndExpiredTrue(projectStatusId, pageable);
        } else if (checkingCustomer && checkingProjectStatus) {
            if (checkingUser)
                projectList = projectRepository.findAllByUsersIdAndCustomerIdAndProjectStatusId(userId, customerId, projectStatusId, pageable);
            else
                projectList = projectRepository.findAllByCustomerIdAndProjectStatusId(customerId, projectStatusId, pageable);
        } else if (checkingType) {
            if (checkingUser)
                projectList = projectRepository.findAllByUsersIdAndProjectTypeId(userId, typeId, pageable);
            else
                projectList = projectRepository.findAllByProjectTypeId(typeId, pageable);
        } else if (checkingCustomer) {
            if (checkingUser)
                projectList = projectRepository.findAllByUsersIdAndCustomerId(userId, customerId, pageable);
            else
                projectList = projectRepository.findAllByCustomerId(customerId, pageable);
        } else if (checkingProjectStatus) {
            if (checkingUser)
                projectList = projectRepository.findAllByUsersIdAndProjectStatusId(userId, projectStatusId, pageable);
            else
                projectList = projectRepository.findAllByProjectStatusId(projectStatusId, pageable);
        } else if (checkingExpired) {
            if (checkingUser)
                projectList = projectRepository.findAllByUsersIdAndBranch_IdAndExpiredTrue(userId, branchId, pageable);
            else
                projectList = projectRepository.findAllByBranch_IdAndExpiredTrue(branchId, pageable);
        } else {
            if (checkingUser)
                projectList = projectRepository.findAllByBranchIdAndUsers_Id(branchId, userId, pageable);
            else
                projectList = projectRepository.findAllByBranch_Id(branchId, pageable);
        }

        assert projectList != null;
        if (projectList.isEmpty()) {
            return new ApiResponse("Project Not Found", false);
        }

        for (Project project : projectList) {
            int completed = taskRepository.countByProjectIdAndTaskStatus_OrginalName(project.getId(), "Completed");
            int all = taskRepository.countByProjectId(project.getId());
            if (completed > 0) {
                int process;
                process = completed * 100 / all;
                project.setProcess(process);
            }
        }

        return new ApiResponse("Found", true, projectList);
    }

    public ApiResponse getProgress(UUID projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isEmpty()) return new ApiResponse("PROJECT NOT FOUND", false);
        Project project = optionalProject.get();
        if (project.getEndDate() != null) return new ApiResponse("PROJECT COMPLETED", false);
        int totalTask = taskRepository.countByProjectId(project.getId());
        if (totalTask == 0) return new ApiResponse("TASK NOT FOUND", false);
        int completedTask = taskRepository.countByProjectIdAndTaskStatus_OrginalName(project.getId(), "Completed");
        int percent = completedTask * 100 / totalTask;

        LocalDate startDate = project.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate deadline = project.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();

        int goalDay = deadline.getDayOfYear() - startDate.getDayOfYear();
        int leftDay = today.getDayOfYear() - startDate.getDayOfYear();
        int remainDay = goalDay - leftDay;
        int lateDay = 0;
        if (completedTask != 0) {
            lateDay = leftDay * totalTask / completedTask;
            lateDay -= goalDay;
        }

        return new ApiResponse("SUCCESS", true, new ProjectProgressDto(
                totalTask,
                completedTask,
                percent,
                java.sql.Date.valueOf(startDate),
                java.sql.Date.valueOf(deadline),
                java.sql.Date.valueOf(deadline.plusDays(lateDay)),
                goalDay,
                leftDay,
                remainDay,
                lateDay
        ));
    }

    public ApiResponse searchByName(String name, UUID branchId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String[] words = name.split("\\s+");
        Page<Project> projects = null;
        for (String word : words) {
            projects = projectRepository.findByNameContainingIgnoreCaseAndBranchId(word, branchId, pageable);
        }
        if (projects == null) {
            return new ApiResponse("Not Found", false);
        }
        return new ApiResponse("Found", true, projects);
    }

    public ApiResponse getOwnProject(UUID branchId, UUID userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Project> projects;

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found", false);
        }

        if (userId != null) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                return new ApiResponse("User Not Found");
            }
            projects = projectRepository.findAllByBranchIdAndUsers_Id(branchId, userId, pageable);
        } else {
            projects = projectRepository.findAllByBranch_Id(branchId, pageable);
        }

        return new ApiResponse("Found", true, projects);
    }
}
