package api.why.uz.api.why.uz.dto.sms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsRequestDTO {
    private String phone;
    private String message;

}