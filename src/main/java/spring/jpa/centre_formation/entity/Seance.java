package spring.jpa.centre_formation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité représentant une séance de cours (planning)
 */
@Entity
@Table(name = "seances")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Seance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cours_id", nullable = false)
    @JsonIgnoreProperties({"inscriptions", "notes", "seances", "groupes"})
    private Cours cours;
    
    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;
    
    @Column(name = "date_fin", nullable = false)
    private LocalDateTime dateFin;
    
    @Column(nullable = false, length = 100)
    private String salle;
    
    @Column(length = 50)
    private String type; // Ex: "Cours", "TD", "TP"
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private Boolean annulee = false;
    
    // Constructeurs
    public Seance() {
    }
    
    public Seance(Cours cours, LocalDateTime dateDebut, LocalDateTime dateFin, String salle, String type) {
        this.cours = cours;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.salle = salle;
        this.type = type;
    }
    
    // Méthode pour vérifier le conflit d'horaire
    public boolean conflitAvec(Seance autreSeance) {
        return !this.dateFin.isBefore(autreSeance.dateDebut) && 
               !autreSeance.dateFin.isBefore(this.dateDebut);
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Cours getCours() {
        return cours;
    }
    
    public void setCours(Cours cours) {
        this.cours = cours;
    }
    
    public LocalDateTime getDateDebut() {
        return dateDebut;
    }
    
    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    public LocalDateTime getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }
    
    public String getSalle() {
        return salle;
    }
    
    public void setSalle(String salle) {
        this.salle = salle;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getAnnulee() {
        return annulee;
    }
    
    public void setAnnulee(Boolean annulee) {
        this.annulee = annulee;
    }
}

