package api.why.uz.api.why.uz.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileUsernameDTO(@NotBlank(message = "Username is required") String username) {
}
