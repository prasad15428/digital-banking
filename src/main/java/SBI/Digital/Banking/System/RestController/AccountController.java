package SBI.Digital.Banking.System.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import SBI.Digital.Banking.System.DTO.AccountResponseDTO;
import SBI.Digital.Banking.System.DTO.CreateAccountRequestDTO;
import SBI.Digital.Banking.System.DTO.TransactionHistoryDTO;
import SBI.Digital.Banking.System.DTO.TransactionRequestDTO;
import SBI.Digital.Banking.System.Service.AccountService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    /**
     * Health check endpoint
     * GET /api/accounts/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Account Service is running");
    }
    
    /**
     * Create a new account
     * POST /api/accounts/create/{userId}
     * 
     * @param userId the user ID
     * @param createAccountRequest the account creation request
     * @return ResponseEntity with account response
     */
    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createAccount(@PathVariable Long userId, 
                                          @Valid @RequestBody CreateAccountRequestDTO createAccountRequest) {
        try {
            AccountResponseDTO response = accountService.createAccount(userId, createAccountRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Get all accounts for a user
     * GET /api/accounts/user/{userId}
     * 
     * @param userId the user ID
     * @return ResponseEntity with list of accounts
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserAccounts(@PathVariable Long userId) {
        try {
            List<AccountResponseDTO> accounts = accountService.getUserAccounts(userId);
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Get account balance
     * GET /api/accounts/{accountId}/balance
     * 
     * @param accountId the account ID
     * @return ResponseEntity with balance
     */
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> getBalance(@PathVariable Long accountId) {
        try {
            AccountResponseDTO account = accountService.getAccountDetails(accountId);
            return ResponseEntity.ok(account.getBalance());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Get account details
     * GET /api/accounts/{accountId}
     * 
     * @param accountId the account ID
     * @return ResponseEntity with account details
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccountDetails(@PathVariable Long accountId) {
        try {
            AccountResponseDTO response = accountService.getAccountDetails(accountId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Deposit money into account
     * POST /api/accounts/{accountId}/deposit
     * 
     * @param accountId the account ID
     * @param transactionRequest the transaction details
     * @return ResponseEntity with transaction details
     */
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<?> deposit(@PathVariable Long accountId,
                                    @Valid @RequestBody TransactionRequestDTO transactionRequest) {
        try {
            TransactionHistoryDTO response = accountService.deposit(accountId, transactionRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Withdraw money from account
     * POST /api/accounts/{accountId}/withdraw
     * 
     * @param accountId the account ID
     * @param transactionRequest the transaction details
     * @return ResponseEntity with transaction details
     */
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable Long accountId,
                                     @Valid @RequestBody TransactionRequestDTO transactionRequest) {
        try {
            TransactionHistoryDTO response = accountService.withdraw(accountId, transactionRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Transfer money to another account
     * POST /api/accounts/{fromAccountId}/transfer/{toAccountId}
     * 
     * @param fromAccountId the source account ID
     * @param toAccountId the destination account ID
     * @param transactionRequest the transaction details
     * @return ResponseEntity with transaction details
     */
    @PostMapping("/{fromAccountId}/transfer/{toAccountId}")
    public ResponseEntity<?> transfer(@PathVariable Long fromAccountId,
                                     @PathVariable Long toAccountId,
                                     @Valid @RequestBody TransactionRequestDTO transactionRequest) {
        try {
            TransactionHistoryDTO response = accountService.transfer(fromAccountId, toAccountId, transactionRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Get transaction history for an account
     * GET /api/accounts/{accountId}/transactions
     * 
     * @param accountId the account ID
     * @param limit optional limit for number of transactions (default: all)
     * @return ResponseEntity with list of transactions
     */
    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<?> getTransactionHistory(@PathVariable Long accountId,
                                                   @RequestParam(required = false) Integer limit) {
        try {
            List<TransactionHistoryDTO> transactions;
            if (limit != null && limit > 0) {
                transactions = accountService.getTransactionHistory(accountId, limit);
            } else {
                transactions = accountService.getTransactionHistory(accountId);
            }
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        }
    }
}
