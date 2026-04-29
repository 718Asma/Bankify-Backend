package com.ebanking.backend.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebanking.backend.dto.CompteResponse;
import com.ebanking.backend.dto.CreerCompteRequest;
import com.ebanking.backend.dto.DepotRetraitRequest;
import com.ebanking.backend.dto.TransactionResponse;
import com.ebanking.backend.dto.VirementRequest;
import com.ebanking.backend.model.Client;
import com.ebanking.backend.service.CompteService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/comptes")
@RequiredArgsConstructor
public class CompteController {

	private final CompteService compteService;

	@PostMapping
    public ResponseEntity<CompteResponse> creerCompte(
            @RequestBody CreerCompteRequest req,
            @AuthenticationPrincipal Client currentUser) {
        return ResponseEntity.ok(compteService.creerCompte(req, currentUser));
    }

    @GetMapping("/{rib}/solde")
    public ResponseEntity<Float> consulterSolde(
            @PathVariable String rib,
            @AuthenticationPrincipal Client currentUser) {
        return ResponseEntity.ok(compteService.consulterSolde(rib, currentUser));
    }

    @PostMapping("/{rib}/virement")
    public ResponseEntity<TransactionResponse> effectuerVirement(
            @PathVariable String rib,
            @RequestBody VirementRequest req,
            @AuthenticationPrincipal Client currentUser) {
        return ResponseEntity.ok(compteService.effectuerVirement(rib, req, currentUser));
    }

    @PostMapping("/{rib}/depot")
    public ResponseEntity<TransactionResponse> effectuerDepot(
            @PathVariable String rib,
            @RequestBody DepotRetraitRequest req,
            @AuthenticationPrincipal Client currentUser) {
        return ResponseEntity.ok(compteService.effectuerDepot(rib, req, currentUser));
    }

    @PostMapping("/{rib}/retrait")
    public ResponseEntity<TransactionResponse> effectuerRetrait(
            @PathVariable String rib,
            @RequestBody DepotRetraitRequest req,
            @AuthenticationPrincipal Client currentUser) {
        return ResponseEntity.ok(compteService.effectuerRetrait(rib, req, currentUser));
    }

    @PutMapping("/{rib}/fermer")
    public ResponseEntity<Void> fermerCompte(
            @PathVariable String rib,
            @AuthenticationPrincipal Client currentUser) {
        compteService.fermerCompte(rib, currentUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{rib}/bloquer")
    public ResponseEntity<Void> bloquerCompte(
            @PathVariable String rib,
            @AuthenticationPrincipal Client currentUser) {
        compteService.bloquerCompte(rib, currentUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{rib}/debloquer")
    public ResponseEntity<Void> debloquerCompte(
            @PathVariable String rib,
            @AuthenticationPrincipal Client currentUser) {
        compteService.debloquerCompte(rib, currentUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{rib}/releve")
    public ResponseEntity<byte[]> telechargerReleve(
            @PathVariable String rib,
            @AuthenticationPrincipal Client currentUser) {
        byte[] releve = compteService.telechargerReleve(rib, currentUser);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=releve-" + rib + ".csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(releve);
    }

}
