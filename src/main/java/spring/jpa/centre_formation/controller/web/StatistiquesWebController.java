package spring.jpa.centre_formation.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.jpa.centre_formation.service.*;

/**
 * Contrôleur Web pour les statistiques
 */
@Controller
@RequestMapping("/admin/statistiques")
@PreAuthorize("hasRole('ADMIN')")
public class StatistiquesWebController {
    
    private final EtudiantService etudiantService;
    private final CoursService coursService;
    private final InscriptionService inscriptionService;
    private final NoteService noteService;
    
    @Autowired
    public StatistiquesWebController(EtudiantService etudiantService,
                                    CoursService coursService,
                                    InscriptionService inscriptionService,
                                    NoteService noteService) {
        this.etudiantService = etudiantService;
        this.coursService = coursService;
        this.inscriptionService = inscriptionService;
        this.noteService = noteService;
    }
    
    @GetMapping
    public String showStatistiques(Model model) {
        // Statistiques générales
        model.addAttribute("totalEtudiants", etudiantService.getAllEtudiants().size());
        model.addAttribute("totalCours", coursService.getAllCours().size());
        model.addAttribute("totalInscriptions", inscriptionService.getAllInscriptions().size());
        model.addAttribute("totalNotes", noteService.getAllNotes().size());
        
        // Listes pour les graphiques
        model.addAttribute("etudiants", etudiantService.getAllEtudiants());
        model.addAttribute("cours", coursService.getAllCours());
        
        return "admin/statistiques/dashboard";
    }
}

