package com.ebanking.backend.serviceImplement;

import com.ebanking.backend.dto.ClientResponse;
import com.ebanking.backend.dto.ModifierProfilRequest;
import com.ebanking.backend.dto.ReinitialiserMotDePasseRequest;
import com.ebanking.backend.exception.BusinessException;
import com.ebanking.backend.exception.ResourceNotFoundException;
import com.ebanking.backend.exception.UnauthorizedException;
import com.ebanking.backend.model.Agent;
import com.ebanking.backend.model.Client;
import com.ebanking.backend.repository.ClientRepository;
import com.ebanking.backend.service.ClientService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

	
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public List<ClientResponse> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ClientResponse modifierProfil(ModifierProfilRequest req, Client currentUser) {
        currentUser.setNom(req.nom());
        currentUser.setPrenom(req.prenom());
        currentUser.setEmail(req.email());
        currentUser.setDateNaiss(req.dateNaiss());
        currentUser.setAdresse(req.adresse());
        currentUser.setTelephone(req.telephone());
        clientRepository.save(currentUser);
        return toResponse(currentUser);
    }

    public void reinitialiserMotDePasse(ReinitialiserMotDePasseRequest req, Client currentUser) {
        if (!passwordEncoder.matches(req.ancienMotDePasse(), currentUser.getMotDePasse())) {
            throw new BusinessException("Ancien mot de passe incorrect");
        }
        currentUser.setMotDePasse(passwordEncoder.encode(req.nouveauMotDePasse()));
        clientRepository.save(currentUser);
    }

    public ClientResponse consulterProfil(Integer cin, Client currentUser) {
        if (!(currentUser instanceof Agent) && !currentUser.getCin().equals(cin)) {
            throw new UnauthorizedException("Accès refusé: vous ne pouvez consulter que votre propre profil");
        }
        Client client = clientRepository.findById(cin)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec CIN: " + cin));
        return toResponse(client);
    }

    public List<ClientResponse> rechercherClients(String nom, String prenom, String email,
                                                   Integer cin, Client currentUser) {
        requireAgent(currentUser);
        return clientRepository.findAll().stream()
                .filter(c -> nom == null || c.getNom().toLowerCase().contains(nom.toLowerCase()))
                .filter(c -> prenom == null || c.getPrenom().toLowerCase().contains(prenom.toLowerCase()))
                .filter(c -> email == null || c.getEmail().toLowerCase().contains(email.toLowerCase()))
                .filter(c -> cin == null || c.getCin().equals(cin))
                .map(this::toResponse)
                .toList();
    }

    // --- helpers ---

    private void requireAgent(Client currentUser) {
        if (!(currentUser instanceof Agent)) {
            throw new UnauthorizedException("Accès refusé: réservé aux agents");
        }
    }

    public ClientResponse toResponse(Client c) {
        return new ClientResponse(
                c.getCin(), c.getNom(), c.getPrenom(), c.getEmail(),
                c.getAdresse(), c.getTelephone(), c.getDateNaiss(),
                c.getRole().name()
        );
    }

}
