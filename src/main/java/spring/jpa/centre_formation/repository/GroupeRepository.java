package spring.jpa.centre_formation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.jpa.centre_formation.entity.Groupe;

import java.util.Optional;

/**
 * Repository pour l'entité Groupe
 */
@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Long> {
    
    Optional<Groupe> findByCode(String code);
    
    boolean existsByCode(String code);
    
    @Query("SELECT COUNT(e) FROM Etudiant e JOIN e.groupes g WHERE g.id = :groupeId")
    Long countEtudiantsByGroupeId(@Param("groupeId") Long groupeId);
}

