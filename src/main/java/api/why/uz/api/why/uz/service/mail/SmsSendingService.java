package api.why.uz.api.why.uz.service.mail;

import api.why.uz.api.why.uz.enums.AppLanguage;
import api.why.uz.api.why.uz.enums.SmsType;
import api.why.uz.api.why.uz.exps.AppBadException;
import api.why.uz.api.why.uz.service.ResourceBundleService;
import api.why.uz.api.why.uz.util.RandomNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SmsSendingService {

    private final RestTemplate restTemplate;
    private final SmsHistoryService smsHistoryService;

    private final TokenService tokenService;
    private final ResourceBundleService resourceService;

    public void sendRegistrationSms(String phone){
        int code = RandomNumberGenerator.generate();
        String body = "Bu Eskiz dan test ";
        try {
            sendSms(phone, body, code, SmsType.REGISTRATION);
        }catch (Exception e){
            // e.printStackTrace();
            throw new AppBadException("Something went wrong with Sms");
        }
    }

    private void sendSms(String phone, String body, int code, SmsType smsType) {
        smsHistoryService.save(phone, code, body, smsType);
        String url ="https://notify.eskiz.uz/api/message/sms/send";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("mobile_phone", phone);
        map.add("message", body);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + tokenService.getToken());
        RequestEntity<MultiValueMap<String, String>> request = RequestEntity
                .post(url)
                .headers(headers)
                .body(map);
        restTemplate.exchange(request, String.class);
    }

    public void sendResetPasswordSms(String username, AppLanguage lang) {
        int code = RandomNumberGenerator.generate();
        String message = resourceService.getMessage("sms.activation.code", lang);
        message = String.format(message, code);
        sendSms(username, message, code, SmsType.PASSWORD_RESET);
    }
}
