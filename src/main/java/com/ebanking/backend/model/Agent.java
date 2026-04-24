package com.ebanking.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "agents")
@PrimaryKeyJoinColumn(name = "cin", referencedColumnName = "cin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Agent extends Client {

	@Column(name = "poste")
	private String poste;

	@Column(name = "agence")
	private String agence;

	@OneToMany(mappedBy = "agent", fetch = FetchType.LAZY)
	private List<Transaction> transactions = new ArrayList<>();

}
