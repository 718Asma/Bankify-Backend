package com.ebanking.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ebanking.backend.enums.Role;

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
public class Client implements UserDetails{

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

	@Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.ROLE_CLIENT;

	// --- UserDetails impl ---

    @Override
    public String getUsername() {
        return email; // email is the login identifier
    }

    @Override
    public String getPassword() {
        return motDePasse;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

}
