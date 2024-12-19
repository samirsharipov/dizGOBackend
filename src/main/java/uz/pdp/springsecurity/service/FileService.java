package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import uz.pdp.springsecurity.entity.FileData;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.FileDataDto;
import uz.pdp.springsecurity.repository.FileDateRepository;

import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.repository.UserRepository;

import java.util.*;

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

    public ApiResponse getByUserId(UUID userId, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<FileData> all;
        if (name != null && !name.isEmpty()) {
            all = fileDateRepository.findAllByUser_IdAndFileNameContainingIgnoreCase(userId, name, pageable);
        } else {
            all = fileDateRepository.findAllByUser_Id(userId, pageable);  // faqat userId bo'yicha
        }
        if (all.isEmpty())
            return new ApiResponse("Not found", false);


        List<FileDataDto> allDto = new ArrayList<>();
        for (FileData fileData : all.toList()) {
            FileDataDto dto = new FileDataDto();
            dto.setId(fileData.getId());
            dto.setName(fileData.getFileName());
            dto.setSize(fileData.getSize());
            dto.setDescription(fileData.getDescription());
            dto.setType(fileData.getFileType());
            allDto.add(dto);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("files", allDto);
        result.put("total", all.getTotalElements());
        result.put("totalPages", all.getTotalPages());
        result.put("currentPage", all.getNumber());

        return new ApiResponse("Found", true, result);
    }
}
