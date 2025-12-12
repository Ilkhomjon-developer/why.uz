package api.why.uz.api.why.uz.repository.mail;

import api.why.uz.api.why.uz.entity.SmsHistoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SmsHistoryRepository extends CrudRepository<SmsHistoryEntity, String> {

    @Query(" from SmsHistoryEntity  where phoneNumber = ?1 order by createdDate desc limit 1")
    Optional<SmsHistoryEntity> findByPhoneNumber(String phone);

    @Transactional
    @Modifying
    @Query("update SmsHistoryEntity set attemptCount = attemptCount + 1 where id = ?1")
    void increaseAttemptCount(String id);
}
