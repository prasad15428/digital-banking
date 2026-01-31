package SBI.Digital.Banking.System.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import SBI.Digital.Banking.System.DTO.RegistrationRequestDTO;
import SBI.Digital.Banking.System.DTO.RegistrationResponseDTO;
import SBI.Digital.Banking.System.DTO.UpdateUserRequestDTO;
import SBI.Digital.Banking.System.Entity.User;
import SBI.Digital.Banking.System.Exceptions.PasswordMismatchException;
import SBI.Digital.Banking.System.Exceptions.UserAlreadyExistsException;
import SBI.Digital.Banking.System.Service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Register a new user
     * POST /api/users/register
     * 
     * @param registrationRequest the registration request with user details
     * @return ResponseEntity with registration response
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequestDTO registrationRequest) {
        try {
            RegistrationResponseDTO response = userService.registerUser(registrationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Error: " + e.getMessage());
        } catch (PasswordMismatchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Health check endpoint
     * GET /api/users/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User Service is running");
    }
    
    /**
     * Get user by email
     * GET /api/users/email?email=user@example.com
     * 
     * @param email the user email
     * @return ResponseEntity with user details
     */
    @GetMapping("/email")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        try {
            User user = userService.findByEmail(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Get user by ID
     * GET /api/users/{id}
     * 
     * @param id the user ID
     * @return ResponseEntity with user details
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Update user profile (full update)
     * PUT /api/users/{id}
     * 
     * @param id the user ID
     * @param updateRequest the update request with new user details
     * @return ResponseEntity with updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDTO updateRequest) {
        try {
            User updatedUser = userService.updateUser(id, updateRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Partial update user profile (update specific fields)
     * PATCH /api/users/{id}
     * 
     * @param id the user ID
     * @param updateRequest the update request with fields to update
     * @return ResponseEntity with updated user
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateUser(@PathVariable Long id, @RequestBody UpdateUserRequestDTO updateRequest) {
        try {
            User updatedUser = userService.updateUserPartial(id, updateRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        }
    }
}
  
