package spring.jpa.centre_formation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.jpa.centre_formation.entity.Inscription;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Inscription
 */
@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    
    List<Inscription> findByEtudiantId(Long etudiantId);
    
    List<Inscription> findByCoursId(Long coursId);
    
    List<Inscription> findByEtudiantIdAndActive(Long etudiantId, Boolean active);
    
    List<Inscription> findByCoursIdAndActive(Long coursId, Boolean active);
    
    Optional<Inscription> findByEtudiantIdAndCoursId(Long etudiantId, Long coursId);
    
    @Query("SELECT i FROM Inscription i WHERE i.etudiant.id = :etudiantId AND i.cours.id = :coursId AND i.active = true")
    Optional<Inscription> findActiveInscription(@Param("etudiantId") Long etudiantId, @Param("coursId") Long coursId);
    
    boolean existsByEtudiantIdAndCoursIdAndActive(Long etudiantId, Long coursId, Boolean active);
    
    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.cours.id = :coursId AND i.active = true")
    Long countActiveInscriptionsByCoursId(@Param("coursId") Long coursId);
}

