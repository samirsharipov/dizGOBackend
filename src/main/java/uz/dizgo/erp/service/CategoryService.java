package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Business;
import uz.dizgo.erp.entity.Category;
import uz.dizgo.erp.entity.CategoryTranslate;
import uz.dizgo.erp.entity.Language;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.CategoryDto;
import uz.dizgo.erp.payload.CategoryGetDto;
import uz.dizgo.erp.payload.CategoryTranslateDto;
import uz.dizgo.erp.repository.BusinessRepository;
import uz.dizgo.erp.repository.CategoryRepository;
import uz.dizgo.erp.repository.CategoryTranslateRepository;
import uz.dizgo.erp.repository.LanguageRepository;

import javax.transaction.Transactional;
import java.util.*;

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
        category.setName(categoryDto.getName());

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
    @Transactional
    public ApiResponse edit(UUID id, CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            return new ApiResponse("Category not found", false);
        }

        Category category = optionalCategory.get();
        category.setName(categoryDto.getName());
        if (categoryDto.getParentCategoryId() != null) {
            Optional<Category> parentCategoryOptional = categoryRepository.findById(categoryDto.getParentCategoryId());
            parentCategoryOptional.ifPresent(category::setParentCategory);
        }else {
            category.setParentCategory(null);
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

        return new ApiResponse("Category found", true, getGetDto1(optionalCategoryTranslate, category, category.getParentCategory().getId()));
    }

    // Kategoriyani o'chirish
    public ApiResponse delete(UUID id) {
        if (!categoryRepository.existsById(id)) {
            return new ApiResponse("Category not found", false);
        }

        categoryRepository.deleteById(id);
        return new ApiResponse("Category deleted successfully", true);
    }

    public ApiResponse getCategoryTreeByBusiness(UUID businessId, String languageCode) {
        // Ota kategoriya bo'lmagan (root) kategoriyalarni topish
        List<CategoryGetDto> categoryTree = buildCategoryTreeByBusiness(null, businessId, languageCode);

        if (categoryTree.isEmpty()) {
            return new ApiResponse("No categories found for this business", false);
        }
        return new ApiResponse("Category tree retrieved successfully", true, categoryTree);
    }

    private List<CategoryGetDto> buildCategoryTreeByBusiness(UUID parentId, UUID businessId, String languageCode) {
        // Ota kategoriya bo'yicha bolalarni va biznesni filtrlaymiz
        List<Category> childCategories = categoryRepository.findAllByParentCategory_IdAndBusiness_Id(parentId, businessId);

        // Har bir bola uchun DTO yaratamiz
        List<CategoryGetDto> categoryTree = new ArrayList<>();
        for (Category category : childCategories) {
            Optional<CategoryTranslate> optionalTranslate = categoryTranslateRepository
                    .findByCategory_IdAndLanguage_Code(category.getId(), languageCode);

            // DTO yaratish
            CategoryGetDto dto = getGetDto1(optionalTranslate, category, parentId);

            // Rekursiv ravishda bolalarini qo'shamiz
            dto.setChildren(buildCategoryTreeByBusiness(category.getId(), businessId, languageCode));
            categoryTree.add(dto);
        }
        return categoryTree;
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
            categoryGetDto.setLanguageCode(translation.getLanguage().getCode());
            categoryGetDto.setLanguageId(translation.getLanguage().getId());
            categoryGetDto.setName(translation.getName());
            categoryGetDto.setDescription(translation.getDescription());
            categoryGetDtoList.add(categoryGetDto);
        }

        return new ApiResponse("Translations found", true, categoryGetDtoList);
    }

    public ApiResponse getCategoryTree(UUID parentId, String languageCode) {
        // Ota kategoriya ID bo'yicha bolalarni rekursiv topish
        List<CategoryGetDto> categoryTree = buildCategoryTree(parentId, languageCode);

        if (categoryTree.isEmpty()) {
            return new ApiResponse("No categories found", false);
        }
        return new ApiResponse("Category tree retrieved successfully", true, categoryTree);
    }

    private List<CategoryGetDto> buildCategoryTree(UUID parentId, String languageCode) {
        // Ota kategoriya bo'yicha bolalar topiladi
        List<Category> childCategories = categoryRepository.findAllByParentCategory_Id(parentId);

        // Har bir bola uchun DTO yaratamiz
        List<CategoryGetDto> categoryTree = new ArrayList<>();
        for (Category category : childCategories) {
            Optional<CategoryTranslate> optionalTranslate = categoryTranslateRepository
                    .findByCategory_IdAndLanguage_Code(category.getId(), languageCode);

            // DTO yaratish
            CategoryGetDto dto = getGetDto1(optionalTranslate, category, parentId);

            // Rekursiv ravishda bolalarini qo'shamiz
            dto.setChildren(buildCategoryTree(category.getId(), languageCode));
            categoryTree.add(dto);
        }
        return categoryTree;
    }

    private CategoryGetDto getGetDto1(Optional<CategoryTranslate> optionalTranslate, Category category, UUID parentId) {
        // CategoryTranslate mavjudligini tekshirish va DTO yaratish
        CategoryGetDto dto = new CategoryGetDto();
        dto.setId(category.getId());
        dto.setParentId(parentId);
        dto.setName(optionalTranslate.map(CategoryTranslate::getName).orElse(category.getName()));
        dto.setDescription(optionalTranslate.map(CategoryTranslate::getDescription).orElse(category.getDescription()));
        dto.setLanguageId(optionalTranslate.map(CategoryTranslate::getLanguage).map(Language::getId).orElse(null));
        dto.setLanguageCode(optionalTranslate.map(CategoryTranslate::getLanguage).map(Language::getCode).orElse(null));
        return dto;
    }
}