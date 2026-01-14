package spring.jpa.centre_formation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un étudiant
 */
@Entity
@Table(name = "etudiants")
@PrimaryKeyJoinColumn(name = "user_id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Etudiant extends User {

    @Column(nullable = false, unique = true, length = 50)
    private String matricule;
    
    @Column(name = "date_inscription", nullable = false)
    private LocalDate dateInscription;
    
    @Column(name = "date_naissance")
    private LocalDate dateNaissance;
    
    @Column(length = 200)
    private String adresse;
    
    @Column(length = 20)
    private String telephone;
    
    @ManyToMany
    @JoinTable(
        name = "etudiant_groupe",
        joinColumns = @JoinColumn(name = "etudiant_id"),
        inverseJoinColumns = @JoinColumn(name = "groupe_id")
    )
    @JsonIgnoreProperties({"etudiants", "cours"})
    private List<Groupe> groupes = new ArrayList<>();

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"etudiant", "cours"})
    private List<Inscription> inscriptions = new ArrayList<>();

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"etudiant", "cours"})
    private List<Note> notes = new ArrayList<>();
    
    // Constructeurs
    public Etudiant() {
        super();
    }
    
    public Etudiant(String email, String password, String nom, String prenom, String matricule, LocalDate dateInscription) {
        super(email, password, nom, prenom, Role.ETUDIANT);
        this.matricule = matricule;
        this.dateInscription = dateInscription;
    }
    
    // Méthodes utilitaires
    public void addInscription(Inscription inscription) {
        inscriptions.add(inscription);
        inscription.setEtudiant(this);
    }
    
    public void removeInscription(Inscription inscription) {
        inscriptions.remove(inscription);
        inscription.setEtudiant(null);
    }
    
    public void addNote(Note note) {
        notes.add(note);
        note.setEtudiant(this);
    }
    
    public void removeNote(Note note) {
        notes.remove(note);
        note.setEtudiant(null);
    }
    
    // Getters et Setters
    public String getMatricule() {
        return matricule;
    }
    
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    
    public LocalDate getDateInscription() {
        return dateInscription;
    }
    
    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }
    
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }
    
    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
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
}

