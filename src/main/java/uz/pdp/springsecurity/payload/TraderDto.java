package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
    @NoArgsConstructor
    public class TraderDto {
        private UUID traderId;
        private UUID photoId;
        private String traderName;
        private Double  quantitySold;

    public TraderDto(UUID traderId, UUID photoId, String traderName, Double quantitySold) {
        this.photoId = photoId;
        this.traderId = traderId;
        this.traderName = traderName;
        this.quantitySold = quantitySold;
    }


    public TraderDto(UUID traderId, String traderName, Double quantitySold) {
        this.traderId = traderId;
        this.traderName = traderName;
        this.quantitySold = quantitySold;
    }
}
