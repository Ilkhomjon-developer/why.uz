package api.why.uz.api.why.uz.service;

import api.why.uz.api.why.uz.entity.ProfileRoleEntity;
import api.why.uz.api.why.uz.enums.ProfileRole;
import api.why.uz.api.why.uz.repository.ProfileRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfileRoleService {

    private final ProfileRoleRepository profileRoleRepository;

    public void create(Integer profileId, ProfileRole role){
        ProfileRoleEntity entity = new ProfileRoleEntity();
        entity.setProfileId(profileId);
        entity.setRoles(role);
        entity.setCreatedDate(LocalDateTime.now());

        profileRoleRepository.save(entity);
    }

    public void deleteRole(Integer profileId) {
        profileRoleRepository.deleteByProfileId(profileId);
    }
}
