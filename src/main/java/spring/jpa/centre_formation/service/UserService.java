package spring.jpa.centre_formation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.jpa.centre_formation.entity.User;
import spring.jpa.centre_formation.repository.UserRepository;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));
    }
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email: " + email));
    }
    
    public User saveUser(User user) {
        return userRepository.save(user);
    }
   
    
    public boolean checkPassword(String email, String rawPassword) {
    if (email == null || rawPassword == null) return false;
    String normalizedEmail = email.trim().toLowerCase();
    try {
        User user = getUserByEmail(normalizedEmail);
        System.out.println("Checking password for user: " + user.getPassword());
        if (user == null || !user.getActive()) return false; // reject inactive users
        return passwordEncoder.matches(rawPassword, user.getPassword());
    } catch (RuntimeException e) {
        System.out.println("Error checking password: " + e.getMessage());
        return false;
    }
    }    
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public User createUser(User user, String rawPassword) {
        // Encode password
        user.setPassword(passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }
    
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = getUserByEmail(email);
        
        // Verify old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }
        
        // Update with new encoded password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}