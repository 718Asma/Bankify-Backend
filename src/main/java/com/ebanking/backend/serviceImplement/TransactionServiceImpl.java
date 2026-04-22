package com.ebanking.backend.serviceImplement;

import com.ebanking.backend.repository.TransactionRepository;
import com.ebanking.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;

}
