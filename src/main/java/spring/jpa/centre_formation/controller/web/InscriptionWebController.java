package spring.jpa.centre_formation.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.jpa.centre_formation.service.InscriptionService;
import spring.jpa.centre_formation.service.EtudiantService;
import spring.jpa.centre_formation.service.CoursService;

/**
 * Contrôleur Web pour la gestion des inscriptions
 */
@Controller
@RequestMapping("/admin/inscriptions")
@PreAuthorize("hasRole('ADMIN')")
public class InscriptionWebController {
    
    private final InscriptionService inscriptionService;
    private final EtudiantService etudiantService;
    private final CoursService coursService;
    
    @Autowired
    public InscriptionWebController(InscriptionService inscriptionService, 
                                    EtudiantService etudiantService,
                                    CoursService coursService) {
        this.inscriptionService = inscriptionService;
        this.etudiantService = etudiantService;
        this.coursService = coursService;
    }
    
    @GetMapping
    public String listInscriptions(Model model) {
        model.addAttribute("inscriptions", inscriptionService.getAllInscriptions());
        return "admin/inscriptions/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("etudiants", etudiantService.getAllEtudiants());
        model.addAttribute("cours", coursService.getAllCours());
        return "admin/inscriptions/form";
    }
    
    @PostMapping
    public String createInscription(@RequestParam Long etudiantId, 
                                   @RequestParam Long coursId,
                                   RedirectAttributes redirectAttributes) {
        try {
            inscriptionService.inscrireEtudiant(etudiantId, coursId);
            redirectAttributes.addFlashAttribute("success", "Inscription créée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'inscription : " + e.getMessage());
        }
        return "redirect:/admin/inscriptions";
    }
    
    @GetMapping("/{id}/delete")
    public String deleteInscription(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            inscriptionService.annulerInscription(id);
            redirectAttributes.addFlashAttribute("success", "Inscription annulée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'annulation : " + e.getMessage());
        }
        return "redirect:/admin/inscriptions";
    }
}

