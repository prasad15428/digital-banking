package SBI.Digital.Banking.System.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SBI.Digital.Banking.System.DTO.AccountResponseDTO;
import SBI.Digital.Banking.System.DTO.CreateAccountRequestDTO;
import SBI.Digital.Banking.System.DTO.TransactionHistoryDTO;
import SBI.Digital.Banking.System.DTO.TransactionRequestDTO;
import SBI.Digital.Banking.System.Entity.Account;
import SBI.Digital.Banking.System.Entity.Transaction;
import SBI.Digital.Banking.System.Entity.User;
import SBI.Digital.Banking.System.Repository.AccountRepository;
import SBI.Digital.Banking.System.Repository.TransactionRepository;
import SBI.Digital.Banking.System.Repository.UserRepository;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Create a new account for a user
     */
    @Transactional
    public AccountResponseDTO createAccount(Long userId, CreateAccountRequestDTO createAccountRequest) {
        
        // Check if user exists
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        // Validate account type
        String accountType = createAccountRequest.getAccountType();
        if (!isValidAccountType(accountType)) {
            throw new RuntimeException("Invalid account type. Allowed types: Savings, Current, Business, Salary");
        }
        
        // Generate unique account number
        String accountNumber = generateAccountNumber();
        
        // Create new account
        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountType);
        account.setBalance(new BigDecimal(createAccountRequest.getInitialBalance()));
        account.setStatus("Active");
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        
        // Save account
        Account savedAccount = accountRepository.save(account);
        
        // Log initial transaction if balance > 0
        if (createAccountRequest.getInitialBalance() > 0) {
            Transaction transaction = new Transaction();
            transaction.setAccount(savedAccount);
            transaction.setTransactionType("Deposit");
            transaction.setAmount(new BigDecimal(createAccountRequest.getInitialBalance()));
            transaction.setDescription("Account opening deposit");
            transaction.setBalanceAfter(savedAccount.getBalance());
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setStatus("Success");
            transactionRepository.save(transaction);
        }
        
        // Return response
        return new AccountResponseDTO(
            savedAccount.getId(),
            savedAccount.getAccountNumber(),
            savedAccount.getAccountType(),
            savedAccount.getBalance(),
            savedAccount.getStatus(),
            savedAccount.getCreatedAt(),
            savedAccount.getUpdatedAt(),
            "Account created successfully",
            true
        );
    }
    
    /**
     * Get account by ID
     */
    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
    }
    
    /**
     * Get account details as DTO
     */
    public AccountResponseDTO getAccountDetails(Long accountId) {
        Account account = getAccountById(accountId);
        
        return new AccountResponseDTO(
            account.getId(),
            account.getAccountNumber(),
            account.getAccountType(),
            account.getBalance(),
            account.getStatus(),
            account.getCreatedAt(),
            account.getUpdatedAt(),
            "Account details retrieved",
            true
        );
    }
    
    /**
     * Get all accounts for a user
     */
    public List<AccountResponseDTO> getUserAccounts(Long userId) {
        // Verify user exists
        userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        List<Account> accounts = accountRepository.findByUserId(userId);
        
        return accounts.stream()
            .map(account -> new AccountResponseDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getStatus(),
                account.getCreatedAt(),
                account.getUpdatedAt(),
                "Account retrieved",
                true
            ))
            .collect(Collectors.toList());
    }
    
    /**
     * Deposit money into account
     */
    @Transactional
    public TransactionHistoryDTO deposit(Long accountId, TransactionRequestDTO transactionRequest) {
        Account account = getAccountById(accountId);
        
        // Validate account is active
        if (!"Active".equals(account.getStatus())) {
            throw new RuntimeException("Cannot deposit to inactive account");
        }
        
        BigDecimal amount = new BigDecimal(transactionRequest.getAmount());
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be greater than 0");
        }
        
        // Update account balance
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
        
        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType("Deposit");
        transaction.setAmount(amount);
        transaction.setDescription(transactionRequest.getDescription() != null ? 
            transactionRequest.getDescription() : "Deposit");
        transaction.setBalanceAfter(newBalance);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus("Success");
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        return mapTransactionToDTO(savedTransaction);
    }
    
    /**
     * Withdraw money from account
     */
    @Transactional
    public TransactionHistoryDTO withdraw(Long accountId, TransactionRequestDTO transactionRequest) {
        Account account = getAccountById(accountId);
        
        // Validate account is active
        if (!"Active".equals(account.getStatus())) {
            throw new RuntimeException("Cannot withdraw from inactive account");
        }
        
        BigDecimal amount = new BigDecimal(transactionRequest.getAmount());
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdrawal amount must be greater than 0");
        }
        
        // Check sufficient balance
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance for withdrawal");
        }
        
        // Update account balance
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
        
        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType("Withdrawal");
        transaction.setAmount(amount);
        transaction.setDescription(transactionRequest.getDescription() != null ? 
            transactionRequest.getDescription() : "Withdrawal");
        transaction.setBalanceAfter(newBalance);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus("Success");
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        return mapTransactionToDTO(savedTransaction);
    }
    
    /**
     * Get transaction history for an account
     */
    public List<TransactionHistoryDTO> getTransactionHistory(Long accountId) {
        // Verify account exists
        getAccountById(accountId);
        
        List<Transaction> transactions = transactionRepository.findRecentTransactions(accountId);
        
        return transactions.stream()
            .map(this::mapTransactionToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get transaction history with limit
     */
    public List<TransactionHistoryDTO> getTransactionHistory(Long accountId, int limit) {
        List<TransactionHistoryDTO> allTransactions = getTransactionHistory(accountId);
        return allTransactions.stream().limit(limit).collect(Collectors.toList());
    }
    
    /**
     * Transfer money to another account
     */
    @Transactional
    public TransactionHistoryDTO transfer(Long fromAccountId, Long toAccountId, TransactionRequestDTO transactionRequest) {
        Account fromAccount = getAccountById(fromAccountId);
        Account toAccount = getAccountById(toAccountId);
        
        // Validate both accounts are active
        if (!"Active".equals(fromAccount.getStatus())) {
            throw new RuntimeException("Cannot transfer from inactive account");
        }
        if (!"Active".equals(toAccount.getStatus())) {
            throw new RuntimeException("Cannot transfer to inactive account");
        }
        
        BigDecimal amount = new BigDecimal(transactionRequest.getAmount());
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Transfer amount must be greater than 0");
        }
        
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance for transfer");
        }
        
        // Deduct from source account
        BigDecimal fromNewBalance = fromAccount.getBalance().subtract(amount);
        fromAccount.setBalance(fromNewBalance);
        fromAccount.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(fromAccount);
        
        // Add to destination account
        BigDecimal toNewBalance = toAccount.getBalance().add(amount);
        toAccount.setBalance(toNewBalance);
        toAccount.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(toAccount);
        
        // Create transaction for source account
        Transaction fromTransaction = new Transaction();
        fromTransaction.setAccount(fromAccount);
        fromTransaction.setTransactionType("Transfer");
        fromTransaction.setAmount(amount);
        fromTransaction.setDescription("Transfer to " + toAccount.getAccountNumber() + ": " + 
            (transactionRequest.getDescription() != null ? transactionRequest.getDescription() : ""));
        fromTransaction.setBalanceAfter(fromNewBalance);
        fromTransaction.setTransactionDate(LocalDateTime.now());
        fromTransaction.setStatus("Success");
        transactionRepository.save(fromTransaction);
        
        // Create transaction for destination account
        Transaction toTransaction = new Transaction();
        toTransaction.setAccount(toAccount);
        toTransaction.setTransactionType("Transfer");
        toTransaction.setAmount(amount);
        toTransaction.setDescription("Transfer from " + fromAccount.getAccountNumber() + ": " + 
            (transactionRequest.getDescription() != null ? transactionRequest.getDescription() : ""));
        toTransaction.setBalanceAfter(toNewBalance);
        toTransaction.setTransactionDate(LocalDateTime.now());
        toTransaction.setStatus("Success");
        
        Transaction savedTransaction = transactionRepository.save(toTransaction);
        
        return mapTransactionToDTO(savedTransaction);
    }
    
    /**
     * Helper methods
     */
    
    private String generateAccountNumber() {
        // Generate unique account number (max 20 chars: SBI + 8 random chars + 9 random chars)
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 14).toUpperCase();
        return "SBI" + randomPart; // Total: 3 + 14 = 17 characters
    }
    
    private boolean isValidAccountType(String accountType) {
        return accountType.equalsIgnoreCase("Savings") || 
               accountType.equalsIgnoreCase("Current") ||
               accountType.equalsIgnoreCase("Business") ||
               accountType.equalsIgnoreCase("Salary");
    }
    
    private TransactionHistoryDTO mapTransactionToDTO(Transaction transaction) {
        return new TransactionHistoryDTO(
            transaction.getId(),
            transaction.getTransactionType(),
            transaction.getAmount(),
            transaction.getDescription(),
            transaction.getBalanceAfter(),
            transaction.getTransactionDate(),
            transaction.getStatus()
        );
    }
}
