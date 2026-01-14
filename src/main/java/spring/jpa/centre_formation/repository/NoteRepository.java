package spring.jpa.centre_formation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.jpa.centre_formation.entity.Note;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Note
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    
    List<Note> findByEtudiantId(Long etudiantId);
    
    List<Note> findByCoursId(Long coursId);
    
    List<Note> findByEtudiantIdAndCoursId(Long etudiantId, Long coursId);
    
    Optional<Note> findByEtudiantIdAndCoursIdAndTypeEvaluation(Long etudiantId, Long coursId, String typeEvaluation);
    
    @Query("SELECT AVG(n.valeur) FROM Note n WHERE n.etudiant.id = :etudiantId")
    Double calculerMoyenneEtudiant(@Param("etudiantId") Long etudiantId);
    
    @Query("SELECT AVG(n.valeur) FROM Note n WHERE n.etudiant.id = :etudiantId AND n.cours.id = :coursId")
    Double calculerMoyenneEtudiantPourCours(@Param("etudiantId") Long etudiantId, @Param("coursId") Long coursId);
    
    @Query("SELECT AVG(n.valeur) FROM Note n WHERE n.cours.id = :coursId")
    Double calculerMoyenneCours(@Param("coursId") Long coursId);
    
    @Query("SELECT n FROM Note n WHERE n.cours.formateur.id = :formateurId")
    List<Note> findByFormateurId(@Param("formateurId") Long formateurId);
}

