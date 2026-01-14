package spring.jpa.centre_formation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.jpa.centre_formation.entity.Role;
import spring.jpa.centre_formation.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<User> findByRole(Role role);
    
    List<User> findByActive(Boolean active);
    
    List<User> findByRoleAndActive(Role role, Boolean active);

    Optional<User> findByEmailAndPassword(String email, String password);
}

