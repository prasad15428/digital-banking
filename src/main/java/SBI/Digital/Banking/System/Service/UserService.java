package SBI.Digital.Banking.System.Service;

import SBI.Digital.Banking.System.DTO.RegistrationRequestDTO;
import SBI.Digital.Banking.System.DTO.RegistrationResponseDTO;
import SBI.Digital.Banking.System.Entity.User;
import SBI.Digital.Banking.System.Exceptions.InvalidPasswordException;
import SBI.Digital.Banking.System.Exceptions.PasswordMismatchException;
import SBI.Digital.Banking.System.Exceptions.UserAlreadyExistsException;
import SBI.Digital.Banking.System.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Validate password strength
     * Password must contain:
     * - Minimum 8 characters
     * - At least one uppercase letter
     * - At least one number
     * - At least one special character
     * 
     * @param password the password to validate
     * @throws InvalidPasswordException if password doesn't meet criteria
     */
    private void validatePasswordStrength(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
        
        if (!password.matches(passwordRegex)) {
            throw new InvalidPasswordException(
                "Password must be at least 8 characters and contain an uppercase letter, a number, and a special character"
            );
        }
    }
    
    /**
     * Register a new user
     * 
     * @param registrationRequest the registration request DTO
     * @return RegistrationResponseDTO with success message
     * @throws UserAlreadyExistsException if email already exists
     * @throws PasswordMismatchException if passwords don't match
     * @throws InvalidPasswordException if password doesn't meet strength criteria
     */
    @Transactional
    public RegistrationResponseDTO registerUser(RegistrationRequestDTO registrationRequest) {
        
        // Check if email already exists
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new UserAlreadyExistsException(
                "User with email '" + registrationRequest.getEmail() + "' already exists"
            );
        }
        
        // Check if passwords match
        if (!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
            throw new PasswordMismatchException("Passwords do not match");
        }
        
        // Validate password strength
        validatePasswordStrength(registrationRequest.getPassword());
        
        // Create new user
        User user = new User();
        user.setName(registrationRequest.getName());
        user.setEmail(registrationRequest.getEmail());
        user.setPhone(registrationRequest.getPhone());
        user.setAddress(registrationRequest.getAddress());
        
        // Encrypt password
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        
        // Set metadata
        user.setCreatedAt(LocalDateTime.now());
        user.setIsActive(true);
        user.setFailedLoginAttempts(0);
        user.setIsLocked(false);
        
        // Save user to database
        User savedUser = userRepository.save(user);
        
        // Return success response
        return new RegistrationResponseDTO(
            savedUser.getId(),
            savedUser.getName(),
            savedUser.getEmail(),
            "User registered successfully",
            true
        );
    }
    
    /**
     * Find user by email
     * 
     * @param email the email to search for
     * @return User if found
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
    
    /**
     * Find user by ID
     * 
     * @param id the user ID
     * @return User if found
     */
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}
