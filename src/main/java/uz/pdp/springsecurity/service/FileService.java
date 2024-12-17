package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import uz.pdp.springsecurity.entity.FileData;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.repository.FileDateRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import uz.pdp.springsecurity.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileDateRepository fileDateRepository;
    private final UserRepository userRepository;

    public ApiResponse saveFileToDatabase(String fileName, byte[] fileData, long size, UUID userId, String description) {


        FileData fileDataEntity = new FileData();
        fileDataEntity.setFileName(fileName);
        fileDataEntity.setFileData(fileData);
        fileDataEntity.setSize(size);

        if (userId != null) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                fileDataEntity.setUser(user);
                fileDataEntity.setDescription(description);
            }
        }
        fileDateRepository.save(fileDataEntity);
        return new ApiResponse("Found", true, fileDataEntity.getId());
    }

    public FileData getFileFromDatabase(UUID fileId) {
        return fileDateRepository.findById(fileId).orElse(null);
    }


    public ApiResponse deleteById(UUID fileId) {
        boolean exists = fileDateRepository.existsById(fileId);
        if (!exists) {
            return new ApiResponse("Not found", false);
        }
        fileDateRepository.deleteById(fileId);
        return new ApiResponse("Deleted", true);
    }

    public ApiResponse getByUserId(UUID userId) {
        List<FileData> all = fileDateRepository.findAllByUser_Id(userId);
        if (all.isEmpty()) {
            return new ApiResponse("Not found", false);
        }
        return new ApiResponse("Found", true, all);
    }
}
