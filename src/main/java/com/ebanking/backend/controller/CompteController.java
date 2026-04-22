package com.ebanking.backend.controller;

import com.ebanking.backend.service.CompteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comptes")
@RequiredArgsConstructor
public class CompteController {

	private final CompteService compteService;

}
