package com.ebanking.backend.service;

import java.util.List;

import com.ebanking.backend.dto.CompteResponse;
import com.ebanking.backend.dto.CreerCompteRequest;
import com.ebanking.backend.dto.DepotRetraitRequest;
import com.ebanking.backend.dto.TransactionResponse;
import com.ebanking.backend.dto.VirementRequest;
import com.ebanking.backend.model.Client;

public interface CompteService {

    public CompteResponse creerCompte(CreerCompteRequest req, Client currentUser);
    public List<CompteResponse> getAllComptes();
    public List<CompteResponse> getComptesByCin(Integer cin, Client currentUser);
    public List<CompteResponse> getComptesByClient(Client client);
    public CompteResponse getDetailsCompte(String rib, Client client);
    public Float consulterSolde(String rib, Client currentUser);
    public TransactionResponse effectuerVirement(String rib, VirementRequest req, Client currentUser);
    public TransactionResponse effectuerDepot(String rib, DepotRetraitRequest req, Client currentUser);
    public TransactionResponse effectuerRetrait(String rib, DepotRetraitRequest req, Client currentUser);
    public void fermerCompte(String rib, Client currentUser);
    public void bloquerCompte(String rib, Client currentUser);
    public void debloquerCompte(String rib, Client currentUser);
    public byte[] telechargerReleve(String rib, Client currentUser);
}
