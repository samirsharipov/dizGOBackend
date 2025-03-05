package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCountDto {
    private List<NotificationGetAllDto> notificationGetAllDts;
    private Long notificationCount;
    private Integer totalPageCount;
}
