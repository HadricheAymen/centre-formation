package spring.jpa.centre_formation.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.jpa.centre_formation.entity.Note;
import spring.jpa.centre_formation.service.NoteService;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des notes
 */
@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteRestController {
    
    private final NoteService noteService;
    
    @Autowired
    public NoteRestController(NoteService noteService) {
        this.noteService = noteService;
    }
    
    /**
     * Récupérer toutes les notes
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<List<Note>> getAllNotes() {
        List<Note> notes = noteService.getAllNotes();
        return ResponseEntity.ok(notes);
    }
    
    /**
     * Récupérer une note par son ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR', 'ETUDIANT')")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Note note = noteService.getNoteById(id);
        return ResponseEntity.ok(note);
    }
    
    /**
     * Créer une nouvelle note
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<Note> creerNote(@RequestBody Note note) {
        Note nouvelleNote = noteService.creerNote(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleNote);
    }
    
    /**
     * Mettre à jour une note
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note note) {
        Note noteMiseAJour = noteService.updateNote(id, note);
        return ResponseEntity.ok(noteMiseAJour);
    }
    
    /**
     * Supprimer une note
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Récupérer les notes d'un étudiant
     */
    @GetMapping("/etudiant/{etudiantId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR', 'ETUDIANT')")
    public ResponseEntity<List<Note>> getNotesByEtudiant(@PathVariable Long etudiantId) {
        List<Note> notes = noteService.getNotesByEtudiant(etudiantId);
        return ResponseEntity.ok(notes);
    }
    
    /**
     * Récupérer les notes d'un cours
     */
    @GetMapping("/cours/{coursId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<List<Note>> getNotesByCours(@PathVariable Long coursId) {
        List<Note> notes = noteService.getNotesByCours(coursId);
        return ResponseEntity.ok(notes);
    }
    
    /**
     * Récupérer les notes d'un étudiant pour un cours
     */
    @GetMapping("/etudiant/{etudiantId}/cours/{coursId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR', 'ETUDIANT')")
    public ResponseEntity<List<Note>> getNotesByEtudiantAndCours(
            @PathVariable Long etudiantId, @PathVariable Long coursId) {
        List<Note> notes = noteService.getNotesByEtudiantAndCours(etudiantId, coursId);
        return ResponseEntity.ok(notes);
    }
    
    /**
     * Récupérer les notes d'un formateur
     */
    @GetMapping("/formateur/{formateurId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<List<Note>> getNotesByFormateur(@PathVariable Long formateurId) {
        List<Note> notes = noteService.getNotesByFormateur(formateurId);
        return ResponseEntity.ok(notes);
    }
    
    /**
     * Calculer la moyenne générale d'un étudiant
     */
    @GetMapping("/etudiant/{etudiantId}/moyenne")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR', 'ETUDIANT')")
    public ResponseEntity<Double> calculerMoyenneEtudiant(@PathVariable Long etudiantId) {
        Double moyenne = noteService.calculerMoyenneEtudiant(etudiantId);
        return ResponseEntity.ok(moyenne);
    }
    
    /**
     * Calculer la moyenne d'un étudiant pour un cours
     */
    @GetMapping("/etudiant/{etudiantId}/cours/{coursId}/moyenne")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR', 'ETUDIANT')")
    public ResponseEntity<Double> calculerMoyenneEtudiantPourCours(
            @PathVariable Long etudiantId, @PathVariable Long coursId) {
        Double moyenne = noteService.calculerMoyenneEtudiantPourCours(etudiantId, coursId);
        return ResponseEntity.ok(moyenne);
    }
    
    /**
     * Calculer la moyenne d'un cours
     */
    @GetMapping("/cours/{coursId}/moyenne")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<Double> calculerMoyenneCours(@PathVariable Long coursId) {
        Double moyenne = noteService.calculerMoyenneCours(coursId);
        return ResponseEntity.ok(moyenne);
    }
}

