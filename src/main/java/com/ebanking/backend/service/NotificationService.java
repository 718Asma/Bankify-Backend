package com.ebanking.backend.service;

import java.util.List;

import com.ebanking.backend.dto.NotificationResponse;
import com.ebanking.backend.model.Client;

public interface NotificationService {
    void envoyer(Client cible, String contenu);
    List<NotificationResponse> getMesNotifications(Client currentUser);
}