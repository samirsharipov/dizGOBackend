package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.LanguageDto;
import uz.pdp.springsecurity.service.LanguageService;

import java.util.UUID;

@RestController
@RequestMapping("/api/language")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    // Til qo'shish
    @PostMapping
    public HttpEntity<?> add(@RequestBody LanguageDto languageDto) {
        ApiResponse apiResponse = languageService.add(languageDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    // Tilni yangilash
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody LanguageDto languageDto) {
        ApiResponse apiResponse = languageService.edit(id, languageDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    // Barcha tillarni olish
    @GetMapping
    public HttpEntity<?> getAll() {
        ApiResponse apiResponse = languageService.getAll();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    // Tilni ID bo'yicha olish
    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = languageService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}