package spring.jpa.centre_formation.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.jpa.centre_formation.entity.Note;
import spring.jpa.centre_formation.service.NoteService;
import spring.jpa.centre_formation.service.EtudiantService;
import spring.jpa.centre_formation.service.CoursService;

/**
 * Contrôleur Web pour la gestion des notes
 */
@Controller
@RequestMapping("/admin/notes")
@PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
public class NoteWebController {
    
    private final NoteService noteService;
    private final EtudiantService etudiantService;
    private final CoursService coursService;
    
    @Autowired
    public NoteWebController(NoteService noteService, 
                            EtudiantService etudiantService,
                            CoursService coursService) {
        this.noteService = noteService;
        this.etudiantService = etudiantService;
        this.coursService = coursService;
    }
    
    @GetMapping
    public String listNotes(Model model) {
        model.addAttribute("notes", noteService.getAllNotes());
        return "admin/notes/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("note", new Note());
        model.addAttribute("etudiants", etudiantService.getAllEtudiants());
        model.addAttribute("cours", coursService.getAllCours());
        return "admin/notes/form";
    }
    
    @PostMapping
    public String createNote(@ModelAttribute Note note, RedirectAttributes redirectAttributes) {
        try {
            noteService.creerNote(note);
            redirectAttributes.addFlashAttribute("success", "Note ajoutée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout : " + e.getMessage());
        }
        return "redirect:/admin/notes";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("note", noteService.getNoteById(id));
        model.addAttribute("etudiants", etudiantService.getAllEtudiants());
        model.addAttribute("cours", coursService.getAllCours());
        return "admin/notes/form";
    }
    
    @PostMapping("/{id}")
    public String updateNote(@PathVariable Long id, @ModelAttribute Note note, 
                            RedirectAttributes redirectAttributes) {
        try {
            noteService.updateNote(id, note);
            redirectAttributes.addFlashAttribute("success", "Note mise à jour avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
        }
        return "redirect:/admin/notes";
    }
    
    @GetMapping("/{id}/delete")
    public String deleteNote(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            noteService.deleteNote(id);
            redirectAttributes.addFlashAttribute("success", "Note supprimée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/notes";
    }
}

