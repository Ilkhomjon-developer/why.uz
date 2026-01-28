package api.why.uz.api.why.uz.repository;

import api.why.uz.api.why.uz.entity.AttachEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachRepository extends JpaRepository<AttachEntity, String> {

    @Modifying
    @Transactional
    @Query("update AttachEntity a set a.visible = false  where a.id = ?1")
    void deletePhotoById(String photoId);

    @Modifying
    @Transactional
    @Query("update AttachEntity a set a.visible = false where a.id = ?1")
    void updateVisible(String photoId);
}
