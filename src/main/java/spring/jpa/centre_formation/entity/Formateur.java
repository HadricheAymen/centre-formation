package spring.jpa.centre_formation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un formateur
 */
@Entity
@Table(name = "formateurs")
@PrimaryKeyJoinColumn(name = "user_id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Formateur extends User {

    @Column(length = 20)
    private String telephone;

    @Column(length = 500)
    private String biographie;

    @ManyToOne
    @JoinColumn(name = "specialite_id")
    @JsonIgnoreProperties({"formateurs", "cours"})
    private Specialite specialite;

    @OneToMany(mappedBy = "formateur", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"formateur", "inscriptions", "notes", "seances", "groupes"})
    private List<Cours> cours = new ArrayList<>();
    
    // Constructeurs
    public Formateur() {
        super();
    }
    
    public Formateur(String email, String password, String nom, String prenom, Specialite specialite) {
        super(email, password, nom, prenom, Role.FORMATEUR);
        this.specialite = specialite;
    }
    
    // Méthodes utilitaires
    public void addCours(Cours cours) {
        this.cours.add(cours);
        cours.setFormateur(this);
    }
    
    public void removeCours(Cours cours) {
        this.cours.remove(cours);
        cours.setFormateur(null);
    }
    
    // Getters et Setters
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getBiographie() {
        return biographie;
    }
    
    public void setBiographie(String biographie) {
        this.biographie = biographie;
    }
    
    public Specialite getSpecialite() {
        return specialite;
    }
    
    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }
    
    public List<Cours> getCours() {
        return cours;
    }
    
    public void setCours(List<Cours> cours) {
        this.cours = cours;
    }
}

