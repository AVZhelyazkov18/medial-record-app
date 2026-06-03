package bg.nbu.medialrecordapp.repository;

import bg.nbu.medialrecordapp.data.entity.auth.WebAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebAccountRepository extends JpaRepository<WebAccount, Long> {
    Optional<WebAccount> findByEmail(String email);
}
