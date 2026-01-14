package spring.jpa.centre_formation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.jpa.centre_formation.entity.Specialite;

import java.util.Optional;

/**
 * Repository pour l'entité Specialite
 */
@Repository
public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {
    
    Optional<Specialite> findByCode(String code);
    
    boolean existsByCode(String code);
    
    Optional<Specialite> findByNom(String nom);
}

