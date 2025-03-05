package uz.dizgo.erp.hr.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Branch;
import uz.dizgo.erp.entity.Product;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.entity.WarehouseRasta;
import uz.dizgo.erp.repository.BranchRepository;
import uz.dizgo.erp.repository.ProductRepository;
import uz.dizgo.erp.repository.RastaRepository;
import uz.dizgo.erp.repository.WarehouseRepository;
import uz.dizgo.erp.hr.exception.HRException;
import uz.dizgo.erp.hr.payload.ProductResult;
import uz.dizgo.erp.hr.payload.Result;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final RastaRepository rastaRepository;
    private final BranchRepository branchRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    public HttpEntity<Result> getAllProductsByFilter(UUID branchId, Integer page, Integer limit, UUID rastaId, User user) {
        Page<Product> productPage;
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (rastaId != null) {
            WarehouseRasta rasta = rastaRepository.findById(rastaId).orElseThrow(() -> new HRException("Rasta topilmadi"));
            productPage = productRepository.findAllByRastaList_IdAndActiveTrue(rasta.getId(), pageable);
        } else if (branchId != null) {
            Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new HRException("Filial toilmadi!"));
            productPage = productRepository.findAllByBranch_IdAndActiveTrue(branch.getId(), pageable);
        } else {
            productPage = productRepository.findAllByBusinessIdAndActiveTrue(user.getBusiness().getId(), pageable);
        }
        List<ProductResult> results = new LinkedList<>();
        for (Product product : productPage.getContent()) {
            Double amount;
            if (branchId == null) {
                amount = warehouseRepository.amountByProductSingle(product.getId());
            } else {
                amount = warehouseRepository.amountByProductSingleAndBranchId(product.getId(), branchId);
            }
            amount = amount == null ? 0 : amount;
            Integer warehouseCount = product.getWarehouseCount();
            int count = warehouseCount == null ? 0 : warehouseCount;
            double v = amount / count;
            double percentage = v * 100;
            results.add(new ProductResult(
                    product.getId(),
                    product.getName(),
                    amount,
                    percentage,
                    new LinkedList<>()
            ));

        }
        Map<String, Object> data = new HashMap<>();
        data.put("list", results);
        data.put("totalPages", productPage.getTotalPages());
        return ResponseEntity.ok(new Result(true, "Mahsulotlar ro'yxati", data));
    }
}
