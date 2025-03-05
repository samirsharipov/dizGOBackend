package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.*;
import uz.dizgo.erp.repository.FifoCalculationRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FifoCalculationService {
    private final FifoCalculationRepository fifoRepository;

    public void createPurchaseProduct(PurchaseProduct purchaseProduct, double minusAmount) {
        if (minusAmount > purchaseProduct.getPurchasedQuantity()) return;
        FifoCalculation fifoCalculation = new FifoCalculation(
                purchaseProduct.getPurchase().getBranch(),
                purchaseProduct.getPurchasedQuantity(),
                purchaseProduct.getPurchasedQuantity() - minusAmount,
                purchaseProduct.getBuyPrice(),
                purchaseProduct.getPurchase().getDate(),
                purchaseProduct
        );
        fifoCalculation.setProduct(purchaseProduct.getProduct());
        fifoRepository.save(fifoCalculation);
    }

    public void createProduction(Production production, double minusAmount) {
        if (minusAmount > production.getQuantity()) return;
        FifoCalculation fifoCalculation = new FifoCalculation(
                production.getBranch(),
                production.getQuantity(),
                production.getQuantity() - minusAmount,
                production.getTotalPrice() / production.getQuantity(),
                production.getDate(),
                production
        );
        fifoCalculation.setProduct(production.getProduct());

        fifoRepository.save(fifoCalculation);
    }

    public void createByProduct(Production production, ContentProduct contentProduct, double minusAmount) {
        if (minusAmount > production.getQuantity()) return;
        FifoCalculation fifoCalculation = new FifoCalculation(
                production.getBranch(),
                contentProduct.getQuantity(),
                contentProduct.getQuantity() - minusAmount,
                production.getDate(),
                production
        );
        fifoCalculation.setProduct(contentProduct.getProduct());
        fifoCalculation.setBuyPrice(contentProduct.getProduct().getBuyPrice());
        fifoRepository.save(fifoCalculation);
    }

    public void editPurchaseProduct(PurchaseProduct purchaseProduct, Double amount) {
        Optional<FifoCalculation> optionalFifoCalculation = fifoRepository.findByPurchaseProductId(purchaseProduct.getId());
        if (optionalFifoCalculation.isEmpty()) return;
        FifoCalculation fifoCalculation = optionalFifoCalculation.get();
        fifoCalculation.setBuyPrice(purchaseProduct.getBuyPrice());
        fifoCalculation.setPurchasedAmount(purchaseProduct.getPurchasedQuantity());
        fifoCalculation.setRemainAmount(fifoCalculation.getRemainAmount() + amount);
        if (fifoCalculation.getRemainAmount() <= 0d) fifoCalculation.setActive(false);
        fifoRepository.save(fifoCalculation);
    }

    public void createOrEditTradeProduct(Branch branch, TradeProduct tradeProduct, double quantity) {
        List<FifoCalculation> fifoList;
        double totalBuyPrice = 0;
        Product product = tradeProduct.getProduct();
        fifoList = fifoRepository.findAllByBranchIdAndProductIdAndActiveTrueOrderByCreatedAt(branch.getId(), product.getId());
        totalBuyPrice = createOrEditTradeProductHelper(fifoList, quantity, branch.getBusiness().isSaleMinus(), product.getBuyPrice());
        fifoRepository.saveAll(fifoList);


        double totalSalePrice = tradeProduct.getTotalSalePrice() * quantity / tradeProduct.getTradedQuantity();
        tradeProduct.setProfit(tradeProduct.getProfit() + (totalSalePrice - totalBuyPrice));
    }

    private Double createOrEditTradeProductHelper(List<FifoCalculation> fifoList, double quantity, boolean saleMinus, double buyPriceLast) {
        double buyPrice;
        double totalBuyPrice = 0;
        for (FifoCalculation fifo : fifoList) {
            buyPrice = fifo.getBuyPrice();
            if (fifo.getRemainAmount() > quantity) {
                fifo.setRemainAmount(fifo.getRemainAmount() - quantity);
                totalBuyPrice += quantity * buyPrice;
                quantity = 0;
                break;
            } else if (fifo.getRemainAmount() < quantity) {
                quantity -= fifo.getRemainAmount();
                totalBuyPrice += fifo.getRemainAmount() * buyPrice;
                fifo.setRemainAmount(0);
                fifo.setActive(false);
            } else {
                totalBuyPrice += quantity * buyPrice;
                fifo.setRemainAmount(0);
                fifo.setActive(false);
                quantity = 0;
                break;
            }
        }
        if (saleMinus && quantity > 0) {
            totalBuyPrice += quantity * buyPriceLast;
        }
        return totalBuyPrice;
    }

    public void returnedTaskProduction(Branch branch, ContentProduct contentProduct) {
        List<FifoCalculation> fifoList;
        if (contentProduct.getProduct() != null) {
            Product product = contentProduct.getProduct();
            fifoList = fifoRepository.findFirst20ByBranchIdAndProductIdOrderByCreatedAtDesc(branch.getId(), product.getId());
            returnedTradeHelper(fifoList, contentProduct.getQuantity());
            fifoRepository.saveAll(fifoList);
        }
    }

    public void returnedTrade(Branch branch, TradeProduct tradeProduct, double quantity) {
        List<FifoCalculation> fifoList;
        double totalBuyPrice = 0;
        Product product = tradeProduct.getProduct();
        fifoList = fifoRepository.findFirst20ByBranchIdAndProductIdOrderByCreatedAtDesc(branch.getId(), product.getId());
        totalBuyPrice = returnedTradeHelper(fifoList, quantity);
        fifoRepository.saveAll(fifoList);

        double totalSalePrice = tradeProduct.getTotalSalePrice() * quantity / tradeProduct.getTradedQuantity();
        tradeProduct.setProfit(tradeProduct.getProfit() - (totalSalePrice - totalBuyPrice));
    }

    private Double returnedTradeHelper(List<FifoCalculation> fifoList, Double quantity) {
        double totalBuyPrice = 0;
        for (FifoCalculation fifo : fifoList) {
            if (fifo.getPurchasedAmount() == fifo.getRemainAmount()) continue;
            double soldQuantity = fifo.getPurchasedAmount() - fifo.getRemainAmount();
            if (soldQuantity >= quantity) {
                fifo.setRemainAmount(fifo.getRemainAmount() + quantity);
                fifo.setActive(true);
                totalBuyPrice += quantity * fifo.getBuyPrice();
                return totalBuyPrice;
            } else {
                quantity -= soldQuantity;
                fifo.setRemainAmount(fifo.getPurchasedAmount());
                fifo.setActive(true);
                totalBuyPrice += soldQuantity * fifo.getBuyPrice();
            }
        }
        return totalBuyPrice;
    }

    public ContentProduct createContentProduct(Branch branch, ContentProduct contentProduct) {
        List<FifoCalculation> fifoList;
        Product product = contentProduct.getProduct();
        fifoList = fifoRepository.findAllByBranchIdAndProductIdAndActiveTrueOrderByCreatedAt(branch.getId(), product.getId());
        contentProduct.setTotalPrice(createContentProductHelper(fifoList, contentProduct.getQuantity()));
        fifoRepository.saveAll(fifoList);

        return contentProduct;
    }

    private Double createContentProductHelper(List<FifoCalculation> fifoList, Double quantity) {
        double buyPrice;
        double totalBuyPrice = 0;
        for (FifoCalculation fifo : fifoList) {
            if (fifo.getRemainAmount() > quantity) {
                buyPrice = fifo.getBuyPrice();
                fifo.setRemainAmount(fifo.getRemainAmount() - quantity);
                totalBuyPrice += quantity * buyPrice;
                break;
            } else if (fifo.getRemainAmount() < quantity) {
                buyPrice = fifo.getBuyPrice();
                double amount = fifo.getRemainAmount();
                quantity -= amount;
                totalBuyPrice += amount * buyPrice;
                fifo.setRemainAmount(0);
                fifo.setActive(false);
            } else {
                buyPrice = fifo.getBuyPrice();
                totalBuyPrice += quantity * buyPrice;
                fifo.setRemainAmount(0);
                fifo.setActive(false);
                break;
            }
        }
//        if (saleMinus && quantity > 0){
//            totalBuyPrice += quantity * buyPrice;
//        }
        return totalBuyPrice;
    }

    public void createLossProduct(Branch branch, LossProduct lossProduct) {
        List<FifoCalculation> fifoList;
        Product product = lossProduct.getProduct();
        fifoList = fifoRepository.findAllByBranchIdAndProductIdAndActiveTrueOrderByCreatedAt(branch.getId(), product.getId());
        createLossProductHelper(fifoList, lossProduct.getQuantity());
        fifoRepository.saveAll(fifoList);
    }

    private void createLossProductHelper(List<FifoCalculation> fifoList, double quantity) {
        for (FifoCalculation fifo : fifoList) {
            if (fifo.getRemainAmount() > quantity) {
                fifo.setRemainAmount(fifo.getRemainAmount() - quantity);
                break;
            } else if (fifo.getRemainAmount() < quantity) {
                double amount = fifo.getRemainAmount();
                quantity -= amount;
                fifo.setRemainAmount(0);
                fifo.setActive(false);
            } else {
                fifo.setRemainAmount(0);
                fifo.setActive(false);
                break;
            }
        }
    }

    public void createExchange(ExchangeProductBranch exchangeProductBranch) {
        for (ExchangeProduct exchangeProduct : exchangeProductBranch.getExchangeProductList()) {
            List<FifoCalculation> fifoCalculationList = fifoRepository.findAllByBranchIdAndProductIdAndActiveTrueOrderByCreatedAt(
                    exchangeProductBranch.getShippedBranch().getId(), exchangeProduct.getProduct().getId());
            createExchangeHelper(fifoCalculationList, exchangeProduct);

            FifoCalculation fifoCalculation = new FifoCalculation();
            fifoCalculation.setBranch(exchangeProductBranch.getReceivedBranch());
            fifoCalculation.setDate(exchangeProductBranch.getExchangeDate());
            fifoCalculation.setPurchasedAmount(exchangeProduct.getExchangeProductQuantity());
            fifoCalculation.setRemainAmount(exchangeProduct.getExchangeProductQuantity());
            fifoCalculation.setProduct(exchangeProduct.getProduct());
            fifoCalculation.setBuyPrice(exchangeProduct.getProduct().getBuyPrice());
            fifoRepository.save(fifoCalculation);

        }

    }

    private void createExchangeHelper(List<FifoCalculation> fifoCalculationList, ExchangeProduct exchangeProduct) {
        Double quantity = exchangeProduct.getExchangeProductQuantity();
        for (FifoCalculation fifoCalculation : fifoCalculationList) {
            if (fifoCalculation.getRemainAmount() > quantity) {
                fifoCalculation.setRemainAmount(fifoCalculation.getRemainAmount() - quantity);
                break;
            } else if (fifoCalculation.getRemainAmount() < quantity) {
                double amount = fifoCalculation.getRemainAmount();
                quantity -= amount;
                fifoCalculation.setRemainAmount(0);
                fifoCalculation.setActive(false);
            } else {
                fifoCalculation.setRemainAmount(0);
                fifoCalculation.setActive(false);
                break;
            }
        }
        fifoRepository.saveAll(fifoCalculationList);
    }

    public double productBuyPriceByBranch(UUID branchId) {
        return productBuyPrice(fifoRepository.findAllByBranchIdAndActiveTrue(branchId));
    }

    public double productBuyPriceByBusiness(UUID businessId) {
        return productBuyPrice(fifoRepository.findAllByBranch_BusinessIdAndActiveTrue(businessId));
    }

    private double productBuyPrice(List<FifoCalculation> fifoCalculationList) {
        double totalBuyPrice = 0;
        for (FifoCalculation fifo : fifoCalculationList) {
            totalBuyPrice += (fifo.getRemainAmount() * fifo.getBuyPrice());
        }
        return totalBuyPrice;
    }
}
