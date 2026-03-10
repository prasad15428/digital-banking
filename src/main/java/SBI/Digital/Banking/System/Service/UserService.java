package SBI.Digital.Banking.System.Service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SBI.Digital.Banking.System.DTO.RegistrationRequestDTO;
import SBI.Digital.Banking.System.DTO.RegistrationResponseDTO;
import SBI.Digital.Banking.System.DTO.UpdateUserRequestDTO;
import SBI.Digital.Banking.System.Entity.User;
import SBI.Digital.Banking.System.Exceptions.PasswordMismatchException;
import SBI.Digital.Banking.System.Exceptions.UserAlreadyExistsException;
import SBI.Digital.Banking.System.Repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Register a new user
     * 
     * @param registrationRequest the registration request DTO
     * @return RegistrationResponseDTO with success message
     * @throws UserAlreadyExistsException if email already exists
     * @throws PasswordMismatchException if passwords don't match
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
        
        // Create new user
        User user = new User();
        user.setName(registrationRequest.getName());
        user.setEmail(registrationRequest.getEmail());
        user.setPhone(registrationRequest.getPhone());
        user.setAddress(registrationRequest.getAddress());
        user.setPassword(registrationRequest.getPassword());
        user.setCreatedAt(LocalDateTime.now());
        
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
    
    /**
     * Update user profile (full update)
     * 
     * @param id the user ID to update
     * @param updateRequest the update request with new details
     * @return updated User
     * @throws RuntimeException if user not found
     * @throws UserAlreadyExistsException if email already exists for another user
     */
    @Transactional
    public User updateUser(Long id, UpdateUserRequestDTO updateRequest) {
        User user = findById(id);
        
        // Check if email is being changed and if it already exists
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updateRequest.getEmail())) {
                throw new UserAlreadyExistsException(
                    "Email '" + updateRequest.getEmail() + "' is already in use"
                );
            }
        }
        
        // Update all fields if provided
        if (updateRequest.getName() != null) {
            user.setName(updateRequest.getName());
        }
        if (updateRequest.getEmail() != null) {
            user.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPhone() != null) {
            user.setPhone(updateRequest.getPhone());
        }
        if (updateRequest.getAddress() != null) {
            user.setAddress(updateRequest.getAddress());
        }
        
        return userRepository.save(user);
    }
    
    /**
     * Partial update user profile (update only provided fields)
     * 
     * @param id the user ID to update
     * @param updateRequest the update request with fields to update
     * @return updated User
     * @throws RuntimeException if user not found
     * @throws UserAlreadyExistsException if email already exists for another user
     */
    @Transactional
    public User updateUserPartial(Long id, UpdateUserRequestDTO updateRequest) {
        return updateUser(id, updateRequest);
    }
}
