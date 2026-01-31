package SBI.Digital.Banking.System.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    
    @NotBlank(message = "Transaction type is required")
    @Column(length = 20)
    private String transactionType; // Deposit, Withdrawal, Transfer, Purchase
    
    @NotNull(message = "Amount is required")
    @Column(precision = 19, scale = 2)
    private BigDecimal amount;
    
    @Column(length = 255)
    private String description;
    
    @NotNull(message = "Balance after transaction is required")
    @Column(precision = 19, scale = 2)
    private BigDecimal balanceAfter;
    
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
    
    @Column(length = 50)
    private String status; // Success, Failed, Pending

    // Constructors
    public Transaction() {
    }

    public Transaction(Account account, String transactionType, BigDecimal amount, 
                      String description, BigDecimal balanceAfter, String status) {
        this.account = account;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.balanceAfter = balanceAfter;
        this.transactionDate = LocalDateTime.now();
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", balanceAfter=" + balanceAfter +
                ", transactionDate=" + transactionDate +
                ", status='" + status + '\'' +
                '}';
    }
}
