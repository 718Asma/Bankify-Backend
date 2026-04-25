package com.ebanking.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "type")
	private String type;

	@Column(name = "date_realisation")
	private LocalDate dateRealisation;

	@Column(name = "montant")
	private Float montant;

	@Column(name = "statut")
    private String statut; // EN_ATTENTE, APPROUVE, ANNULE

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_cin")
	private Agent agent;

	@ManyToMany(mappedBy = "transactions", fetch = FetchType.LAZY)
	private Set<Compte> comptes = new HashSet<>();

}
