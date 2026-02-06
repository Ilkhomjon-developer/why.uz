package api.why.uz.api.why.uz.repository;

import api.why.uz.api.why.uz.entity.PostEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<PostEntity, String>, PagingAndSortingRepository<PostEntity, String> {

    @Query("SELECT p from PostEntity p where p.profileId = ?1 and p.visible = true order by p.createdDate desc ")
    Page<PostEntity> findAllByProfileId(Integer profileId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update PostEntity p set p.visible = false where p.id = ?1")
    void updateVisible(String id);

    @Query("SELECT p from PostEntity p where p.visible = true order by p.createdDate desc ")
    Page<PostEntity>getAllPosts(Pageable pageable);


    @Query("SELECT p from PostEntity p where p.id <> ?1 and p.visible = true order by p.createdDate desc limit 3")
    List<PostEntity> findNPosts(String s);

    @Query("SELECT p FROM PostEntity p WHERE p.title LIKE CONCAT('%', :str, '%') AND p.visible = true ORDER BY p.createdDate DESC")
    Page<PostEntity> searchPost(@Param("str") String str, Pageable pageable);
}
