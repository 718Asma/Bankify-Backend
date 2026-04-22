package com.ebanking.backend.serviceImplement;

import com.ebanking.backend.repository.NotificationRepository;
import com.ebanking.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;

}
