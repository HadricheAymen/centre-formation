package spring.jpa.centre_formation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.jpa.centre_formation.entity.Cours;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Cours
 */
@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {
    
    Optional<Cours> findByCode(String code);
    
    boolean existsByCode(String code);
    
    List<Cours> findByFormateurId(Long formateurId);
    
    List<Cours> findBySpecialiteId(Long specialiteId);
    
    List<Cours> findBySessionId(Long sessionId);
    
    @Query("SELECT c FROM Cours c JOIN c.groupes g WHERE g.id = :groupeId")
    List<Cours> findByGroupeId(@Param("groupeId") Long groupeId);
    
    @Query("SELECT c FROM Cours c WHERE LOWER(c.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.code) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Cours> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.cours.id = :coursId AND i.active = true")
    Long countInscriptionsByCoursId(@Param("coursId") Long coursId);
    
    @Query("SELECT AVG(n.valeur) FROM Note n WHERE n.cours.id = :coursId")
    Double calculerMoyenneCours(@Param("coursId") Long coursId);
    
    @Query("SELECT COUNT(n) * 100.0 / COUNT(DISTINCT i.etudiant) " +
           "FROM Inscription i LEFT JOIN Note n ON n.etudiant = i.etudiant AND n.cours = i.cours AND n.valeur >= 10 " +
           "WHERE i.cours.id = :coursId AND i.active = true")
    Double calculerTauxReussite(@Param("coursId") Long coursId);
}

