package spring.jpa.centre_formation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.jpa.centre_formation.entity.Etudiant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Etudiant
 */
@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    
    Optional<Etudiant> findByEmail(String email);

    Optional<Etudiant> findByMatricule(String matricule);
    
    boolean existsByMatricule(String matricule);
    
    List<Etudiant> findByDateInscriptionBetween(LocalDate dateDebut, LocalDate dateFin);
    List<Etudiant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

    
    @Query("SELECT e FROM Etudiant e JOIN e.groupes g WHERE g.id = :groupeId")
    List<Etudiant> findByGroupeId(@Param("groupeId") Long groupeId);
    
    @Query("SELECT e FROM Etudiant e JOIN e.inscriptions i WHERE i.cours.id = :coursId AND i.active = true")
    List<Etudiant> findByCoursId(@Param("coursId") Long coursId);
    
    @Query("SELECT AVG(n.valeur) FROM Note n WHERE n.etudiant.id = :etudiantId")
    Double calculerMoyenneGenerale(@Param("etudiantId") Long etudiantId);
    
    @Query("SELECT e FROM Etudiant e WHERE LOWER(e.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(e.prenom) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(e.matricule) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Etudiant> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT COUNT(e) FROM Etudiant e WHERE e.active = true")
    Long countActiveEtudiants();
}

