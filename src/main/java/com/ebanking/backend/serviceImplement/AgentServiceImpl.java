package com.ebanking.backend.serviceImplement;

import com.ebanking.backend.repository.AgentRepository;
import com.ebanking.backend.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

	private final AgentRepository agentRepository;

}
