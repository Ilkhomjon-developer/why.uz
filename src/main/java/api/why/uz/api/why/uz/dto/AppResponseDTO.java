package api.why.uz.api.why.uz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class AppResponseDTO <T> {

    private T data;
}
