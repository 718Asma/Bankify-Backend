package com.ebanking.backend.repository;

import com.ebanking.backend.model.Agent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Integer> {

	List<Agent> findByAgence(String agence);

}
