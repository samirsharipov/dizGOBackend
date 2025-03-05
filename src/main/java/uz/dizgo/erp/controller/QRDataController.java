package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.QRDataDto;
import uz.dizgo.erp.service.QRDataService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/qrdata")
@RequiredArgsConstructor
public class QRDataController {

    private final QRDataService qrDataService;
    private final ResponseEntityHelper responseEntityHelper;

    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody QRDataDto qrDataDto) {
        return responseEntityHelper.buildResponse(qrDataService.add(qrDataDto));
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable Long id, @RequestBody QRDataDto qrDataDto) {
        return responseEntityHelper.buildResponse(qrDataService.edit(id, qrDataDto));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable Long id) {
        return responseEntityHelper.buildResponse(qrDataService.get(id));
    }

    @GetMapping
    public HttpEntity<?> getAll() {
        return responseEntityHelper.buildResponse(qrDataService.getAll());
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Long id) {
        return responseEntityHelper.buildResponse(qrDataService.delete(id));
    }
}