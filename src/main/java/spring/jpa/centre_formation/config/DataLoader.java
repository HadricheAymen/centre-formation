package spring.jpa.centre_formation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import spring.jpa.centre_formation.entity.*;
import spring.jpa.centre_formation.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Chargeur de données initiales pour le développement
 */
@Component
public class DataLoader implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final EtudiantRepository etudiantRepository;
    private final FormateurRepository formateurRepository;
    private final SpecialiteRepository specialiteRepository;
    private final GroupeRepository groupeRepository;
    private final SessionPedagogiqueRepository sessionRepository;
    private final CoursRepository coursRepository;
    private final InscriptionRepository inscriptionRepository;
    private final NoteRepository noteRepository;
    private final SeanceRepository seanceRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public DataLoader(UserRepository userRepository, EtudiantRepository etudiantRepository,
                     FormateurRepository formateurRepository, SpecialiteRepository specialiteRepository,
                     GroupeRepository groupeRepository, SessionPedagogiqueRepository sessionRepository,
                     CoursRepository coursRepository, InscriptionRepository inscriptionRepository,
                     NoteRepository noteRepository, SeanceRepository seanceRepository,
                     PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.etudiantRepository = etudiantRepository;
        this.formateurRepository = formateurRepository;
        this.specialiteRepository = specialiteRepository;
        this.groupeRepository = groupeRepository;
        this.sessionRepository = sessionRepository;
        this.coursRepository = coursRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.noteRepository = noteRepository;
        this.seanceRepository = seanceRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Vérifier si les données existent déjà
        if (userRepository.findByEmail("admin@formation.tn").isPresent()) {
            System.out.println("=== Les données existent déjà dans la base de données ===");
            System.out.println("Admin: admin@formation.tn / admin123");
            System.out.println("Formateur 1: formateur1@formation.tn / formateur123");
            System.out.println("Formateur 2: formateur2@formation.tn / formateur123");
            System.out.println("Formateur 3: formateur3@formation.tn / formateur123");
            System.out.println("Etudiant 1: etudiant1@formation.tn / etudiant123");
            System.out.println("Etudiant 2: etudiant2@formation.tn / etudiant123");
            System.out.println("Etudiant 3: etudiant3@formation.tn / etudiant123");
            return;
        }

        // Créer un administrateur
        User admin = new User();
        admin.setEmail("admin@formation.tn");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setNom("Admin");
        admin.setPrenom("System");
        admin.setRole(Role.ADMIN);
        admin.setActive(true);
        userRepository.save(admin);
        
        // Créer des spécialités
        Specialite infoSpecialite = new Specialite();
        infoSpecialite.setCode("INFO");
        infoSpecialite.setNom("Informatique");
        infoSpecialite.setDescription("Spécialité Informatique et Technologies");
        specialiteRepository.save(infoSpecialite);
        
        Specialite gestionSpecialite = new Specialite();
        gestionSpecialite.setCode("GEST");
        gestionSpecialite.setNom("Gestion");
        gestionSpecialite.setDescription("Spécialité Gestion et Management");
        specialiteRepository.save(gestionSpecialite);
        
        // Créer des formateurs
        Formateur formateur1 = new Formateur();
        formateur1.setEmail("formateur1@formation.tn");
        formateur1.setPassword(passwordEncoder.encode("formateur123"));
        formateur1.setNom("Benali");
        formateur1.setPrenom("Ahmed");
        formateur1.setRole(Role.FORMATEUR);
        formateur1.setActive(true);
        formateur1.setTelephone("20123456");
        formateur1.setBiographie("Formateur expérimenté en développement web");
        formateur1.setSpecialite(infoSpecialite);
        formateurRepository.save(formateur1);
        
        Formateur formateur2 = new Formateur();
        formateur2.setEmail("formateur2@formation.tn");
        formateur2.setPassword(passwordEncoder.encode("formateur123"));
        formateur2.setNom("Trabelsi");
        formateur2.setPrenom("Fatma");
        formateur2.setRole(Role.FORMATEUR);
        formateur2.setActive(true);
        formateur2.setTelephone("20234567");
        formateur2.setBiographie("Formatrice spécialisée en bases de données");
        formateur2.setSpecialite(infoSpecialite);
        formateurRepository.save(formateur2);

        Formateur formateur3 = new Formateur();
        formateur3.setEmail("formateur3@formation.tn");
        formateur3.setPassword(passwordEncoder.encode("formateur123"));
        formateur3.setNom("Khelifi");
        formateur3.setPrenom("Karim");
        formateur3.setRole(Role.FORMATEUR);
        formateur3.setActive(true);
        formateur3.setTelephone("20345678");
        formateur3.setBiographie("Formateur expert en gestion et management d'entreprise");
        formateur3.setSpecialite(gestionSpecialite);
        formateurRepository.save(formateur3);

        // Créer des groupes
        Groupe groupe1 = new Groupe();
        groupe1.setCode("G1-2024");
        groupe1.setNom("Groupe 1 - Promotion 2024");
        groupe1.setCapaciteMax(30);
        groupeRepository.save(groupe1);
        
        Groupe groupe2 = new Groupe();
        groupe2.setCode("G2-2024");
        groupe2.setNom("Groupe 2 - Promotion 2024");
        groupe2.setCapaciteMax(30);
        groupeRepository.save(groupe2);
        
        // Créer une session pédagogique
        SessionPedagogique session = new SessionPedagogique();
        session.setCode("S1-2024-2025");
        session.setNom("Semestre 1 - Année 2024/2025");
        session.setDateDebut(LocalDate.of(2024, 9, 1));
        session.setDateFin(LocalDate.of(2025, 1, 31));
        session.setActive(true);
        sessionRepository.save(session);
        
        // Créer des étudiants
        Etudiant etudiant1 = new Etudiant();
        etudiant1.setEmail("etudiant1@formation.tn");
        etudiant1.setPassword(passwordEncoder.encode("etudiant123"));
        etudiant1.setNom("Gharbi");
        etudiant1.setPrenom("Mohamed");
        etudiant1.setRole(Role.ETUDIANT);
        etudiant1.setActive(true);
        etudiant1.setMatricule("ETU001");
        etudiant1.setDateInscription(LocalDate.of(2024, 9, 1));
        etudiant1.setDateNaissance(LocalDate.of(2002, 5, 15));
        etudiant1.setAdresse("Tunis, Tunisie");
        etudiant1.setTelephone("98123456");
        etudiant1.setGroupes(Arrays.asList(groupe1));
        etudiantRepository.save(etudiant1);

        Etudiant etudiant2 = new Etudiant();
        etudiant2.setEmail("etudiant2@formation.tn");
        etudiant2.setPassword(passwordEncoder.encode("etudiant123"));
        etudiant2.setNom("Sassi");
        etudiant2.setPrenom("Amira");
        etudiant2.setRole(Role.ETUDIANT);
        etudiant2.setActive(true);
        etudiant2.setMatricule("ETU002");
        etudiant2.setDateInscription(LocalDate.of(2024, 9, 1));
        etudiant2.setDateNaissance(LocalDate.of(2003, 3, 20));
        etudiant2.setAdresse("Sfax, Tunisie");
        etudiant2.setTelephone("98234567");
        etudiant2.setGroupes(Arrays.asList(groupe1));
        etudiantRepository.save(etudiant2);

        Etudiant etudiant3 = new Etudiant();
        etudiant3.setEmail("etudiant3@formation.tn");
        etudiant3.setPassword(passwordEncoder.encode("etudiant123"));
        etudiant3.setNom("Jebali");
        etudiant3.setPrenom("Youssef");
        etudiant3.setRole(Role.ETUDIANT);
        etudiant3.setActive(true);
        etudiant3.setMatricule("ETU003");
        etudiant3.setDateInscription(LocalDate.of(2024, 9, 1));
        etudiant3.setDateNaissance(LocalDate.of(2002, 11, 10));
        etudiant3.setAdresse("Sousse, Tunisie");
        etudiant3.setTelephone("98345678");
        etudiant3.setGroupes(Arrays.asList(groupe2));
        etudiantRepository.save(etudiant3);

        Etudiant etudiant4 = new Etudiant();
        etudiant4.setEmail("aymenhadriche@gmail.com");
        etudiant4.setPassword(passwordEncoder.encode("etudiant123"));
        etudiant4.setNom("Hadriche");
        etudiant4.setPrenom("Aymen");
        etudiant4.setRole(Role.ETUDIANT);
        etudiant4.setActive(true);
        etudiant4.setMatricule("ETU004");
        etudiant4.setDateInscription(LocalDate.of(2024, 9, 1));
        etudiant4.setDateNaissance(LocalDate.of(2003, 7, 25));
        etudiant4.setAdresse("Gabes, Tunisie");
        etudiant4.setTelephone("98456789");
        etudiant4.setGroupes(Arrays.asList(groupe2));
        etudiantRepository.save(etudiant4);
        
        // Créer des cours
        Cours cours1 = new Cours();
        cours1.setCode("SPRING-101");
        cours1.setTitre("Framework Spring Boot");
        cours1.setDescription("Introduction au développement avec Spring Boot");
        cours1.setNombreHeures(40);
        cours1.setCoefficient(3.0);
        cours1.setFormateur(formateur1);
        cours1.setSpecialite(infoSpecialite);
        cours1.setSession(session);
        cours1.getGroupes().add(groupe1);
        cours1.getGroupes().add(groupe2);
        coursRepository.save(cours1);
        
        Cours cours2 = new Cours();
        cours2.setCode("DB-201");
        cours2.setTitre("Bases de Données Avancées");
        cours2.setDescription("Conception et optimisation de bases de données");
        cours2.setNombreHeures(35);
        cours2.setCoefficient(2.5);
        cours2.setFormateur(formateur2);
        cours2.setSpecialite(infoSpecialite);
        cours2.setSession(session);
        cours2.getGroupes().add(groupe1);
        coursRepository.save(cours2);
        
        Cours cours3 = new Cours();
        cours3.setCode("WEB-301");
        cours3.setTitre("Développement Web Full Stack");
        cours3.setDescription("Développement d'applications web modernes");
        cours3.setNombreHeures(50);
        cours3.setCoefficient(4.0);
        cours3.setFormateur(formateur1);
        cours3.setSpecialite(infoSpecialite);
        cours3.setSession(session);
        cours3.getGroupes().add(groupe2);
        coursRepository.save(cours3);

        Cours cours4 = new Cours();
        cours4.setCode("MGMT-101");
        cours4.setTitre("Principes de Management");
        cours4.setDescription("Introduction aux concepts de management");
        cours4.setNombreHeures(30);
        cours4.setCoefficient(2.0);
        cours4.setFormateur(formateur3);
        cours4.setSpecialite(gestionSpecialite);
        cours4.setSession(session);
        cours4.getGroupes().add(groupe2);
        coursRepository.save(cours4);

        Cours cours5 = new Cours();
        cours5.setCode("SALES-101");
        cours5.setTitre("Ventes et Marketing");
        cours5.setDescription("Stratégies de ventes et de marketing");
        cours5.setNombreHeures(40);
        cours5.setCoefficient(3.0);
        cours5.setFormateur(formateur3);
        cours5.setSpecialite(gestionSpecialite);
        cours5.setSession(session);
        cours5.getGroupes().add(groupe1);
        coursRepository.save(cours5);

        Cours cours6 = new Cours();
        cours6.setCode("JEE-101");
        cours6.setTitre("Développement JEE");
        cours6.setDescription("Développement d'applications Java Enterprise Edition");
        cours6.setNombreHeures(45);
        cours6.setCoefficient(3.5);
        cours6.setFormateur(formateur2);
        cours6.setSpecialite(infoSpecialite);
        cours6.setSession(session);
        cours6.getGroupes().add(groupe2);
        coursRepository.save(cours6);

        Cours cours7 = new Cours();
        cours7.setCode("DEVOPS-101");
        cours7.setTitre("Introduction au DevOps");
        cours7.setDescription("Introduction aux principes de DevOps");
        cours7.setNombreHeures(30);
        cours7.setCoefficient(2.0);
        cours7.setFormateur(formateur1);
        cours7.setSpecialite(infoSpecialite);
        cours7.setSession(session);
        cours7.getGroupes().add(groupe1);
        coursRepository.save(cours7);

        // Créer des inscriptions
        Inscription inscription1 = new Inscription();
        inscription1.setEtudiant(etudiant1);
        inscription1.setCours(cours1);
        inscription1.setDateInscription(LocalDateTime.of(2024, 9, 5, 10, 0));
        inscription1.setActive(true);
        inscriptionRepository.save(inscription1);

        Inscription inscription2 = new Inscription();
        inscription2.setEtudiant(etudiant1);
        inscription2.setCours(cours2);
        inscription2.setDateInscription(LocalDateTime.of(2024, 9, 5, 10, 0));
        inscription2.setActive(true);
        inscriptionRepository.save(inscription2);

        Inscription inscription3 = new Inscription();
        inscription3.setEtudiant(etudiant2);
        inscription3.setCours(cours1);
        inscription3.setDateInscription(LocalDateTime.of(2024, 9, 5, 10, 0));
        inscription3.setActive(true);
        inscriptionRepository.save(inscription3);

        Inscription inscription4 = new Inscription();
        inscription4.setEtudiant(etudiant2);
        inscription4.setCours(cours2);
        inscription4.setDateInscription(LocalDateTime.of(2024, 9, 5, 10, 0));
        inscription4.setActive(true);
        inscriptionRepository.save(inscription4);

        Inscription inscription5 = new Inscription();
        inscription5.setEtudiant(etudiant3);
        inscription5.setCours(cours1);
        inscription5.setDateInscription(LocalDateTime.of(2024, 9, 5, 10, 0));
        inscription5.setActive(true);
        inscriptionRepository.save(inscription5);

        Inscription inscription6 = new Inscription();
        inscription6.setEtudiant(etudiant3);
        inscription6.setCours(cours3);
        inscription6.setDateInscription(LocalDateTime.of(2024, 9, 5, 10, 0));
        inscription6.setActive(true);
        inscriptionRepository.save(inscription6);
        
        // Créer des notes
        Note note1 = new Note();
        note1.setEtudiant(etudiant1);
        note1.setCours(cours1);
        note1.setValeur(16.5);
        note1.setTypeEvaluation("Examen");
        note1.setDateEvaluation(LocalDateTime.of(2024, 11, 15, 9, 0));
        note1.setCommentaire("Très bon travail");
        noteRepository.save(note1);

        Note note2 = new Note();
        note2.setEtudiant(etudiant1);
        note2.setCours(cours2);
        note2.setValeur(14.0);
        note2.setTypeEvaluation("Projet");
        note2.setDateEvaluation(LocalDateTime.of(2024, 11, 20, 14, 0));
        note2.setCommentaire("Bon projet");
        noteRepository.save(note2);

        Note note3 = new Note();
        note3.setEtudiant(etudiant2);
        note3.setCours(cours1);
        note3.setValeur(15.0);
        note3.setTypeEvaluation("Examen");
        note3.setDateEvaluation(LocalDateTime.of(2024, 11, 15, 9, 0));
        note3.setCommentaire("Bien");
        noteRepository.save(note3);
        
        // Créer des séances
        Seance seance1 = new Seance();
        seance1.setCours(cours1);
        seance1.setDateDebut(LocalDateTime.of(2024, 11, 25, 9, 0));
        seance1.setDateFin(LocalDateTime.of(2024, 11, 25, 12, 0));
        seance1.setSalle("A101");
        seance1.setType("Cours");
        seance1.setDescription("Introduction à Spring Boot");
        seance1.setAnnulee(false);
        seanceRepository.save(seance1);
        
        Seance seance2 = new Seance();
        seance2.setCours(cours1);
        seance2.setDateDebut(LocalDateTime.of(2024, 11, 26, 14, 0));
        seance2.setDateFin(LocalDateTime.of(2024, 11, 26, 17, 0));
        seance2.setSalle("A102");
        seance2.setType("TP");
        seance2.setDescription("Travaux pratiques Spring Boot");
        seance2.setAnnulee(false);
        seanceRepository.save(seance2);
        
        Seance seance3 = new Seance();
        seance3.setCours(cours2);
        seance3.setDateDebut(LocalDateTime.of(2024, 11, 27, 9, 0));
        seance3.setDateFin(LocalDateTime.of(2024, 11, 27, 12, 0));
        seance3.setSalle("B201");
        seance3.setType("Cours");
        seance3.setDescription("Optimisation de requêtes SQL");
        seance3.setAnnulee(false);
        seanceRepository.save(seance3);
        
        System.out.println("=== Données initiales chargées avec succès ===");
        System.out.println("Admin: admin@formation.tn / admin123");
        System.out.println("Formateur 1: formateur1@formation.tn / formateur123");
        System.out.println("Formateur 2: formateur2@formation.tn / formateur123");
        System.out.println("Formateur 3: formateur3@formation.tn / formateur123");
        System.out.println("Etudiant 1: etudiant1@formation.tn / etudiant123");
        System.out.println("Etudiant 2: etudiant2@formation.tn / etudiant123");
        System.out.println("Etudiant 3: etudiant3@formation.tn / etudiant123");
    }
}

