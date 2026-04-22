package com.ebanking.backend.serviceImplement;

import com.ebanking.backend.repository.CompteRepository;
import com.ebanking.backend.service.CompteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompteServiceImpl implements CompteService {

	private final CompteRepository compteRepository;

}
