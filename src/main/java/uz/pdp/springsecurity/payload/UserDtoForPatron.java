package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoForPatron {
    private String fio;
    private UUID photosId;
    private String role;
    private ProjectInfoDto projectInfoDto;
    private TaskInfoGetDto taskInfoGetDto;
    private List<BonusGetMetDto> bonusGetMetDtoList;
    private TradeResultDto tradeResultDto;
}
