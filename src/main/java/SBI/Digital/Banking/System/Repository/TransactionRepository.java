package SBI.Digital.Banking.System.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import SBI.Digital.Banking.System.Entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    /**
     * Find all transactions for an account
     */
    List<Transaction> findByAccountId(Long accountId);
    
    /**
     * Find transactions by account ID with pagination
     */
    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId ORDER BY t.transactionDate DESC")
    List<Transaction> findRecentTransactions(@Param("accountId") Long accountId);
    
    /**
     * Count successful transactions for an account
     */
    long countByAccountIdAndStatus(Long accountId, String status);
}
