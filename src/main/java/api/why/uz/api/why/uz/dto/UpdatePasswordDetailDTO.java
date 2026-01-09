package api.why.uz.api.why.uz.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordDetailDTO(@NotBlank(message = "Old password required") String oldPassword,
                                      @NotBlank(message = "New message required") String newPassword) {
}
