package com.ebanking.backend.serviceImplement;

import com.ebanking.backend.dto.TransactionResponse;
import com.ebanking.backend.exception.BusinessException;
import com.ebanking.backend.exception.ResourceNotFoundException;
import com.ebanking.backend.exception.UnauthorizedException;
import com.ebanking.backend.model.Agent;
import com.ebanking.backend.model.Client;
import com.ebanking.backend.model.Compte;
import com.ebanking.backend.model.Transaction;
import com.ebanking.backend.repository.CompteRepository;
import com.ebanking.backend.repository.TransactionRepository;
import com.ebanking.backend.service.TransactionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;
    private final CompteRepository compteRepository;

    @Transactional
    public TransactionResponse approuver(Integer id, Client currentUser) {
        requireAgent(currentUser);
        Transaction transaction = getOrThrow(id);

        if (!"EN_ATTENTE".equals(transaction.getStatut())) {
            throw new BusinessException("Seules les transactions EN_ATTENTE peuvent être approuvées");
        }

        // apply balance changes based on type
        applyTransaction(transaction);

        transaction.setStatut("APPROUVE");
        transaction.setAgent((Agent) currentUser);
        transactionRepository.save(transaction);
        return toResponse(transaction);
    }

    @Transactional
    public TransactionResponse annuler(Integer id, Client currentUser) {
        requireAgent(currentUser);
        Transaction transaction = getOrThrow(id);

        if (!"EN_ATTENTE".equals(transaction.getStatut())) {
            throw new BusinessException("Seules les transactions EN_ATTENTE peuvent être annulées");
        }

        transaction.setStatut("ANNULE");
        transaction.setAgent((Agent) currentUser);
        transactionRepository.save(transaction);
        return toResponse(transaction);
    }

	
    public List<TransactionResponse> consulterTransactions(String type, String statut,
                                                            LocalDate dateDebut, LocalDate dateFin,
                                                            String rib, Client currentUser) {
        // if client, restrict to their own accounts
        String ribFilter = rib;
        if (!(currentUser instanceof Agent) && rib == null) {
            // return only transactions linked to client's accounts
            List<String> clientRibs = compteRepository.findByClientCin(currentUser.getCin())
                    .stream().map(Compte::getRib).toList();

            return transactionRepository.findAll().stream()
                    .filter(t -> t.getComptes().stream()
                            .anyMatch(c -> clientRibs.contains(c.getRib())))
                    .filter(t -> type == null || t.getType().equals(type))
                    .filter(t -> statut == null || t.getStatut().equals(statut))
                    .filter(t -> dateDebut == null || !t.getDateRealisation().isBefore(dateDebut))
                    .filter(t -> dateFin == null || !t.getDateRealisation().isAfter(dateFin))
                    .map(this::toResponse)
                    .toList();
        }

        return transactionRepository.findWithFilters(type, statut, dateDebut, dateFin, ribFilter)
                .stream().map(this::toResponse).toList();
    }

    // --- helpers ---

    private void applyTransaction(Transaction transaction) {
        List<Compte> comptes = transaction.getComptes().stream().toList();

        switch (transaction.getType()) {
            case "DEPOT" -> {
                Compte compte = comptes.get(0);
                compte.setSolde(compte.getSolde() + transaction.getMontant());
                compteRepository.save(compte);
            }
            case "RETRAIT" -> {
                Compte compte = comptes.get(0);
                if (compte.getSolde() < transaction.getMontant()) {
                    throw new BusinessException("Solde insuffisant pour approuver ce retrait");
                }
                compte.setSolde(compte.getSolde() - transaction.getMontant());
                compteRepository.save(compte);
            }
            case "VIREMENT" -> {
                // find source (client's account) and destination
                if (comptes.size() < 2) throw new BusinessException("Virement invalide: comptes manquants");
                // both comptes are linked; we need to identify source vs destination
                // source is the one whose client initiated — we check which was saved first via any heuristic
                // simplest: the transaction was created from source, so first compte added is source
                // Since Set has no order, we store source/dest by checking transaction comptes order
                // For simplicity, deduct from the compte that belongs to a non-agent client
                Compte source = comptes.stream()
                        .filter(c -> !"AGENT".equals(c.getClient().getRole().name()))
                        .findFirst()
                        .orElse(comptes.get(0));
                Compte destination = comptes.stream()
                        .filter(c -> !c.getRib().equals(source.getRib()))
                        .findFirst()
                        .orElseThrow(() -> new BusinessException("Compte destination introuvable"));

                if (source.getSolde() < transaction.getMontant()) {
                    throw new BusinessException("Solde insuffisant pour approuver ce virement");
                }
                source.setSolde(source.getSolde() - transaction.getMontant());
                destination.setSolde(destination.getSolde() + transaction.getMontant());
                compteRepository.save(source);
                compteRepository.save(destination);
            }
            default -> throw new BusinessException("Type de transaction inconnu: " + transaction.getType());
        }
    }

    private Transaction getOrThrow(Integer id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction introuvable: " + id));
    }

    private void requireAgent(Client currentUser) {
        if (!(currentUser instanceof Agent)) {
            throw new UnauthorizedException("Accès refusé: réservé aux agents");
        }
    }

    public TransactionResponse toResponse(Transaction t) {
        Set<String> ribs = t.getComptes().stream()
                .map(Compte::getRib).collect(Collectors.toSet());
        return new TransactionResponse(
                t.getId(), t.getType(), t.getDateRealisation(), t.getMontant(),
                t.getStatut(), t.getAgent() != null ? t.getAgent().getCin() : null, ribs
        );
    }

}
