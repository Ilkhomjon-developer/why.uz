package api.why.uz.api.why.uz.dto.sms;

import jakarta.validation.constraints.NotNull;

public record SmsVerificationDTO(@NotNull(message = ("Username cannot be null")) String username,
                              @NotNull(message = ("Email code cannot be null")) Integer code) {

}
