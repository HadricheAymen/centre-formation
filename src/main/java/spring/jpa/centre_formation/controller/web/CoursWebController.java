package spring.jpa.centre_formation.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.jpa.centre_formation.entity.Cours;
import spring.jpa.centre_formation.service.CoursService;
import spring.jpa.centre_formation.service.FormateurService;
import spring.jpa.centre_formation.service.SpecialiteService;
import spring.jpa.centre_formation.service.SessionPedagogiqueService;

/**
 * Contrôleur Web pour la gestion des cours
 */
@Controller
@RequestMapping("/admin/cours")
@PreAuthorize("hasRole('ADMIN')")
public class CoursWebController {

    private final CoursService coursService;
    private final FormateurService formateurService;
    private final SpecialiteService specialiteService;
    private final SessionPedagogiqueService sessionService;

    @Autowired
    public CoursWebController(CoursService coursService, FormateurService formateurService,
                             SpecialiteService specialiteService, SessionPedagogiqueService sessionService) {
        this.coursService = coursService;
        this.formateurService = formateurService;
        this.specialiteService = specialiteService;
        this.sessionService = sessionService;
    }

    @GetMapping
    public String listCours(Model model) {
        model.addAttribute("cours", coursService.getAllCours());
        return "admin/cours/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("cours", new Cours());
        model.addAttribute("formateurs", formateurService.getAllFormateurs());
        model.addAttribute("specialites", specialiteService.getAllSpecialites());
        model.addAttribute("sessions", sessionService.getAllSessions());
        return "admin/cours/form";
    }
    
    @PostMapping
    public String createCours(@ModelAttribute Cours cours, RedirectAttributes redirectAttributes) {
        try {
            coursService.creerCours(cours);
            redirectAttributes.addFlashAttribute("success", "Cours créé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création : " + e.getMessage());
        }
        return "redirect:/admin/cours";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("cours", coursService.getCoursById(id));
        model.addAttribute("formateurs", formateurService.getAllFormateurs());
        model.addAttribute("specialites", specialiteService.getAllSpecialites());
        model.addAttribute("sessions", sessionService.getAllSessions());
        return "admin/cours/form";
    }
    
    @PostMapping("/{id}")
    public String updateCours(@PathVariable Long id, @ModelAttribute Cours cours, 
                             RedirectAttributes redirectAttributes) {
        try {
            coursService.updateCours(id, cours);
            redirectAttributes.addFlashAttribute("success", "Cours mis à jour avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
        }
        return "redirect:/admin/cours";
    }
    
    @GetMapping("/{id}/delete")
    public String deleteCours(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            coursService.deleteCours(id);
            redirectAttributes.addFlashAttribute("success", "Cours supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/cours";
    }
    
    @GetMapping("/{id}")
    public String viewCours(@PathVariable Long id, Model model) {
        Cours cours = coursService.getCoursById(id);
        model.addAttribute("cours", cours);
        model.addAttribute("nombreInscriptions", coursService.countInscriptions(id));
        model.addAttribute("moyenne", coursService.calculerMoyenneCours(id));
        model.addAttribute("tauxReussite", coursService.calculerTauxReussite(id));
        return "admin/cours/view";
    }
}

