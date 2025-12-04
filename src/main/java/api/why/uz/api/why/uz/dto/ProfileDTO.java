package api.why.uz.api.why.uz.dto;

import api.why.uz.api.why.uz.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileDTO {

    private String name;
    private String username;
    private List<ProfileRole> roleList;
    private String token;
}
