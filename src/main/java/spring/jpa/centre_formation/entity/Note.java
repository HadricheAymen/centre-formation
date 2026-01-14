package spring.jpa.centre_formation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité représentant une note d'un étudiant pour un cours
 */
@Entity
@Table(name = "notes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"etudiant_id", "cours_id", "type_evaluation"})
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Note {

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
    
    @Column(nullable = false)
    private Double valeur;
    
    @Column(name = "type_evaluation", nullable = false, length = 50)
    private String typeEvaluation; // Ex: "Examen", "TP", "Projet", "CC"
    
    @Column(name = "date_evaluation")
    private LocalDateTime dateEvaluation;
    
    @Column(length = 500)
    private String commentaire;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructeurs
    public Note() {
    }
    
    public Note(Etudiant etudiant, Cours cours, Double valeur, String typeEvaluation) {
        this.etudiant = etudiant;
        this.cours = cours;
        this.valeur = valeur;
        this.typeEvaluation = typeEvaluation;
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
    
    public Double getValeur() {
        return valeur;
    }
    
    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }
    
    public String getTypeEvaluation() {
        return typeEvaluation;
    }
    
    public void setTypeEvaluation(String typeEvaluation) {
        this.typeEvaluation = typeEvaluation;
    }
    
    public LocalDateTime getDateEvaluation() {
        return dateEvaluation;
    }
    
    public void setDateEvaluation(LocalDateTime dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }
    
    public String getCommentaire() {
        return commentaire;
    }
    
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

