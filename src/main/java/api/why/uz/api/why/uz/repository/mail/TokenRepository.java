package api.why.uz.api.why.uz.repository.mail;

import api.why.uz.api.why.uz.entity.TokenEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity, Integer> {

    @Query("from TokenEntity order by createDate desc limit 1")
    Optional<TokenEntity> findTopByOrderByCreatedDateDesc();
}
