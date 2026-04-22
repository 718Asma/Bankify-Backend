package com.ebanking.backend.repository;

import com.ebanking.backend.model.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

	List<Notification> findByCibleCin(Integer cin);

}
