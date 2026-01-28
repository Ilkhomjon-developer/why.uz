package api.why.uz.api.why.uz.dto.post;

import api.why.uz.api.why.uz.dto.AttachDTO;
import api.why.uz.api.why.uz.entity.AttachEntity;

public record PostDTO(String id, String title, AttachDTO photo) {
}
