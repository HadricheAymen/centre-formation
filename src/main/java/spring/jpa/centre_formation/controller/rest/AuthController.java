package spring.jpa.centre_formation.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import spring.jpa.centre_formation.config.JwtUtil;
import spring.jpa.centre_formation.dto.LoginRequest;
import spring.jpa.centre_formation.entity.Etudiant;
import spring.jpa.centre_formation.entity.Role;
import spring.jpa.centre_formation.entity.User;
import spring.jpa.centre_formation.service.EtudiantService;
import spring.jpa.centre_formation.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

     public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Autowired
    private UserService userService;
    
    @Autowired
    private EtudiantService etudiantService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        

        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();
            System.out.println("Logging in user: " + email + " with password: " + password);
            boolean isValid = userService.checkPassword(email, password);
            System.out.println("Is valid: " + isValid);
            if (!isValid) {
                return ResponseEntity.status(401).body(Map.of("error", "Email ou mot de passe incorrect"));
            }
            
            // Get user details
            User user = userService.getUserByEmail(email);
            
            // Create response
            Map<String, Object> response = new HashMap<>();
            System.out.println("Generating token for user: " + user.getEmail() + " with role: " + user.getRole().name());
            String token = jwtUtil.generateToken(user.getEmail(), List.of(user.getRole().name()));
            response.put("token", token);
            response.put("user", Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "nom", user.getNom(),
                "prenom", user.getPrenom(),
                "role", user.getRole().name(),
                "active", user.getActive()
            ));
            
            // Add student-specific data if role is ETUDIANT
            if (user.getRole() == Role.ETUDIANT) {
                try {
                    Etudiant etudiant = etudiantService.getEtudiantById(user.getId());
                    response.put("matricule", etudiant.getMatricule());
                    response.put("dateInscription", etudiant.getDateInscription());
                } catch (Exception e) {
                    // Student data not found, but user exists
                }
            }
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Email ou mot de passe incorrect"));
        }
    }
    

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestParam("email") String email,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword) {
        
        try {
            userService.changePassword(email, oldPassword, newPassword);
            return ResponseEntity.ok(Map.of("message", "Mot de passe modifié avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam("email") String email) {
        try {
            User user = userService.getUserByEmail(email);
            
            Map<String, Object> profile = new HashMap<>();
            profile.put("id", user.getId());
            profile.put("email", user.getEmail());
            profile.put("nom", user.getNom());
            profile.put("prenom", user.getPrenom());
            profile.put("role", user.getRole().name());
            profile.put("active", user.getActive());
            profile.put("createdAt", user.getCreatedAt());
            
            if (user.getRole() == Role.ETUDIANT) {
                Etudiant etudiant = etudiantService.getEtudiantById(user.getId());
                profile.put("matricule", etudiant.getMatricule());
                profile.put("dateInscription", etudiant.getDateInscription());
                profile.put("dateNaissance", etudiant.getDateNaissance());
                profile.put("adresse", etudiant.getAdresse());
                profile.put("telephone", etudiant.getTelephone());
            }
            
            return ResponseEntity.ok(profile);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Utilisateur non trouvé"));
        }
    }
}