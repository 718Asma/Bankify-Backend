package com.ebanking.backend.repository;

import com.ebanking.backend.model.Compte;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteRepository extends JpaRepository<Compte, String> {

	List<Compte> findByClientCin(Integer cin);

	List<Compte> findByStatus(String status);

}
