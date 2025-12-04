package api.why.uz.api.why.uz.repository;

import api.why.uz.api.why.uz.entity.ProfileEntity;
import api.why.uz.api.why.uz.enums.GeneralStatus;
import jakarta.transaction.Transactional;
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


}
