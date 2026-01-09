package api.why.uz.api.why.uz.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileDetailsDTO(@NotBlank(message = "Name required") String name) {
}
