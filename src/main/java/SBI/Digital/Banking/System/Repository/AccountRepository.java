package SBI.Digital.Banking.System.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SBI.Digital.Banking.System.Entity.Account;
import SBI.Digital.Banking.System.Entity.User;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    /**
     * Find account by account number
     */
    Optional<Account> findByAccountNumber(String accountNumber);
    
    /**
     * Find all accounts for a user
     */
    List<Account> findByUser(User user);
    
    /**
     * Find all accounts for a user ID
     */
    List<Account> findByUserId(Long userId);
    
    /**
     * Check if account number exists
     */
    boolean existsByAccountNumber(String accountNumber);
    
    /**
     * Find active accounts for a user
     */
    List<Account> findByUserIdAndStatus(Long userId, String status);
}
