package com.app.tradogt.repository;

import com.app.tradogt.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByOrdenIdOrderByFechaEnvio(int idorden);
}
