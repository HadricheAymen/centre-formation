package spring.jpa.centre_formation.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.jpa.centre_formation.entity.SessionPedagogique;
import spring.jpa.centre_formation.service.SessionPedagogiqueService;

import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des sessions pédagogiques
 */
@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class SessionPedagogiqueRestController {
    
    private final SessionPedagogiqueService sessionService;
    
    @Autowired
    public SessionPedagogiqueRestController(SessionPedagogiqueService sessionService) {
        this.sessionService = sessionService;
    }
    
    @GetMapping
    public ResponseEntity<List<SessionPedagogique>> getAllSessions() {
        List<SessionPedagogique> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SessionPedagogique> getSessionById(@PathVariable Long id) {
        SessionPedagogique session = sessionService.getSessionById(id);
        return ResponseEntity.ok(session);
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<SessionPedagogique> getSessionByCode(@PathVariable String code) {
        SessionPedagogique session = sessionService.getSessionByCode(code);
        return ResponseEntity.ok(session);
    }
    
    @GetMapping("/actives")
    public ResponseEntity<List<SessionPedagogique>> getSessionsActives() {
        List<SessionPedagogique> sessions = sessionService.getSessionsActives();
        return ResponseEntity.ok(sessions);
    }
    
    @GetMapping("/active")
    public ResponseEntity<SessionPedagogique> getSessionActive(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        SessionPedagogique session = sessionService.getSessionActive(date);
        return ResponseEntity.ok(session);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SessionPedagogique> creerSession(@RequestBody SessionPedagogique session) {
        SessionPedagogique nouvelleSession = sessionService.creerSession(session);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleSession);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SessionPedagogique> updateSession(@PathVariable Long id, @RequestBody SessionPedagogique session) {
        SessionPedagogique sessionMiseAJour = sessionService.updateSession(id, session);
        return ResponseEntity.ok(sessionMiseAJour);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/toggle-active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SessionPedagogique> toggleActive(@PathVariable Long id) {
        SessionPedagogique session = sessionService.toggleActive(id);
        return ResponseEntity.ok(session);
    }
}

