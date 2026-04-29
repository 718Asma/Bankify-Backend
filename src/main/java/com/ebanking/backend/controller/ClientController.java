package com.ebanking.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebanking.backend.dto.ClientResponse;
import com.ebanking.backend.dto.ModifierProfilRequest;
import com.ebanking.backend.dto.ReinitialiserMotDePasseRequest;
import com.ebanking.backend.model.Client;
import com.ebanking.backend.service.ClientService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

	private final ClientService clientService;

	@PutMapping("/profil")
    public ResponseEntity<ClientResponse> modifierProfil(
            @RequestBody ModifierProfilRequest req,
            @AuthenticationPrincipal Client currentUser) {
        return ResponseEntity.ok(clientService.modifierProfil(req, currentUser));
    }

    @PutMapping("/mot-de-passe")
    public ResponseEntity<Void> reinitialiserMotDePasse(
            @RequestBody ReinitialiserMotDePasseRequest req,
            @AuthenticationPrincipal Client currentUser) {
        clientService.reinitialiserMotDePasse(req, currentUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profil")
    public ResponseEntity<ClientResponse> consulterMonProfil(
            @AuthenticationPrincipal Client currentUser) {
        return ResponseEntity.ok(clientService.consulterProfil(currentUser.getCin(), currentUser));
    }

    @GetMapping("/profil/{cin}")
    public ResponseEntity<ClientResponse> consulterProfil(
            @PathVariable Integer cin,
            @AuthenticationPrincipal Client currentUser) {
        return ResponseEntity.ok(clientService.consulterProfil(cin, currentUser));
    }

    @GetMapping("/rechercher")
    public ResponseEntity<List<ClientResponse>> rechercherClients(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer cin,
            @AuthenticationPrincipal Client currentUser) {
        return ResponseEntity.ok(clientService.rechercherClients(nom, prenom, email, cin, currentUser));
    }

}
