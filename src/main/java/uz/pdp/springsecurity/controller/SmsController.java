package uz.pdp.springsecurity.controller;

import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.service.SmsSendService;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsSendService smsSendService;

    public SmsController(SmsSendService smsSendService) {
        this.smsSendService = smsSendService;
    }

    /**
     * SMS yuborish
     * @param recipient Qabul qiluvchining telefon raqami
     * @param messageId Yuborilgan xabar uchun unikal ID
     * @param content Xabar matni
     * @return SMS yuborilganligi haqida xabar
     */
    @PostMapping("/send")
    public String sendSms(@RequestParam String recipient,
                          @RequestParam String messageId,
                          @RequestParam String content) {
        return smsSendService.sendSms(recipient, messageId, content);
    }

    /**
     * SMS holatini tekshirish
     * @param messageId SMS yuborilgan xabar ID'si
     * @return Xabar holati
     */
    @GetMapping("/status/{messageId}")
    public String checkSmsStatus(@PathVariable String messageId) {
        return smsSendService.checkSmsStatus(messageId);
    }
}