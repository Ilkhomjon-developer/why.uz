package api.why.uz.api.why.uz.service.mail;

import api.why.uz.api.why.uz.dto.sms.SmsProviderTokenDTO;
import api.why.uz.api.why.uz.dto.sms.SmsTokenProviderResponse;
import api.why.uz.api.why.uz.entity.TokenEntity;
import api.why.uz.api.why.uz.repository.mail.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${eskiz.sms.email}")
    private String email;
    @Value("${eskiz.sms.password}")
    private String password;

    private String urlToCreateToken = "http://notify.eskiz.uz/api/auth/login";
    private String urlToRefreshToken = "http://notify.eskiz.uz/api/auth/refresh";

    public String getToken() {

        Optional<TokenEntity> optional = tokenRepository.findTopByOrderByCreatedDateDesc();

        if(optional.isPresent()){

            LocalDateTime tokenCreatedDate = optional.get().getCreateDate();
            LocalDateTime now = LocalDateTime.now();
            long days = Duration.between(tokenCreatedDate, now).toDaysPart();

            if(days >= 30){
               createToken();
            }else if(days == 29){
                refreshToken(optional.get().getToken());
            }else{
                 optional.get().getToken();
            }
        }



        return createToken();

    }

    private String createToken() {
        SmsProviderTokenDTO smsProviderTokenDTO = new SmsProviderTokenDTO();
        smsProviderTokenDTO.setEmail(email);
        smsProviderTokenDTO.setPassword(password);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        RequestEntity<SmsProviderTokenDTO> request = RequestEntity
                .post(urlToCreateToken)
                .headers(headers)
                .body(smsProviderTokenDTO);

        var response = restTemplate.exchange(request, SmsTokenProviderResponse.class);

        String token = response.getBody().getData().getToken();

        TokenEntity entity = new TokenEntity();
        entity.setToken(token);

        tokenRepository.save(entity);

        return token;

    }
    private String refreshToken(String oldToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + oldToken);
        headers.set("Content-Type", "application/json");

        RequestEntity<Void> request = RequestEntity
                .patch(urlToRefreshToken)
                .headers(headers)
                .build();

        var response = restTemplate.exchange(request, SmsTokenProviderResponse.class);
        String newToken = response.getBody().getData().getToken();

        TokenEntity entity = new TokenEntity();
        entity.setToken(newToken);
        tokenRepository.save(entity);

        return newToken;
    }
}
