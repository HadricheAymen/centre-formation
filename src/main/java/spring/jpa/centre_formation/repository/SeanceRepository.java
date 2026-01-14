package spring.jpa.centre_formation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.jpa.centre_formation.entity.Seance;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour l'entité Seance
 */
@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
    
    List<Seance> findByCoursId(Long coursId);
    
    List<Seance> findByCoursIdAndAnnulee(Long coursId, Boolean annulee);
    
    List<Seance> findBySalle(String salle);
    
    @Query("SELECT s FROM Seance s WHERE s.dateDebut >= :dateDebut AND s.dateFin <= :dateFin")
    List<Seance> findByPeriode(@Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin);
    
    @Query("SELECT s FROM Seance s WHERE s.cours.formateur.id = :formateurId " +
           "AND s.dateDebut >= :dateDebut AND s.dateFin <= :dateFin AND s.annulee = false")
    List<Seance> findEmploiDuTempsFormateur(@Param("formateurId") Long formateurId, 
                                             @Param("dateDebut") LocalDateTime dateDebut, 
                                             @Param("dateFin") LocalDateTime dateFin);
    
    @Query("SELECT s FROM Seance s JOIN s.cours c JOIN c.inscriptions i " +
           "WHERE i.etudiant.id = :etudiantId AND i.active = true " +
           "AND s.dateDebut >= :dateDebut AND s.dateFin <= :dateFin AND s.annulee = false")
    List<Seance> findEmploiDuTempsEtudiant(@Param("etudiantId") Long etudiantId, 
                                            @Param("dateDebut") LocalDateTime dateDebut, 
                                            @Param("dateFin") LocalDateTime dateFin);
    
    @Query("SELECT s FROM Seance s WHERE s.salle = :salle " +
           "AND s.annulee = false " +
           "AND ((s.dateDebut <= :dateDebut AND s.dateFin > :dateDebut) " +
           "OR (s.dateDebut < :dateFin AND s.dateFin >= :dateFin) " +
           "OR (s.dateDebut >= :dateDebut AND s.dateFin <= :dateFin))")
    List<Seance> findConflitsSalle(@Param("salle") String salle, 
                                    @Param("dateDebut") LocalDateTime dateDebut, 
                                    @Param("dateFin") LocalDateTime dateFin);
    
    @Query("SELECT s FROM Seance s WHERE s.cours.formateur.id = :formateurId " +
           "AND s.annulee = false " +
           "AND ((s.dateDebut <= :dateDebut AND s.dateFin > :dateDebut) " +
           "OR (s.dateDebut < :dateFin AND s.dateFin >= :dateFin) " +
           "OR (s.dateDebut >= :dateDebut AND s.dateFin <= :dateFin))")
    List<Seance> findConflitsFormateur(@Param("formateurId") Long formateurId, 
                                        @Param("dateDebut") LocalDateTime dateDebut, 
                                        @Param("dateFin") LocalDateTime dateFin);
}

