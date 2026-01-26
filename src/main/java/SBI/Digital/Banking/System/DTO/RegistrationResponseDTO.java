package SBI.Digital.Banking.System.DTO;

public class RegistrationResponseDTO {
    
    private Long userId;
    private String name;
    private String email;
    private String message;
    private Boolean success;

    // Constructors
    public RegistrationResponseDTO() {
    }

    public RegistrationResponseDTO(Long userId, String name, String email, String message, Boolean success) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.message = message;
        this.success = success;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "RegistrationResponseDTO{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
