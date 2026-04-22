package com.ebanking.backend.controller;

import com.ebanking.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;

}
