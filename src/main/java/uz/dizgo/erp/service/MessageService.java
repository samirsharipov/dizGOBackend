package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageSource messageSource;

    // Tilni olish
    public Locale getUserLocale() {
        return LocaleContextHolder.getLocale();
    }

    // Xabarni tilga qarab olish
    public String getMessage(String key) {
        Locale locale = getUserLocale();
        return messageSource.getMessage(key, null, locale);
    }

    // Parametrlar bilan xabarni olish
    public String getMessage(String key, Object[] params) {
        Locale locale = getUserLocale();
        return messageSource.getMessage(key, params, locale);
    }
}
