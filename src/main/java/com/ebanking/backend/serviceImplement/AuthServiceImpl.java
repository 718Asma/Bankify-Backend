package com.ebanking.backend.serviceImplement;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ebanking.backend.dto.AuthResponse;
import com.ebanking.backend.dto.LoginRequest;
import com.ebanking.backend.dto.SignupRequest;
import com.ebanking.backend.enums.Role;
import com.ebanking.backend.model.Agent;
import com.ebanking.backend.model.Client;
import com.ebanking.backend.repository.ClientRepository;
import com.ebanking.backend.service.AuthService;
import com.ebanking.backend.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{


    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse signup(SignupRequest req) {
        if (clientRepository.existsById(req.cin())) {
            throw new RuntimeException("CIN already registered");
        }
        if (clientRepository.existsByEmail(req.email())) {
            throw new RuntimeException("Email already registered");
        }

        if ("AGENT".equalsIgnoreCase(req.userType())) {
            Agent agent = new Agent();
            agent.setCin(req.cin());
            agent.setNom(req.nom());
            agent.setPrenom(req.prenom());
            agent.setEmail(req.email());
            agent.setMotDePasse(passwordEncoder.encode(req.motDePasse()));
            agent.setDateNaiss(req.dateNaiss());
            agent.setAdresse(req.adresse());
            agent.setTelephone(req.telephone());
            agent.setPoste(req.poste());
            agent.setAgence(req.agence());
            agent.setRole(Role.ROLE_AGENT);
            clientRepository.save(agent);
            String token = jwtService.generateToken(agent, "AGENT");
            return new AuthResponse(token, "AGENT", agent.getEmail(), agent.getNom(), agent.getPrenom());

        } else {
            Client client = new Client();
            client.setCin(req.cin());
            client.setNom(req.nom());
            client.setPrenom(req.prenom());
            client.setEmail(req.email());
            client.setMotDePasse(passwordEncoder.encode(req.motDePasse()));
            client.setDateNaiss(req.dateNaiss());
            client.setAdresse(req.adresse());
            client.setTelephone(req.telephone());
            client.setRole(Role.ROLE_CLIENT);
            clientRepository.save(client);
            String token = jwtService.generateToken(client, "CLIENT");
            return new AuthResponse(token, "CLIENT", client.getEmail(), client.getNom(), client.getPrenom());
        }
    }

    public AuthResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        UserDetails user = clientRepository.findByEmail(req.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String role = user.getAuthorities().iterator().next().getAuthority()
                .replace("ROLE_", "");

        String token = jwtService.generateToken(user, role);
        Client client = (Client) user;
        return new AuthResponse(token, role, client.getEmail(), client.getNom(), client.getPrenom());
    }
}
