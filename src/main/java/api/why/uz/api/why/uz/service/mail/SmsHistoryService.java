package api.why.uz.api.why.uz.service.mail;

import api.why.uz.api.why.uz.entity.SmsHistoryEntity;
import api.why.uz.api.why.uz.enums.SmsType;
import api.why.uz.api.why.uz.exps.AppBadException;
import api.why.uz.api.why.uz.repository.mail.SmsHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SmsHistoryService {

    private final SmsHistoryRepository smsHistoryRepository;

    public boolean isSmsSendToAccount(String phone, Integer code){
        Optional<SmsHistoryEntity> optional = smsHistoryRepository.findByPhoneNumber(phone);
        if(optional.isEmpty()){
            return false;
        }
        Integer attemptCount = optional.get().getAttemptCount();
        if(attemptCount >= 5){
            return false;
        }
        if(!optional.get().getCode().equals(code)){
            smsHistoryRepository.increaseAttemptCount(optional.get().getId());
            return false;
        }
        if(optional.get().getCreatedDate().plus(Duration.ofMinutes(2)).isBefore(LocalDateTime.now())){
            throw new AppBadException("Sms Code expired");
        }
        return true;
    }

    public void save(String phone, int code, String body, SmsType smsType) {
        SmsHistoryEntity entity = new SmsHistoryEntity();
        entity.setPhoneNumber(phone);
        entity.setCode(code);
        entity.setMessage(body);
        entity.setSmsType(smsType);
        smsHistoryRepository.save(entity);
    }



}
