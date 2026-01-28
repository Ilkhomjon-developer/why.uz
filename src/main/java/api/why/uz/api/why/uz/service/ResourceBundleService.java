package api.why.uz.api.why.uz.service;

import api.why.uz.api.why.uz.enums.AppLanguage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ResourceBundleService {

    private final ResourceBundleMessageSource messageSource;

    public String getMessage(String code, AppLanguage lang){
        return messageSource.getMessage(code, null,Locale.forLanguageTag(lang.name()));
    }
}
