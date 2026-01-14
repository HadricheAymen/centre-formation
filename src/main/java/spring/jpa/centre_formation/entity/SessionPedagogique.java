package spring.jpa.centre_formation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant une session pédagogique (semestre, année scolaire)
 */
@Entity
@Table(name = "sessions_pedagogiques")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SessionPedagogique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 200)
    private String nom;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"session", "inscriptions", "notes", "seances", "groupes"})
    private List<Cours> cours = new ArrayList<>();
    
    // Constructeurs
    public SessionPedagogique() {
    }
    
    public SessionPedagogique(String code, String nom, LocalDate dateDebut, LocalDate dateFin) {
        this.code = code;
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
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
    
    public LocalDate getDateDebut() {
        return dateDebut;
    }
    
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    public LocalDate getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public List<Cours> getCours() {
        return cours;
    }
    
    public void setCours(List<Cours> cours) {
        this.cours = cours;
    }
}

