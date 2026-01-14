package spring.jpa.centre_formation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un groupe d'étudiants (ex: TP1, TP2)
 */
@Entity
@Table(name = "groupes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Groupe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 200)
    private String nom;

    @Column(name = "capacite_max")
    private Integer capaciteMax;

    @ManyToMany(mappedBy = "groupes")
    @JsonIgnoreProperties({"groupes", "inscriptions", "notes"})
    private List<Etudiant> etudiants = new ArrayList<>();

    @ManyToMany(mappedBy = "groupes")
    @JsonIgnoreProperties({"groupes", "inscriptions", "notes", "seances"})
    private List<Cours> cours = new ArrayList<>();
    
    // Constructeurs
    public Groupe() {
    }
    
    public Groupe(String code, String nom, Integer capaciteMax) {
        this.code = code;
        this.nom = nom;
        this.capaciteMax = capaciteMax;
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
    
    public Integer getCapaciteMax() {
        return capaciteMax;
    }
    
    public void setCapaciteMax(Integer capaciteMax) {
        this.capaciteMax = capaciteMax;
    }
    
    public List<Etudiant> getEtudiants() {
        return etudiants;
    }
    
    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }
    
    public List<Cours> getCours() {
        return cours;
    }
    
    public void setCours(List<Cours> cours) {
        this.cours = cours;
    }
}

