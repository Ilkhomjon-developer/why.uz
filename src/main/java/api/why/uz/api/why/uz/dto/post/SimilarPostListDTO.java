package api.why.uz.api.why.uz.dto.post;

import jakarta.validation.constraints.NotBlank;

public record SimilarPostListDTO(@NotBlank(message = "Except Id cannot be empty") String exceptId) {
}
