package com.example.service2.repository;

import com.example.service2.model.KafkaMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KafkaMessageRepository extends JpaRepository<KafkaMessage, Long> {
    KafkaMessage findLastByOrderById();
    KafkaMessage findFirstByOrderById();

    @Query(value = "SELECT * FROM kafka_messages ORDER BY content DESC LIMIT 1", nativeQuery = true)
    KafkaMessage findLastOrderNative();
}
