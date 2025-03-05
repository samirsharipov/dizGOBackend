package uz.dizgo.erp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.ExchangeStatus;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.repository.ExchangeStatusRepository;

import java.util.List;

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
