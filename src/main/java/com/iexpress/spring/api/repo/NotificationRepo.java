package com.iexpress.spring.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iexpress.spring.domain.Notification;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Integer>{

}
