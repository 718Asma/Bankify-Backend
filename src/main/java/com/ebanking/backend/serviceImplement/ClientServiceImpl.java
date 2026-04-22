package com.ebanking.backend.serviceImplement;

import com.ebanking.backend.repository.ClientRepository;
import com.ebanking.backend.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;

}
