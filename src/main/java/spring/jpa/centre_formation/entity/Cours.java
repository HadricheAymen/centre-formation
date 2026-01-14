package spring.jpa.centre_formation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un cours
 */
@Entity
@Table(name = "cours")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cours {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String code;
    
    @Column(nullable = false, length = 200)
    private String titre;
    
    @Column(length = 1000)
    private String description;
    
    @Column(name = "nombre_heures")
    private Integer nombreHeures;
    
    @Column(name = "coefficient")
    private Double coefficient;
    
    @ManyToOne
    @JoinColumn(name = "formateur_id")
    @JsonIgnoreProperties({"cours", "inscriptions", "notes", "seances"})
    private Formateur formateur;

    @ManyToOne
    @JoinColumn(name = "specialite_id")
    @JsonIgnoreProperties({"cours", "formateurs"})
    private Specialite specialite;

    @ManyToOne
    @JoinColumn(name = "session_id")
    @JsonIgnoreProperties({"cours"})
    private SessionPedagogique session;

    @ManyToMany
    @JoinTable(
        name = "cours_groupe",
        joinColumns = @JoinColumn(name = "cours_id"),
        inverseJoinColumns = @JoinColumn(name = "groupe_id")
    )
    @JsonIgnoreProperties({"cours", "etudiants"})
    private List<Groupe> groupes = new ArrayList<>();

    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"cours", "etudiant"})
    private List<Inscription> inscriptions = new ArrayList<>();

    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"cours", "etudiant"})
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"cours"})
    private List<Seance> seances = new ArrayList<>();
    
    // Constructeurs
    public Cours() {
    }
    
    public Cours(String code, String titre, String description, Formateur formateur) {
        this.code = code;
        this.titre = titre;
        this.description = description;
        this.formateur = formateur;
    }
    
    // Méthodes utilitaires
    public void addInscription(Inscription inscription) {
        inscriptions.add(inscription);
        inscription.setCours(this);
    }
    
    public void removeInscription(Inscription inscription) {
        inscriptions.remove(inscription);
        inscription.setCours(null);
    }
    
    public void addNote(Note note) {
        notes.add(note);
        note.setCours(this);
    }
    
    public void removeNote(Note note) {
        notes.remove(note);
        note.setCours(null);
    }
    
    public void addSeance(Seance seance) {
        seances.add(seance);
        seance.setCours(this);
    }
    
    public void removeSeance(Seance seance) {
        seances.remove(seance);
        seance.setCours(null);
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
    
    public String getTitre() {
        return titre;
    }
    
    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getNombreHeures() {
        return nombreHeures;
    }
    
    public void setNombreHeures(Integer nombreHeures) {
        this.nombreHeures = nombreHeures;
    }
    
    public Double getCoefficient() {
        return coefficient;
    }
    
    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }
    
    public Formateur getFormateur() {
        return formateur;
    }
    
    public void setFormateur(Formateur formateur) {
        this.formateur = formateur;
    }
    
    public Specialite getSpecialite() {
        return specialite;
    }
    
    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }
    
    public SessionPedagogique getSession() {
        return session;
    }
    
    public void setSession(SessionPedagogique session) {
        this.session = session;
    }
    
    public List<Groupe> getGroupes() {
        return groupes;
    }
    
    public void setGroupes(List<Groupe> groupes) {
        this.groupes = groupes;
    }
    
    public List<Inscription> getInscriptions() {
        return inscriptions;
    }
    
    public void setInscriptions(List<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }
    
    public List<Note> getNotes() {
        return notes;
    }
    
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
    
    public List<Seance> getSeances() {
        return seances;
    }
    
    public void setSeances(List<Seance> seances) {
        this.seances = seances;
    }
}

