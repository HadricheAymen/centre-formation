package spring.jpa.centre_formation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.jpa.centre_formation.entity.Cours;
import spring.jpa.centre_formation.entity.Etudiant;
import spring.jpa.centre_formation.entity.Note;
import spring.jpa.centre_formation.exception.BusinessException;
import spring.jpa.centre_formation.exception.ResourceNotFoundException;
import spring.jpa.centre_formation.repository.CoursRepository;
import spring.jpa.centre_formation.repository.EtudiantRepository;
import spring.jpa.centre_formation.repository.InscriptionRepository;
import spring.jpa.centre_formation.repository.NoteRepository;

import java.util.List;

/**
 * Service métier pour la gestion des notes
 */
@Service
@Transactional
public class NoteService {
    
    private final NoteRepository noteRepository;
    private final EtudiantRepository etudiantRepository;
    private final CoursRepository coursRepository;
    private final InscriptionRepository inscriptionRepository;
    
    @Autowired
    public NoteService(NoteRepository noteRepository,
                      EtudiantRepository etudiantRepository,
                      CoursRepository coursRepository,
                      InscriptionRepository inscriptionRepository) {
        this.noteRepository = noteRepository;
        this.etudiantRepository = etudiantRepository;
        this.coursRepository = coursRepository;
        this.inscriptionRepository = inscriptionRepository;
    }
    
    /**
     * Créer une nouvelle note
     */
    public Note creerNote(Note note) {
        // Vérifier que l'étudiant existe
        Etudiant etudiant = etudiantRepository.findById(note.getEtudiant().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Etudiant", "id", note.getEtudiant().getId()));
        
        // Vérifier que le cours existe
        Cours cours = coursRepository.findById(note.getCours().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cours", "id", note.getCours().getId()));
        
        // Vérifier que l'étudiant est inscrit au cours
        if (!inscriptionRepository.existsByEtudiantIdAndCoursIdAndActive(
                etudiant.getId(), cours.getId(), true)) {
            throw new BusinessException("L'étudiant n'est pas inscrit à ce cours");
        }
        
        // Vérifier que la note est valide (entre 0 et 20)
        if (note.getValeur() < 0 || note.getValeur() > 20) {
            throw new BusinessException("La note doit être comprise entre 0 et 20");
        }
        
        return noteRepository.save(note);
    }
    
    /**
     * Récupérer toutes les notes
     */
    @Transactional(readOnly = true)
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }
    
    /**
     * Récupérer une note par son ID
     */
    @Transactional(readOnly = true)
    public Note getNoteById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", id));
    }
    
    /**
     * Récupérer les notes d'un étudiant
     */
    @Transactional(readOnly = true)
    public List<Note> getNotesByEtudiant(Long etudiantId) {
        return noteRepository.findByEtudiantId(etudiantId);
    }
    
    /**
     * Récupérer les notes d'un cours
     */
    @Transactional(readOnly = true)
    public List<Note> getNotesByCours(Long coursId) {
        return noteRepository.findByCoursId(coursId);
    }
    
    /**
     * Récupérer les notes d'un étudiant pour un cours
     */
    @Transactional(readOnly = true)
    public List<Note> getNotesByEtudiantAndCours(Long etudiantId, Long coursId) {
        return noteRepository.findByEtudiantIdAndCoursId(etudiantId, coursId);
    }
    
    /**
     * Récupérer les notes d'un formateur
     */
    @Transactional(readOnly = true)
    public List<Note> getNotesByFormateur(Long formateurId) {
        return noteRepository.findByFormateurId(formateurId);
    }
    
    /**
     * Mettre à jour une note
     */
    public Note updateNote(Long id, Note noteDetails) {
        Note note = getNoteById(id);
        
        // Vérifier que la note est valide
        if (noteDetails.getValeur() < 0 || noteDetails.getValeur() > 20) {
            throw new BusinessException("La note doit être comprise entre 0 et 20");
        }
        
        note.setValeur(noteDetails.getValeur());
        note.setTypeEvaluation(noteDetails.getTypeEvaluation());
        note.setDateEvaluation(noteDetails.getDateEvaluation());
        note.setCommentaire(noteDetails.getCommentaire());
        
        return noteRepository.save(note);
    }
    
    /**
     * Supprimer une note
     */
    public void deleteNote(Long id) {
        Note note = getNoteById(id);
        noteRepository.delete(note);
    }
    
    /**
     * Calculer la moyenne générale d'un étudiant
     */
    @Transactional(readOnly = true)
    public Double calculerMoyenneEtudiant(Long etudiantId) {
        Double moyenne = noteRepository.calculerMoyenneEtudiant(etudiantId);
        return moyenne != null ? moyenne : 0.0;
    }
    
    /**
     * Calculer la moyenne d'un étudiant pour un cours
     */
    @Transactional(readOnly = true)
    public Double calculerMoyenneEtudiantPourCours(Long etudiantId, Long coursId) {
        Double moyenne = noteRepository.calculerMoyenneEtudiantPourCours(etudiantId, coursId);
        return moyenne != null ? moyenne : 0.0;
    }
    
    /**
     * Calculer la moyenne d'un cours
     */
    @Transactional(readOnly = true)
    public Double calculerMoyenneCours(Long coursId) {
        Double moyenne = noteRepository.calculerMoyenneCours(coursId);
        return moyenne != null ? moyenne : 0.0;
    }
}

