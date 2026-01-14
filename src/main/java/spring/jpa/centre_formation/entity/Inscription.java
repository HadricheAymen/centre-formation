package spring.jpa.centre_formation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité représentant une inscription d'un étudiant à un cours
 */
@Entity
@Table(name = "inscriptions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"etudiant_id", "cours_id"})
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    @JsonIgnoreProperties({"inscriptions", "notes", "groupes"})
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cours_id", nullable = false)
    @JsonIgnoreProperties({"inscriptions", "notes", "seances", "groupes"})
    private Cours cours;
    
    @Column(name = "date_inscription", nullable = false)
    private LocalDateTime dateInscription;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @Column(name = "date_annulation")
    private LocalDateTime dateAnnulation;
    
    @PrePersist
    protected void onCreate() {
        dateInscription = LocalDateTime.now();
    }
    
    // Constructeurs
    public Inscription() {
    }
    
    public Inscription(Etudiant etudiant, Cours cours) {
        this.etudiant = etudiant;
        this.cours = cours;
    }
    
    // Méthode pour annuler l'inscription
    public void annuler() {
        this.active = false;
        this.dateAnnulation = LocalDateTime.now();
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Etudiant getEtudiant() {
        return etudiant;
    }
    
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }
    
    public Cours getCours() {
        return cours;
    }
    
    public void setCours(Cours cours) {
        this.cours = cours;
    }
    
    public LocalDateTime getDateInscription() {
        return dateInscription;
    }
    
    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public LocalDateTime getDateAnnulation() {
        return dateAnnulation;
    }
    
    public void setDateAnnulation(LocalDateTime dateAnnulation) {
        this.dateAnnulation = dateAnnulation;
    }
}

