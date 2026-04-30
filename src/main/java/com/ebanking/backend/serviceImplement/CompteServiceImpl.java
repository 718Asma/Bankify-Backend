package com.ebanking.backend.serviceImplement;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebanking.backend.dto.CompteResponse;
import com.ebanking.backend.dto.CreerCompteRequest;
import com.ebanking.backend.dto.DepotRetraitRequest;
import com.ebanking.backend.dto.TransactionResponse;
import com.ebanking.backend.dto.VirementRequest;
import com.ebanking.backend.exception.BusinessException;
import com.ebanking.backend.exception.ResourceNotFoundException;
import com.ebanking.backend.exception.UnauthorizedException;
import com.ebanking.backend.model.Agent;
import com.ebanking.backend.model.Client;
import com.ebanking.backend.model.Compte;
import com.ebanking.backend.model.Transaction;
import com.ebanking.backend.repository.ClientRepository;
import com.ebanking.backend.repository.CompteRepository;
import com.ebanking.backend.repository.TransactionRepository;
import com.ebanking.backend.service.CompteService;
import com.ebanking.backend.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompteServiceImpl implements CompteService {

	private final CompteRepository compteRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;

    public CompteResponse creerCompte(CreerCompteRequest req, Client currentUser) {
        requireAgent(currentUser);

        if (compteRepository.existsByRib(req.rib())) {
            throw new BusinessException("Un compte avec ce RIB existe déjà");
        }

        Client proprietaire = clientRepository.findById(req.clientCin())
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec CIN: " + req.clientCin()));

        Compte compte = new Compte();
        compte.setRib(req.rib());
        compte.setSolde(req.soldeInitial() != null ? req.soldeInitial() : 0f);
        compte.setDateCreation(LocalDate.now());
        compte.setType(req.type());
        compte.setStatus("ACTIF");
        compte.setClient(proprietaire);
        compteRepository.save(compte);
        return toResponse(compte);
    }

    public List<CompteResponse> getComptesByCin(Integer cin, Client currentUser) {
        requireAgent(currentUser);
        return compteRepository.findByClientCin(cin)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CompteResponse> getComptesByClient(Client client) {
        List<Compte> comptes = compteRepository.findByClient(client);

        return comptes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CompteResponse getDetailsCompte(String rib, Client client) {
        Compte compte = compteRepository.findByRib(rib)
                .orElseThrow(() -> new ResourceNotFoundException("Compte introuvable: " + rib));

        if (!(client instanceof Agent) && !compte.getClient().getCin().equals(client.getCin())){
            throw new UnauthorizedException("Accès non autorisé à ce compte");
        }

        return toResponse(compte);
    }

    public List<CompteResponse> getAllComptes() {
        return compteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Float consulterSolde(String rib, Client currentUser) {
        Compte compte = getCompteOrThrow(rib);
        checkAccess(compte, currentUser);
        return compte.getSolde();
    }

    @Transactional
    public TransactionResponse effectuerVirement(String rib, VirementRequest req, Client currentUser) {
        Compte source = getCompteOrThrow(rib);
        checkProprietaire(source, currentUser);
        checkCompteActif(source);

        Compte destination = compteRepository.findById(req.ribDestination())
                .orElseThrow(() -> new ResourceNotFoundException("Compte destination introuvable: " + req.ribDestination()));
        checkCompteActif(destination);

        if (req.montant() <= 0) throw new BusinessException("Le montant doit être positif");

        Transaction transaction = new Transaction();
        transaction.setType("VIREMENT");
        transaction.setMontant(req.montant());
        transaction.setDateRealisation(LocalDate.now());
        transaction.setStatut("EN_ATTENTE");
        Transaction saved = transactionRepository.save(transaction);

        source.getTransactions().add(saved);
        destination.getTransactions().add(saved);
        compteRepository.save(source);
        compteRepository.save(destination);

        return toTransactionResponse(saved);
    }

    @Transactional
    public TransactionResponse effectuerDepot(String rib, DepotRetraitRequest req, Client currentUser) {
        Compte compte = getCompteOrThrow(rib);
        checkProprietaire(compte, currentUser);
        checkCompteActif(compte);
        if (req.montant() <= 0) throw new BusinessException("Le montant doit être positif");

        Transaction transaction = new Transaction();
        transaction.setType("DEPOT");
        transaction.setMontant(req.montant());
        transaction.setDateRealisation(LocalDate.now());
        transaction.setStatut("EN_ATTENTE");
        Transaction saved = transactionRepository.save(transaction);

        compte.getTransactions().add(saved);
        compteRepository.save(compte);
        return toTransactionResponse(saved);
    }

    @Transactional
    public TransactionResponse effectuerRetrait(String rib, DepotRetraitRequest req, Client currentUser) {
        Compte compte = getCompteOrThrow(rib);
        checkProprietaire(compte, currentUser);
        checkCompteActif(compte);
        if (req.montant() <= 0) throw new BusinessException("Le montant doit être positif");
        if (compte.getSolde() < req.montant()) throw new BusinessException("Solde insuffisant");

        Transaction transaction = new Transaction();
        transaction.setType("RETRAIT");
        transaction.setMontant(req.montant());
        transaction.setDateRealisation(LocalDate.now());
        transaction.setStatut("EN_ATTENTE");
        Transaction saved = transactionRepository.save(transaction);

        compte.getTransactions().add(saved);
        compteRepository.save(compte);
        return toTransactionResponse(saved);
    }

    public void fermerCompte(String rib, Client currentUser) {
        Compte compte = getCompteOrThrow(rib);
        checkAccess(compte, currentUser);
        if ("FERME".equals(compte.getStatus())) throw new BusinessException("Compte déjà fermé");
        compte.setStatus("FERME");
        compteRepository.save(compte);
    }

    public void bloquerCompte(String rib, Client currentUser) {
        requireAgent(currentUser);
        Compte compte = getCompteOrThrow(rib);
        if ("BLOQUE".equals(compte.getStatus())) throw new BusinessException("Compte déjà bloqué");
        if ("FERME".equals(compte.getStatus())) throw new BusinessException("Impossible de bloquer un compte fermé");
        compte.setStatus("BLOQUE");
        compteRepository.save(compte);

        notificationService.envoyer(compte.getClient(), "Votre compte " + rib + " a été bloqué.");
    }

    public void debloquerCompte(String rib, Client currentUser) {
        requireAgent(currentUser);
        Compte compte = getCompteOrThrow(rib);
        if (!"BLOQUE".equals(compte.getStatus())) throw new BusinessException("Le compte n'est pas bloqué");
        compte.setStatus("ACTIF");
        compteRepository.save(compte);

        notificationService.envoyer(compte.getClient(), "Votre compte " + rib + " a été débloqué.");
    }

    @Transactional
    public byte[] telechargerReleve(String rib, Client currentUser) {
        Compte compte = getCompteOrThrow(rib);
        checkAccess(compte, currentUser);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        writer.println("Relevé de Compte - RIB: " + rib);
        writer.println("Client: " + compte.getClient().getNom() + " " + compte.getClient().getPrenom());
        writer.println("Solde actuel: " + compte.getSolde() + " TND");
        writer.println("Statut: " + compte.getStatus());
        writer.println("---");
        writer.println("ID,Type,Date,Montant,Statut");

        compte.getTransactions().forEach(t ->
            writer.println(t.getId() + "," + t.getType() + "," +
                    t.getDateRealisation() + "," + t.getMontant() + "," + t.getStatut())
        );

        writer.flush();
        return out.toByteArray();
    }

    // --- helpers ---

    private Compte getCompteOrThrow(String rib) {
        return compteRepository.findById(rib)
                .orElseThrow(() -> new ResourceNotFoundException("Compte introuvable: " + rib));
    }

    private void checkAccess(Compte compte, Client currentUser) {
        if (currentUser instanceof Agent) return;
        if (!compte.getClient().getCin().equals(currentUser.getCin())) {
            throw new UnauthorizedException("Accès refusé: ce compte ne vous appartient pas");
        }
    }

    private void checkProprietaire(Compte compte, Client currentUser) {
        if (currentUser instanceof Agent) {
            throw new UnauthorizedException("Accès refusé: opération réservée au propriétaire du compte");
        }
        if (!compte.getClient().getCin().equals(currentUser.getCin())) {
            throw new UnauthorizedException("Accès refusé: ce compte ne vous appartient pas");
        }
    }

    private void checkCompteActif(Compte compte) {
        if (!"ACTIF".equals(compte.getStatus())) {
            throw new BusinessException("Opération impossible: le compte " + compte.getRib() + " est " + compte.getStatus());
        }
    }

    private void requireAgent(Client currentUser) {
        if (!(currentUser instanceof Agent)) {
            throw new UnauthorizedException("Accès refusé: réservé aux agents");
        }
    }

    public CompteResponse toResponse(Compte c) {
        return new CompteResponse(
                c.getRib(), c.getSolde(), c.getDateCreation(),
                c.getType(), c.getStatus(), c.getClient().getCin()
        );
    }

    public TransactionResponse toTransactionResponse(Transaction t) {
        Set<String> ribs = t.getComptes().stream()
                .map(Compte::getRib).collect(Collectors.toSet());
        return new TransactionResponse(
                t.getId(), t.getType(), t.getDateRealisation(), t.getMontant(),
                t.getStatut(), t.getAgent() != null ? t.getAgent().getCin() : null, ribs
        );
    }
	
}
