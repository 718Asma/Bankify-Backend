package com.ebanking.backend.controller;

import com.ebanking.backend.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentController {

	private final AgentService agentService;

}
