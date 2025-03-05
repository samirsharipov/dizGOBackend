package uz.dizgo.erp.controller;

import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.service.SmsSendService;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsSendService smsSendService;

    public SmsController(SmsSendService smsSendService) {
        this.smsSendService = smsSendService;
    }

    /**
     * SMS yuborish
     *
     * @param recipient Qabul qiluvchining telefon raqami
     * @param content   Xabar matni
     * @return SMS yuborilganligi haqida xabar
     */
    @PostMapping("/send")
    public String sendSms(@RequestParam String recipient,
                          @RequestParam String content) {
        return smsSendService.sendSms(recipient, content);
    }

    /**
     * SMS holatini tekshirish
     *
     * @param messageId SMS yuborilgan xabar ID'si
     * @return Xabar holati
     */
    @GetMapping("/status/{messageId}")
    public String checkSmsStatus(@PathVariable String messageId) {
        return smsSendService.checkSmsStatus(messageId);
    }
}