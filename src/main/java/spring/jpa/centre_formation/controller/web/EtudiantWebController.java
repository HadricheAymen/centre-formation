package spring.jpa.centre_formation.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.jpa.centre_formation.entity.Etudiant;
import spring.jpa.centre_formation.service.EtudiantService;
import spring.jpa.centre_formation.service.GroupeService;

/**
 * Contrôleur Web pour la gestion des étudiants
 */
@Controller
@RequestMapping("/admin/etudiants")
@PreAuthorize("hasRole('ADMIN')")
public class EtudiantWebController {
    
    private final EtudiantService etudiantService;
    private final GroupeService groupeService;
    
    @Autowired
    public EtudiantWebController(EtudiantService etudiantService, GroupeService groupeService) {
        this.etudiantService = etudiantService;
        this.groupeService = groupeService;
    }
    
    @GetMapping
    public String listEtudiants(Model model) {
        model.addAttribute("etudiants", etudiantService.getAllEtudiants());
        return "admin/etudiants/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        model.addAttribute("groupes", groupeService.getAllGroupes());
        return "admin/etudiants/form";
    }
    
    @PostMapping
    public String createEtudiant(@ModelAttribute Etudiant etudiant, RedirectAttributes redirectAttributes) {
        try {
            // Définir le rôle ETUDIANT et activer le compte
            etudiant.setRole(spring.jpa.centre_formation.entity.Role.ETUDIANT);
            if (etudiant.getActive() == null) {
                etudiant.setActive(true);
            }
            etudiantService.creerEtudiant(etudiant);
            redirectAttributes.addFlashAttribute("success", "Étudiant créé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création : " + e.getMessage());
        }
        return "redirect:/admin/etudiants";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("etudiant", etudiantService.getEtudiantById(id));
        model.addAttribute("groupes", groupeService.getAllGroupes());
        return "admin/etudiants/form";
    }
    
    @PostMapping("/{id}")
    public String updateEtudiant(@PathVariable Long id, @ModelAttribute Etudiant etudiant,
                                RedirectAttributes redirectAttributes) {
        try {
            // S'assurer que le rôle est ETUDIANT
            etudiant.setRole(spring.jpa.centre_formation.entity.Role.ETUDIANT);
            etudiantService.updateEtudiant(id, etudiant);
            redirectAttributes.addFlashAttribute("success", "Étudiant mis à jour avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
        }
        return "redirect:/admin/etudiants";
    }
    
    @GetMapping("/{id}/delete")
    public String deleteEtudiant(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            etudiantService.deleteEtudiant(id);
            redirectAttributes.addFlashAttribute("success", "Étudiant supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/etudiants";
    }

    @GetMapping("/{id}/toggle-active")
    public String toggleActive(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Etudiant etudiant = etudiantService.toggleActive(id);
            String statut = etudiant.getActive() ? "activé" : "désactivé";
            redirectAttributes.addFlashAttribute("success", "Étudiant " + statut + " avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors du changement de statut : " + e.getMessage());
        }
        return "redirect:/admin/etudiants";
    }

    @GetMapping("/{id}")
    public String viewEtudiant(@PathVariable Long id, Model model) {
        Etudiant etudiant = etudiantService.getEtudiantById(id);
        model.addAttribute("etudiant", etudiant);
        model.addAttribute("moyenne", etudiantService.calculerMoyenneGenerale(id));
        return "admin/etudiants/view";
    }
}

