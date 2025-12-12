package api.why.uz.api.why.uz.dto.sms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsTokenProviderResponse {
    private String message;
    private Data data;
    private String tokenType;

    @Getter
    @Setter
    public static class Data {
        private String token;
    }
}
