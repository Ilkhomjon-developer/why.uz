package api.why.uz.api.why.uz.repository;

import api.why.uz.api.why.uz.entity.ProfileEntity;
import api.why.uz.api.why.uz.entity.ProfileRoleEntity;
import api.why.uz.api.why.uz.enums.ProfileRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProfileRoleRepository extends CrudRepository<ProfileRoleEntity, Integer> {

        ProfileRoleEntity findByProfile_UsernameAndRoles(String profile_username, ProfileRole roles);

        @Transactional
        @Modifying
        void deleteByProfileId(Integer profileId);

        @Query("select p.roles from ProfileRoleEntity p where p.profileId = ?1")
        List<ProfileRole> getAllRolesListByProfileId(Integer profileId);


}
