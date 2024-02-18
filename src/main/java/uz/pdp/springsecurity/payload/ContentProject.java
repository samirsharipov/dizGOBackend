package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.objenesis.instantiator.util.UnsafeUtils;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentProject {
    private String contentName;
    private double goalAmount;
    private String measurement;
    private UUID photoId;
}
