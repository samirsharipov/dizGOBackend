package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.Content;
import uz.pdp.springsecurity.entity.ContentProduct;
import uz.pdp.springsecurity.entity.Production;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOneContentProductionDto {
    private Content content;

    private Production production;

    private List<CostGetDto> costGetDtoList;

    private List<ContentProduct> contentProductList;

    public GetOneContentProductionDto(Content content, List<CostGetDto> costGetDtoList, List<ContentProduct> contentProductList) {
        this.content = content;
        this.costGetDtoList = costGetDtoList;
        this.contentProductList = contentProductList;
    }

    public GetOneContentProductionDto(Production production, List<CostGetDto> costGetDtoList, List<ContentProduct> contentProductList) {
        this.production = production;
        this.costGetDtoList = costGetDtoList;
        this.contentProductList = contentProductList;
    }
}
