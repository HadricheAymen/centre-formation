package spring.jpa.centre_formation.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.jpa.centre_formation.entity.Formateur;
import spring.jpa.centre_formation.service.FormateurService;
import spring.jpa.centre_formation.service.SpecialiteService;

/**
 * Contrôleur Web pour la gestion des formateurs
 */
@Controller
@RequestMapping("/admin/formateurs")
@PreAuthorize("hasRole('ADMIN')")
public class FormateurWebController {
    
    private final FormateurService formateurService;
    private final SpecialiteService specialiteService;
    
    @Autowired
    public FormateurWebController(FormateurService formateurService, SpecialiteService specialiteService) {
        this.formateurService = formateurService;
        this.specialiteService = specialiteService;
    }
    
    @GetMapping
    public String listFormateurs(Model model) {
        model.addAttribute("formateurs", formateurService.getAllFormateurs());
        return "admin/formateurs/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("formateur", new Formateur());
        model.addAttribute("specialites", specialiteService.getAllSpecialites());
        return "admin/formateurs/form";
    }
    
    @PostMapping
    public String createFormateur(@ModelAttribute Formateur formateur, RedirectAttributes redirectAttributes) {
        try {
            // Définir le rôle FORMATEUR et activer le compte
            formateur.setRole(spring.jpa.centre_formation.entity.Role.FORMATEUR);
            if (formateur.getActive() == null) {
                formateur.setActive(true);
            }
            formateurService.creerFormateur(formateur);
            redirectAttributes.addFlashAttribute("success", "Formateur créé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création : " + e.getMessage());
        }
        return "redirect:/admin/formateurs";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("formateur", formateurService.getFormateurById(id));
        model.addAttribute("specialites", specialiteService.getAllSpecialites());
        return "admin/formateurs/form";
    }
    
    @PostMapping("/{id}")
    public String updateFormateur(@PathVariable Long id, @ModelAttribute Formateur formateur,
                                RedirectAttributes redirectAttributes) {
        try {
            // S'assurer que le rôle est FORMATEUR
            formateur.setRole(spring.jpa.centre_formation.entity.Role.FORMATEUR);
            formateurService.updateFormateur(id, formateur);
            redirectAttributes.addFlashAttribute("success", "Formateur mis à jour avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
        }
        return "redirect:/admin/formateurs";
    }
    
    @GetMapping("/{id}/delete")
    public String deleteFormateur(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            formateurService.deleteFormateur(id);
            redirectAttributes.addFlashAttribute("success", "Formateur supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/formateurs";
    }

    @GetMapping("/{id}/toggle-active")
    public String toggleActive(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Formateur formateur = formateurService.toggleActive(id);
            String statut = formateur.getActive() ? "activé" : "désactivé";
            redirectAttributes.addFlashAttribute("success", "Formateur " + statut + " avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors du changement de statut : " + e.getMessage());
        }
        return "redirect:/admin/formateurs";
    }

    @GetMapping("/{id}")
    public String viewFormateur(@PathVariable Long id, Model model) {
        Formateur formateur = formateurService.getFormateurById(id);
        model.addAttribute("formateur", formateur);
        model.addAttribute("nombreCours", formateurService.countCours(id));
        return "admin/formateurs/view";
    }
}

