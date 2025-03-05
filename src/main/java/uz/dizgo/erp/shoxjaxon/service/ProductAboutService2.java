package uz.dizgo.erp.shoxjaxon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.shoxjaxon.activity.ProductAbout2;
import uz.dizgo.erp.shoxjaxon.repository.ProductAboutRepository2;

import java.util.List;
import java.util.UUID;

@Service
public class ProductAboutService2 {

    private final ProductAboutRepository2 productAboutRepository2;


    @Autowired
    public ProductAboutService2(ProductAboutRepository2 productAboutRepository2) {
        this.productAboutRepository2 = productAboutRepository2;
    }

    public List<ProductAbout2> getAllDescriptionsWithKeyword(String keyword, UUID branchId) {
        return productAboutRepository2.getAllDescriptionsWithKeyword(keyword, branchId);
    }

    public List<ProductAbout2> getAllProductAboutData() {
        return productAboutRepository2.getAllProductAboutData();
    }

    public List<ProductAbout2> getAllDescriptionsHomashyo(String keyword, UUID branchId) {
        return productAboutRepository2.getAllDescriptionsHomashyo(keyword, branchId);
    }

    public List<ProductAbout2> getAllDescriptionsBrak(String keyword, UUID branchId) {
        return productAboutRepository2.getAllDescriptionsBrak( keyword,  branchId);

    }


    public List<ProductAbout2> getAllDescriptionsWithKeywordAndBranchId(String keyword, UUID branchId) {
        return productAboutRepository2.getAllDescriptionsWithKeyword(keyword, branchId);
    }


}
