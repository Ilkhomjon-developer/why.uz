package api.why.uz.api.why.uz.dto;


import jakarta.validation.constraints.NotBlank;

public record RegistrationDTO(@NotBlank(message = "Name required")
                              String name,
                              @NotBlank(message = "Username required")
                              String username,
                              @NotBlank(message = "Password required")
                              String password) {
}
