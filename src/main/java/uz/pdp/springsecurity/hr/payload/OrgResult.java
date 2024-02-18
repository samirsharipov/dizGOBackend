package uz.pdp.springsecurity.hr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgResult {
    private String tradingName;
    List<OrgResult> organizationChildRelationship;
    public OrgResult(String tradingName) {
        this.tradingName = tradingName;
    }
}
