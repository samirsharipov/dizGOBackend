package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.Category;
import uz.pdp.springsecurity.entity.CategoryTranslate;
import uz.pdp.springsecurity.entity.Language;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.CategoryDto;
import uz.pdp.springsecurity.payload.CategoryGetDto;
import uz.pdp.springsecurity.payload.CategoryTranslateDto;
import uz.pdp.springsecurity.repository.BusinessRepository;
import uz.pdp.springsecurity.repository.CategoryRepository;
import uz.pdp.springsecurity.repository.CategoryTranslateRepository;
import uz.pdp.springsecurity.repository.LanguageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BusinessRepository businessRepository;
    private final CategoryTranslateRepository categoryTranslateRepository;
    private final LanguageRepository languageRepository;

    // Kategoriya qo'shish
    public ApiResponse add(CategoryDto categoryDto) {
        Optional<Business> optionalBusiness = businessRepository.findById(categoryDto.getBusinessId());
        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("Business not found", false);
        }

        Category category = new Category();
        category.setBusiness(optionalBusiness.get());

        if (categoryDto.getParentCategoryId() != null) {
            Optional<Category> optionalParentCategory = categoryRepository.findById(categoryDto.getParentCategoryId());
            if (optionalParentCategory.isPresent()) {
                category.setParentCategory(optionalParentCategory.get());
            }
        }

        category = categoryRepository.save(category);

        // Tarjima qo'shish
        for (CategoryTranslateDto translateDto : categoryDto.getCategoryTranslateDtoList()) {
            addTranslation(category, translateDto);
        }

        return new ApiResponse("Category added successfully", true);
    }

    // Tarjima qo'shish uchun metod
    private void addTranslation(Category category, CategoryTranslateDto translateDto) {
        Optional<Language> optionalLanguage = languageRepository.findById(translateDto.getLanguageId());
        if (optionalLanguage.isPresent()) {
            CategoryTranslate categoryTranslate = new CategoryTranslate();
            categoryTranslate.setCategory(category);
            categoryTranslate.setName(translateDto.getName());
            categoryTranslate.setDescription(translateDto.getDescription());
            categoryTranslate.setLanguage(optionalLanguage.get());
            categoryTranslateRepository.save(categoryTranslate);
        }
    }

    // Kategoriyani tahrirlash
    public ApiResponse edit(UUID id, CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            return new ApiResponse("Category not found", false);
        }

        Category category = optionalCategory.get();
        if (categoryDto.getParentCategoryId() != null) {
            Optional<Category> parentCategoryOptional = categoryRepository.findById(categoryDto.getParentCategoryId());
            parentCategoryOptional.ifPresent(category::setParentCategory);
        }

        // Tarjimalarni yangilash
        categoryTranslateRepository.deleteAllByCategory_Id(id); // Eski tarjimalarni o'chirish
        for (CategoryTranslateDto translateDto : categoryDto.getCategoryTranslateDtoList()) {
            addTranslation(category, translateDto);
        }

        categoryRepository.save(category);
        return new ApiResponse("Category edited successfully", true);
    }

    // Kategoriyani olish
    public ApiResponse get(UUID id, String languageCode) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            return new ApiResponse("Category not found", false);
        }

        Category category = optionalCategory.get();

        Optional<CategoryTranslate> optionalCategoryTranslate
                = categoryTranslateRepository.findByCategory_IdAndLanguage_Code(id, languageCode);

        return new ApiResponse("Category found", true, getGetDto(optionalCategoryTranslate, category));
    }

    @NotNull
    private static CategoryGetDto getGetDto(Optional<CategoryTranslate> optionalCategoryTranslate, Category category) {
        CategoryGetDto categoryGetDto = new CategoryGetDto();
        if (optionalCategoryTranslate.isPresent()) {
            CategoryTranslate categoryTranslate = optionalCategoryTranslate.get();
            categoryGetDto.setId(category.getId());
            categoryGetDto.setName(categoryTranslate.getName());
            categoryGetDto.setDescription(categoryTranslate.getDescription());
        } else {
            categoryGetDto.setId(category.getId());
            categoryGetDto.setName(category.getName());
            categoryGetDto.setDescription(category.getDescription());
        }
        return categoryGetDto;
    }

    // Kategoriyani o'chirish
    public ApiResponse delete(UUID id) {
        if (!categoryRepository.existsById(id)) {
            return new ApiResponse("Category not found", false);
        }

        categoryRepository.deleteById(id);
        return new ApiResponse("Category deleted successfully", true);
    }

    public ApiResponse getAllByBusinessId(UUID businessId, String languageCode) {
        // Business ID bo'yicha asosiy kategoriyalarni topish
        List<Category> categories = categoryRepository.findAllByBusiness_IdAndParentCategoryIsNull(businessId);
        if (categories.isEmpty()) {
            return new ApiResponse("No categories found", false);
        }

        List<CategoryGetDto> categoryGetDtoList = categories.stream()
                .map(category -> {
                    // Har bir kategoriya uchun tarjimani olish
                    Optional<CategoryTranslate> categoryTranslate = categoryTranslateRepository
                            .findByCategory_IdAndLanguage_Code(category.getId(), languageCode);

                    // Kategoriyaning DTO'sini yaratish
                    CategoryGetDto categoryGetDto = getGetDto(categoryTranslate, category);

                    // Bolalar kategoriyalarini topish
                    List<Category> childCategories = categoryRepository.findAllByParentCategory_Id(category.getId());
                    List<CategoryGetDto> childCategoryDtoList = childCategories.stream()
                            .map(childCategory -> {
                                // Har bir bola kategoriya uchun tarjimani olish
                                Optional<CategoryTranslate> childCategoryTranslate = categoryTranslateRepository
                                        .findByCategory_IdAndLanguage_Code(childCategory.getId(), languageCode);
                                return getGetDto(childCategoryTranslate, childCategory);
                            })
                            .collect(Collectors.toList());

                    // Bolalar kategoriyalarini qo'shish
                    categoryGetDto.setChildren(childCategoryDtoList);
                    return categoryGetDto;
                })
                .collect(Collectors.toList());

        return new ApiResponse("Categories found", true, categoryGetDtoList);
    }

    // Kategoriya ID bo'yicha barcha tarjimalarni olish
    public ApiResponse getAllTranslations(UUID categoryId) {

        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            return new ApiResponse("Category not found", false);
        }

        List<CategoryGetDto> categoryGetDtoList = new ArrayList<>();

        for (CategoryTranslate translation : optionalCategory.get().getTranslations()) {
            CategoryGetDto categoryGetDto = new CategoryGetDto();
            categoryGetDto.setId(categoryId);
            categoryGetDto.setName(translation.getName());
            categoryGetDto.setDescription(translation.getDescription());
            categoryGetDtoList.add(categoryGetDto);
        }

        return new ApiResponse("Translations found", true, categoryGetDtoList);
    }

    // Bola kategoriyalarini olish
    public ApiResponse getAllChildCategoriesById(UUID parentId, String languageCode) {
        List<Category> childCategories = categoryRepository.findAllByParentCategory_Id(parentId);
        if (childCategories.isEmpty()) {
            return new ApiResponse("Child categories not found", false);
        }

        List<CategoryGetDto> categoryGetDtoList = new ArrayList<>();
        for (Category category : childCategories) {
            Optional<CategoryTranslate> optionalCategoryTranslate
                    = categoryTranslateRepository.findByCategory_IdAndLanguage_Code(category.getId(), languageCode);
            categoryGetDtoList.add(getGetDto(optionalCategoryTranslate, category));
        }
        return new ApiResponse("Child categories found", true, categoryGetDtoList);
    }
}