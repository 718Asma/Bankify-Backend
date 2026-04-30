package com.ebanking.backend.service;

import java.util.List;

import com.ebanking.backend.dto.ClientResponse;
import com.ebanking.backend.dto.ModifierProfilRequest;
import com.ebanking.backend.dto.ReinitialiserMotDePasseRequest;
import com.ebanking.backend.model.Client;

public interface ClientService {
    public List<ClientResponse> getAllClients();
    public ClientResponse modifierProfil(ModifierProfilRequest req, Client currentUser);
    public void reinitialiserMotDePasse(ReinitialiserMotDePasseRequest req, Client currentUser);
    public ClientResponse consulterProfil(Integer cin, Client currentUser);
    public List<ClientResponse> rechercherClients(String nom, String prenom, String email,
                                                   Integer cin, Client currentUser);

}
