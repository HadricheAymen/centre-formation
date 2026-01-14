package spring.jpa.centre_formation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.jpa.centre_formation.entity.Formateur;

import java.util.List;

/**
 * Repository pour l'entité Formateur
 */
@Repository
public interface FormateurRepository extends JpaRepository<Formateur, Long> {
    
    List<Formateur> findBySpecialiteId(Long specialiteId);
    
    @Query("SELECT f FROM Formateur f WHERE f.specialite.code = :codeSpecialite")
    List<Formateur> findBySpecialiteCode(@Param("codeSpecialite") String codeSpecialite);
    
    @Query("SELECT f FROM Formateur f WHERE LOWER(f.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(f.prenom) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Formateur> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT COUNT(c) FROM Cours c WHERE c.formateur.id = :formateurId")
    Long countCoursByFormateurId(@Param("formateurId") Long formateurId);
}

