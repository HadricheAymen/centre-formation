package spring.jpa.centre_formation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.jpa.centre_formation.entity.SessionPedagogique;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité SessionPedagogique
 */
@Repository
public interface SessionPedagogiqueRepository extends JpaRepository<SessionPedagogique, Long> {
    
    Optional<SessionPedagogique> findByCode(String code);
    
    boolean existsByCode(String code);
    
    List<SessionPedagogique> findByActive(Boolean active);
    
    @Query("SELECT s FROM SessionPedagogique s WHERE :date BETWEEN s.dateDebut AND s.dateFin")
    Optional<SessionPedagogique> findSessionActive(@Param("date") LocalDate date);
    
    List<SessionPedagogique> findByDateDebutBetween(LocalDate dateDebut, LocalDate dateFin);
}

