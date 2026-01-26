package SBI.Digital.Banking.System.Exceptions;

public class PasswordMismatchException extends RuntimeException {
    
    public PasswordMismatchException(String message) {
        super(message);
    }

    public PasswordMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
