package SBI.Digital.Banking.System.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class TransactionRequestDTO {
    
    @NotBlank(message = "Transaction type is required")
    private String transactionType; // Deposit, Withdrawal, Transfer
    
    @Positive(message = "Amount must be positive")
    private Double amount;
    
    private String description;

    // Constructors
    public TransactionRequestDTO() {
    }

    public TransactionRequestDTO(String transactionType, Double amount, String description) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
    }

    // Getters and Setters
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
