package com.ebanking.backend.repository;

import com.ebanking.backend.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	List<Transaction> findByType(String type);

	@Query("""
        SELECT DISTINCT t FROM Transaction t
        LEFT JOIN FETCH t.comptes c
        WHERE (:type IS NULL OR t.type = :type)
        AND (:statut IS NULL OR t.statut = :statut)
        AND (:dateDebut IS NULL OR t.dateRealisation >= :dateDebut)
        AND (:dateFin IS NULL OR t.dateRealisation <= :dateFin)
        AND (:rib IS NULL OR c.rib = :rib)
    """)
    List<Transaction> findWithFilters(
        @Param("type") String type,
        @Param("statut") String statut,
        @Param("dateDebut") LocalDate dateDebut,
        @Param("dateFin") LocalDate dateFin,
        @Param("rib") String rib
    );

    @Query("SELECT DISTINCT t FROM Transaction t LEFT JOIN FETCH t.comptes")
    List<Transaction> findAllWithComptes();

}
