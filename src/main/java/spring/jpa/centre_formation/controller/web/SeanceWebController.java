package spring.jpa.centre_formation.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.jpa.centre_formation.entity.Seance;
import spring.jpa.centre_formation.service.SeanceService;
import spring.jpa.centre_formation.service.CoursService;

/**
 * Contrôleur Web pour la gestion des séances (emploi du temps)
 */
@Controller
@RequestMapping("/admin/seances")
@PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
public class SeanceWebController {
    
    private final SeanceService seanceService;
    private final CoursService coursService;
    
    @Autowired
    public SeanceWebController(SeanceService seanceService, CoursService coursService) {
        this.seanceService = seanceService;
        this.coursService = coursService;
    }
    
    @GetMapping
    public String listSeances(Model model) {
        model.addAttribute("seances", seanceService.getAllSeances());
        return "admin/seances/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("seance", new Seance());
        model.addAttribute("cours", coursService.getAllCours());
        return "admin/seances/form";
    }
    
    @PostMapping
    public String createSeance(@ModelAttribute Seance seance, RedirectAttributes redirectAttributes) {
        try {
            seanceService.creerSeance(seance);
            redirectAttributes.addFlashAttribute("success", "Séance créée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création : " + e.getMessage());
        }
        return "redirect:/admin/seances";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("seance", seanceService.getSeanceById(id));
        model.addAttribute("cours", coursService.getAllCours());
        return "admin/seances/form";
    }
    
    @PostMapping("/{id}")
    public String updateSeance(@PathVariable Long id, @ModelAttribute Seance seance, 
                              RedirectAttributes redirectAttributes) {
        try {
            seanceService.updateSeance(id, seance);
            redirectAttributes.addFlashAttribute("success", "Séance mise à jour avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
        }
        return "redirect:/admin/seances";
    }
    
    @GetMapping("/{id}/delete")
    public String deleteSeance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            seanceService.deleteSeance(id);
            redirectAttributes.addFlashAttribute("success", "Séance supprimée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/seances";
    }
}

