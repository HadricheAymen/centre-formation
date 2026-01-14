package spring.jpa.centre_formation.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.jpa.centre_formation.entity.*;
import spring.jpa.centre_formation.service.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contrôleur Web pour le Dashboard dynamique
 */
@Controller
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class DashboardWebController {
    
    private final EtudiantService etudiantService;
    private final FormateurService formateurService;
    private final CoursService coursService;
    private final InscriptionService inscriptionService;
    private final NoteService noteService;
    private final SeanceService seanceService;

    @Autowired
    public DashboardWebController(EtudiantService etudiantService,
                                 FormateurService formateurService,
                                 CoursService coursService,
                                 InscriptionService inscriptionService,
                                 NoteService noteService,
                                 SeanceService seanceService) {
        this.etudiantService = etudiantService;
        this.formateurService = formateurService;
        this.coursService = coursService;
        this.inscriptionService = inscriptionService;
        this.noteService = noteService;
        this.seanceService = seanceService;
    }
    
    @GetMapping
    public String showDashboard(Model model) {
        // Statistiques générales
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        List<Formateur> formateurs = formateurService.getAllFormateurs();
        List<Cours> cours = coursService.getAllCours();
        List<Inscription> inscriptions = inscriptionService.getAllInscriptions();
        List<Note> notes = noteService.getAllNotes();
        List<Seance> seances = seanceService.getAllSeances();

        model.addAttribute("totalEtudiants", etudiants.size());
        model.addAttribute("totalFormateurs", formateurs.size());
        model.addAttribute("totalCours", cours.size());
        model.addAttribute("totalInscriptions", inscriptions.size());
        model.addAttribute("totalNotes", notes.size());
        model.addAttribute("totalSeances", seances.size());
        
        // Calcul des statistiques dynamiques
        
        // 1. Répartition des étudiants par cours (pour graphique camembert)
        Map<String, Long> etudiantsParCours = inscriptions.stream()
            .collect(Collectors.groupingBy(
                i -> i.getCours().getTitre(),
                Collectors.counting()
            ));
        model.addAttribute("etudiantsParCours", etudiantsParCours);
        
        // 2. Distribution des notes (pour graphique barres)
        Map<String, Long> distributionNotes = new LinkedHashMap<>();
        distributionNotes.put("0-5", notes.stream().filter(n -> n.getValeur() >= 0 && n.getValeur() < 5).count());
        distributionNotes.put("5-10", notes.stream().filter(n -> n.getValeur() >= 5 && n.getValeur() < 10).count());
        distributionNotes.put("10-12", notes.stream().filter(n -> n.getValeur() >= 10 && n.getValeur() < 12).count());
        distributionNotes.put("12-14", notes.stream().filter(n -> n.getValeur() >= 12 && n.getValeur() < 14).count());
        distributionNotes.put("14-16", notes.stream().filter(n -> n.getValeur() >= 14 && n.getValeur() < 16).count());
        distributionNotes.put("16-18", notes.stream().filter(n -> n.getValeur() >= 16 && n.getValeur() < 18).count());
        distributionNotes.put("18-20", notes.stream().filter(n -> n.getValeur() >= 18 && n.getValeur() <= 20).count());
        model.addAttribute("distributionNotes", distributionNotes);
        
        // 3. Top 10 étudiants (par moyenne des notes)
        Map<Etudiant, Double> moyennesEtudiants = new HashMap<>();
        for (Etudiant etudiant : etudiants) {
            List<Note> notesEtudiant = notes.stream()
                .filter(n -> n.getEtudiant().getId().equals(etudiant.getId()))
                .collect(Collectors.toList());
            if (!notesEtudiant.isEmpty()) {
                double moyenne = notesEtudiant.stream()
                    .mapToDouble(Note::getValeur)
                    .average()
                    .orElse(0.0);
                moyennesEtudiants.put(etudiant, moyenne);
            }
        }
        List<Map.Entry<Etudiant, Double>> topEtudiants = moyennesEtudiants.entrySet().stream()
            .sorted(Map.Entry.<Etudiant, Double>comparingByValue().reversed())
            .limit(10)
            .collect(Collectors.toList());
        model.addAttribute("topEtudiants", topEtudiants);
        
        // 4. Cours populaires (par nombre d'inscriptions)
        List<Cours> coursPopulaires = cours.stream()
            .sorted((c1, c2) -> Integer.compare(c2.getInscriptions().size(), c1.getInscriptions().size()))
            .limit(10)
            .collect(Collectors.toList());
        model.addAttribute("coursPopulaires", coursPopulaires);
        
        // 5. Taux de réussite global (notes >= 10)
        long notesReussies = notes.stream().filter(n -> n.getValeur() >= 10).count();
        double tauxReussite = notes.isEmpty() ? 0 : (notesReussies * 100.0 / notes.size());
        model.addAttribute("tauxReussite", String.format("%.1f", tauxReussite));
        
        // 6. Moyenne générale
        double moyenneGenerale = notes.stream()
            .mapToDouble(Note::getValeur)
            .average()
            .orElse(0.0);
        model.addAttribute("moyenneGenerale", String.format("%.2f", moyenneGenerale));
        
        return "admin/dashboard/index";
    }
}

