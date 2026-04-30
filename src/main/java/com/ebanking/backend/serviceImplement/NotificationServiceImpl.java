package com.ebanking.backend.serviceImplement;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ebanking.backend.dto.NotificationResponse;
import com.ebanking.backend.model.Client;
import com.ebanking.backend.model.Notification;
import com.ebanking.backend.repository.NotificationRepository;
import com.ebanking.backend.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void envoyer(Client cible, String contenu) {
        Notification n = new Notification();
        n.setCible(cible);
        n.setContenu(contenu);
        n.setDateCreation(LocalDate.now());
        notificationRepository.save(n);
    }

    @Override
    public List<NotificationResponse> getMesNotifications(Client currentUser) {
        return notificationRepository.findByCibleCin(currentUser.getCin())
                .stream()
                .map(n -> new NotificationResponse(n.getId(), n.getContenu(), n.getDateCreation()))
                .toList();
    }
}