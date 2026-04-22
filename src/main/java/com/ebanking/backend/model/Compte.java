package com.ebanking.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
@Table(name = "comptes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Compte {

	@Id
	@Column(name = "rib", length = 50)
	private String rib;

	@Column(name = "solde", nullable = false)
	private Float solde;

	@Column(name = "date_creation")
	private LocalDate dateCreation;

	@Column(name = "type")
	private String type;

	@Column(name = "status")
	private String status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_cin", nullable = false)
	private Client client;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "transaction_comptes",
			joinColumns = @JoinColumn(name = "compte_rib"),
			inverseJoinColumns = @JoinColumn(name = "transaction_id")
	)
	private Set<Transaction> transactions = new HashSet<>();

}
