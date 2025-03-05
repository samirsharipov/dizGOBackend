package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Language;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.LanguageDto;
import uz.dizgo.erp.repository.LanguageRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;

    // Yangi til qo'shish
    public ApiResponse add(LanguageDto languageDto) {
        Language language = new Language();
        language.setCode(languageDto.getCode()); // Til kodini qo'shamiz
        language.setName(languageDto.getName());
        language.setDescription(languageDto.getDescription());

        Language savedLanguage = languageRepository.save(language);
        return new ApiResponse("Language added successfully", true, savedLanguage);
    }

    // Tilni yangilash
    public ApiResponse edit(UUID id, LanguageDto languageDto) {
        Optional<Language> optionalLanguage = languageRepository.findById(id);
        if (optionalLanguage.isEmpty()) {
            return new ApiResponse("Language not found", false);
        }

        Language language = optionalLanguage.get();
        language.setCode(languageDto.getCode()); // Til kodini yangilaymiz
        language.setName(languageDto.getName());
        language.setDescription(languageDto.getDescription());

        languageRepository.save(language);
        return new ApiResponse("Language updated successfully", true, language);
    }

    // Barcha tillarni olish
    public ApiResponse getAll() {
        return new ApiResponse("Languages retrieved successfully", true, languageRepository.findAll());
    }

    // ID bo'yicha tilni olish
    public ApiResponse getById(UUID id) {
        Optional<Language> optionalLanguage = languageRepository.findById(id);
        return optionalLanguage
                .map(language ->
                        new ApiResponse("Language retrieved successfully", true, language))
                .orElseGet(() -> new ApiResponse("Language not found", false));
    }
}