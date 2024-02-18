package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LidReportDto {
    private int totalLid;
    private List<SourceReportDto> sourceReportDtos;
    private List<StatusReportDto> statusReportDtos;
    private int totalSale;
}
