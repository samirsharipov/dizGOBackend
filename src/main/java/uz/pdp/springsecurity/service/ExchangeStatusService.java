package uz.pdp.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.ExchangeProduct;
import uz.pdp.springsecurity.entity.ExchangeStatus;
import uz.pdp.springsecurity.entity.Measurement;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.repository.ExchangeProductRepository;
import uz.pdp.springsecurity.repository.ExchangeStatusRepository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ExchangeStatusService {

    @Autowired
    ExchangeStatusRepository exchangeStatusRepository;

    public ApiResponse getAllStatus(){
        List<ExchangeStatus> exchangeStatuses = exchangeStatusRepository.findAll();
        if (exchangeStatuses.isEmpty()) return new ApiResponse("NOT FOUND",false);
        return new ApiResponse("SUCCESS",true,exchangeStatuses);
    }
}
