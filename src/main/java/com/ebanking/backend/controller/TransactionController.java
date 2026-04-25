package com.ebanking.backend.controller;

import com.ebanking.backend.dto.TransactionResponse;
import com.ebanking.backend.model.Client;
import com.ebanking.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;

	@PutMapping("/{id}/approuver")
    public ResponseEntity<TransactionResponse> approuver(
            @PathVariable Integer id,
            @AuthenticationPrincipal Client currentUser) {
        return ResponseEntity.ok(transactionService.approuver(id, currentUser));
    }

    @PutMapping("/{id}/annuler")
    public ResponseEntity<TransactionResponse> annuler(
            @PathVariable Integer id,
            @AuthenticationPrincipal Client currentUser) {
        return ResponseEntity.ok(transactionService.annuler(id, currentUser));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> consulterTransactions(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(required = false) String rib,
            @AuthenticationPrincipal Client currentUser) {
        return ResponseEntity.ok(
                transactionService.consulterTransactions(type, statut, dateDebut, dateFin, rib, currentUser));
    }

}
