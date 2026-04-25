package com.ebanking.backend.service;

import java.time.LocalDate;
import java.util.List;

import com.ebanking.backend.dto.TransactionResponse;
import com.ebanking.backend.model.Client;

public interface TransactionService {

    public TransactionResponse approuver(Integer id, Client currentUser);
    public TransactionResponse annuler(Integer id, Client currentUser);
    public List<TransactionResponse> consulterTransactions(String type, String statut,
                                                            LocalDate dateDebut, LocalDate dateFin,
                                                            String rib, Client currentUser);

}
