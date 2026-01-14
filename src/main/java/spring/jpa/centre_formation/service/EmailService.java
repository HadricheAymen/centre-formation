package spring.jpa.centre_formation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import spring.jpa.centre_formation.entity.Cours;
import spring.jpa.centre_formation.entity.Etudiant;
import spring.jpa.centre_formation.entity.Formateur;
import spring.jpa.centre_formation.entity.Inscription;

/**
 * Service pour l'envoi d'emails
 */
@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    private final JavaMailSender mailSender;
    
    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    /**
     * Envoyer un email de confirmation d'inscription à un étudiant
     */
    @Async
    public void envoyerEmailInscription(Inscription inscription) {
        try {
            Etudiant etudiant = inscription.getEtudiant();
            Cours cours = inscription.getCours();
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(etudiant.getEmail());
            message.setSubject("Confirmation d'inscription - " + cours.getTitre());
            message.setText(
                "Bonjour " + etudiant.getPrenom() + " " + etudiant.getNom() + ",\n\n" +
                "Votre inscription au cours \"" + cours.getTitre() + "\" a été confirmée avec succès.\n\n" +
                "Détails du cours :\n" +
                "- Code : " + cours.getCode() + "\n" +
                "- Titre : " + cours.getTitre() + "\n" +
                "- Description : " + cours.getDescription() + "\n" +
                "- Formateur : " + (cours.getFormateur() != null ? 
                    cours.getFormateur().getPrenom() + " " + cours.getFormateur().getNom() : "Non assigné") + "\n" +
                "- Nombre d'heures : " + cours.getNombreHeures() + "h\n\n" +
                "Date d'inscription : " + inscription.getDateInscription() + "\n\n" +
                "Cordialement,\n" +
                "L'équipe du Centre de Formation"
            );
            
            mailSender.send(message);
            logger.info("Email d'inscription envoyé à {} pour le cours {}", etudiant.getEmail(), cours.getCode());
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email d'inscription : {}", e.getMessage());
            // Ne pas propager l'exception pour ne pas bloquer l'inscription
        }
    }
    
    /**
     * Envoyer une notification au formateur lors d'une nouvelle inscription
     */
    @Async
    public void notifierFormateurNouvelleInscription(Inscription inscription) {
        try {
            Cours cours = inscription.getCours();
            Formateur formateur = cours.getFormateur();
            
            if (formateur == null) {
                logger.warn("Pas de formateur assigné au cours {}, notification non envoyée", cours.getCode());
                return;
            }
            
            Etudiant etudiant = inscription.getEtudiant();
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(formateur.getEmail());
            message.setSubject("Nouvelle inscription - " + cours.getTitre());
            message.setText(
                "Bonjour " + formateur.getPrenom() + " " + formateur.getNom() + ",\n\n" +
                "Un nouvel étudiant s'est inscrit à votre cours \"" + cours.getTitre() + "\".\n\n" +
                "Informations de l'étudiant :\n" +
                "- Nom : " + etudiant.getNom() + "\n" +
                "- Prénom : " + etudiant.getPrenom() + "\n" +
                "- Matricule : " + etudiant.getMatricule() + "\n" +
                "- Email : " + etudiant.getEmail() + "\n\n" +
                "Date d'inscription : " + inscription.getDateInscription() + "\n\n" +
                "Cordialement,\n" +
                "L'équipe du Centre de Formation"
            );
            
            mailSender.send(message);
            logger.info("Notification envoyée au formateur {} pour nouvelle inscription au cours {}", 
                formateur.getEmail(), cours.getCode());
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de la notification au formateur : {}", e.getMessage());
            // Ne pas propager l'exception
        }
    }
    
    /**
     * Envoyer une notification au formateur lors d'une désinscription
     */
    @Async
    public void notifierFormateurDesinscription(Inscription inscription) {
        try {
            Cours cours = inscription.getCours();
            Formateur formateur = cours.getFormateur();
            
            if (formateur == null) {
                logger.warn("Pas de formateur assigné au cours {}, notification non envoyée", cours.getCode());
                return;
            }
            
            Etudiant etudiant = inscription.getEtudiant();
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(formateur.getEmail());
            message.setSubject("Désinscription - " + cours.getTitre());
            message.setText(
                "Bonjour " + formateur.getPrenom() + " " + formateur.getNom() + ",\n\n" +
                "Un étudiant s'est désinscrit de votre cours \"" + cours.getTitre() + "\".\n\n" +
                "Informations de l'étudiant :\n" +
                "- Nom : " + etudiant.getNom() + "\n" +
                "- Prénom : " + etudiant.getPrenom() + "\n" +
                "- Matricule : " + etudiant.getMatricule() + "\n" +
                "- Email : " + etudiant.getEmail() + "\n\n" +
                "Cordialement,\n" +
                "L'équipe du Centre de Formation"
            );
            
            mailSender.send(message);
            logger.info("Notification de désinscription envoyée au formateur {} pour le cours {}", 
                formateur.getEmail(), cours.getCode());
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de la notification de désinscription : {}", e.getMessage());
            // Ne pas propager l'exception
        }
    }
    
    /**
     * Envoyer un email générique
     */
    @Async
    public void envoyerEmail(String destinataire, String sujet, String contenu) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destinataire);
            message.setSubject(sujet);
            message.setText(contenu);
            
            mailSender.send(message);
            logger.info("Email envoyé à {} avec le sujet : {}", destinataire, sujet);
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email : {}", e.getMessage());
        }
    }
}

