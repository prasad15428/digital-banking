package SBI.Digital.Banking.System.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class CreateAccountRequestDTO {
    
    @NotBlank(message = "Account type is required")
    private String accountType; // Savings, Current, Business, Salary
    
    @Positive(message = "Initial balance must be positive")
    private Double initialBalance;

    // Constructors
    public CreateAccountRequestDTO() {
    }

    public CreateAccountRequestDTO(String accountType, Double initialBalance) {
        this.accountType = accountType;
        this.initialBalance = initialBalance;
    }

    // Getters and Setters
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(Double initialBalance) {
        this.initialBalance = initialBalance;
    }
}
