package com.ebanking.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clients")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {

	@Id
	@Column(name = "cin")
	private Integer cin;

	@Column(name = "nom", nullable = false)
	private String nom;

	@Column(name = "prenom", nullable = false)
	private String prenom;

	@Column(name = "mot_de_passe", nullable = false)
	private String motDePasse;

	@Column(name = "date_naissance")
	private LocalDate dateNaiss;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "telephone")
	private Integer telephone;

	@Column(name = "adresse")
	private String adresse;

	@OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
	private List<Compte> comptes = new ArrayList<>();

	@OneToMany(mappedBy = "cible", fetch = FetchType.LAZY)
	private List<Notification> notifications = new ArrayList<>();

}
