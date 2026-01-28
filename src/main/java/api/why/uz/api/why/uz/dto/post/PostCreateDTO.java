package api.why.uz.api.why.uz.dto.post;

import api.why.uz.api.why.uz.dto.AttachCreateDTO;

public record PostCreateDTO(String title, String content, AttachCreateDTO photo) {
}
