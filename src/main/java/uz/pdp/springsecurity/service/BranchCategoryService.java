package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.BranchCategory;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.BranchCategoryDto;
import uz.pdp.springsecurity.repository.BranchCategoryRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchCategoryService {
    private final BranchCategoryRepository branchCategoryRepository;

    public ApiResponse create(BranchCategoryDto branchCategoryDto) {
        BranchCategory branchCategory = new BranchCategory(
                branchCategoryDto.getName(),
                branchCategoryDto.getDescription()
        );
        branchCategoryRepository.save(branchCategory);
        return new ApiResponse("Branch category created successfully", true);
    }

    public ApiResponse get(UUID id) {
        Optional<BranchCategory> optionalBranchCategory = branchCategoryRepository.findById(id);
        if (optionalBranchCategory.isPresent()) {
            BranchCategory branchCategory = optionalBranchCategory.get();
            BranchCategoryDto branchCategoryDto = new BranchCategoryDto(
                    branchCategory.getId(),
                    branchCategory.getName(),
                    branchCategory.getDescription()
            );
            return new ApiResponse("Branch category found", true, branchCategoryDto);
        } else {
            return new ApiResponse("Branch category not found", false);
        }
    }

    public ApiResponse getAll() {
        return new ApiResponse("All branch categories", true, branchCategoryRepository.findAll());
    }

    public ApiResponse edit(UUID id, BranchCategoryDto branchCategoryDto) {
        Optional<BranchCategory> optionalBranchCategory = branchCategoryRepository.findById(id);
        if (optionalBranchCategory.isPresent()) {
            BranchCategory branchCategory = optionalBranchCategory.get();
            branchCategory.setName(branchCategoryDto.getName());
            branchCategory.setDescription(branchCategoryDto.getDescription());
            branchCategoryRepository.save(branchCategory);
            return new ApiResponse("Branch category edited successfully", true);
        } else {
            return new ApiResponse("Branch category not found", false);
        }
    }

    public ApiResponse delete(UUID id) {
        Optional<BranchCategory> optionalBranchCategory = branchCategoryRepository.findById(id);
        if (optionalBranchCategory.isPresent()) {
            BranchCategory branchCategory = optionalBranchCategory.get();
            branchCategory.setDeleted(true); // Haqiqiy o'chirish o'rniga deleted ni true qilamiz
            branchCategoryRepository.save(branchCategory);
            return new ApiResponse("Branch category soft deleted successfully", true);
        } else {
            return new ApiResponse("Branch category not found", false);
        }
    }
}