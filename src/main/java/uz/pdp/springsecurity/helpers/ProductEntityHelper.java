package uz.pdp.springsecurity.helpers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.repository.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class ProductEntityHelper {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final MeasurementRepository measurementRepository;

    public Product cloneProduct(UUID productId, Branch branch) {
        Product originalProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Business business = branch.getBusiness();

        Category category = originalProduct.getCategory() != null
                ? findOrCreate(
                () -> categoryRepository.findByBusiness_IdAndName(business.getId(), originalProduct.getCategory().getName()),
                () -> createCategory(originalProduct.getCategory(), business),
                categoryRepository::save)
                : null;

        Brand brand = originalProduct.getBrand() != null
                ? findOrCreate(
                () -> brandRepository.findByBusiness_IdAndName(business.getId(), originalProduct.getBrand().getName()),
                () -> createBrand(originalProduct.getBrand(), business),
                brandRepository::save)
                : null;

        Measurement measurement = originalProduct.getMeasurement() != null
                ? findOrCreate(
                () -> measurementRepository.findByBusinessIdAndName(business.getId(), originalProduct.getMeasurement().getName()),
                () -> createMeasurement(originalProduct.getMeasurement(), business),
                measurementRepository::save)
                : null;

        Product clonedProduct = new Product();
        clonedProduct.setName(originalProduct.getName());
        clonedProduct.setDescription(originalProduct.getDescription());
        clonedProduct.setLongDescription(originalProduct.getLongDescription());
        clonedProduct.setBarcode(originalProduct.getBarcode());
        clonedProduct.setPhoto(originalProduct.getPhoto());
        clonedProduct.setSalePrice(originalProduct.getSalePrice());
        clonedProduct.setBuyPrice(originalProduct.getBuyPrice());
        clonedProduct.setActive(true);
        clonedProduct.setClone(true);

        clonedProduct.setBranch(new ArrayList<>(Collections.singletonList(branch)));
        clonedProduct.setBusiness(business);
        if (category != null) clonedProduct.setCategory(category);
        if (brand != null) clonedProduct.setBrand(brand);
        if (measurement != null) clonedProduct.setMeasurement(measurement);

        return productRepository.save(clonedProduct);
    }

    private <T> T findOrCreate(Supplier<Optional<T>> finder, Supplier<T> creator, Function<T, T> saver) {
        return finder.get().orElseGet(() -> saver.apply(creator.get()));
    }

    private Category createCategory(Category originalCategory, Business business) {
        return categoryRepository.save(new Category(originalCategory.getName(), business));
    }

    private Brand createBrand(Brand originalBrand, Business business) {
        return brandRepository.save(new Brand(originalBrand.getName(), business));
    }

    private Measurement createMeasurement(Measurement orginalMeasurement, Business business) {
        return measurementRepository.save(new Measurement(orginalMeasurement.getName(), business));
    }
}