package spring.jpa.centre_formation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant une spécialité (Informatique, Réseaux, IA, etc.)
 */
@Entity
@Table(name = "specialites")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Specialite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(nullable = false, length = 200)
    private String nom;

    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "specialite", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"specialite", "cours"})
    private List<Formateur> formateurs = new ArrayList<>();

    @OneToMany(mappedBy = "specialite", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"specialite", "inscriptions", "notes", "seances", "groupes"})
    private List<Cours> cours = new ArrayList<>();
    
    // Constructeurs
    public Specialite() {
    }
    
    public Specialite(String code, String nom, String description) {
        this.code = code;
        this.nom = nom;
        this.description = description;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<Formateur> getFormateurs() {
        return formateurs;
    }
    
    public void setFormateurs(List<Formateur> formateurs) {
        this.formateurs = formateurs;
    }
    
    public List<Cours> getCours() {
        return cours;
    }
    
    public void setCours(List<Cours> cours) {
        this.cours = cours;
    }
}

