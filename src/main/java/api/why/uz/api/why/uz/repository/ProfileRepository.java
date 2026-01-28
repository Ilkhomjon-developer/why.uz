package api.why.uz.api.why.uz.repository;

import api.why.uz.api.why.uz.entity.ProfileEntity;
import api.why.uz.api.why.uz.enums.GeneralStatus;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer> {

    Optional<ProfileEntity> findByUsernameAndVisibleTrue(String username);
    Optional<ProfileEntity> findByIdAndVisibleTrue(Integer id);

    @Modifying
    @Transactional
    @Query("update ProfileEntity set status =?2  where id = ?1")
    void updateStatus(Integer id, GeneralStatus status);


    @Modifying
    @Transactional
    @Query("update ProfileEntity set name =?1  where id = ?2")
    void update(@NotBlank String name, Integer id);

    @Modifying
    @Transactional
    @Query("update ProfileEntity set password =?2  where id = ?1")
    void updatePassword(Integer id, String encode);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set photoId = ?2 where id = ?1")
    void updatePhoto(Integer currentUserId, String photoId);
}
