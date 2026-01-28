package api.why.uz.api.why.uz.dto;

import jakarta.validation.constraints.NotNull;

public record AttachCreateDTO (@NotNull(message = "File is required") String id){
}
