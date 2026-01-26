package SBI.Digital.Banking.System.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SBI.Digital.Banking.System.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by email
     * @param email the email to search for
     * @return Optional containing User if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by email
     * @param email the email to check
     * @return true if user exists
     */
    boolean existsByEmail(String email);
}
