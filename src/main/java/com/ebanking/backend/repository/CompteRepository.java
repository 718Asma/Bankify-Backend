package com.ebanking.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebanking.backend.model.Client;
import com.ebanking.backend.model.Compte;

@Repository
public interface CompteRepository extends JpaRepository<Compte, String> {

	List<Compte> findByClientCin(Integer cin);

	List<Compte> findByStatus(String status);

	boolean existsByRib(String rib);

	List<Compte> findByClient(Client client);

    Optional<Compte> findByRib(String rib);

}
