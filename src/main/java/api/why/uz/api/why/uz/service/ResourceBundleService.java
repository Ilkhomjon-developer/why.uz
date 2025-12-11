package api.why.uz.api.why.uz.service;

import api.why.uz.api.why.uz.enums.AppLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ResourceBundleService {

    @Autowired
    private ResourceBundleMessageSource messageSource;

    public String getMessage(String code, AppLanguage lang){

        return messageSource.getMessage(code, null,Locale.forLanguageTag(lang.name()));
    }
}
