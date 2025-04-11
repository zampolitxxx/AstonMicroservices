package com.example.service2.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kafka_messages")
public class KafkaMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime receivedAt;

    // Конструкторы, геттеры и сеттеры
    public KafkaMessage() {}

    public KafkaMessage(String content) {
        this.content = content;
        this.receivedAt = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public String getContent() { return content; }
    public LocalDateTime getReceivedAt() { return receivedAt; }

    public void setId(Long id) { this.id = id; }
    public void setContent(String content) { this.content = content; }
    public void setReceivedAt(LocalDateTime receivedAt) { this.receivedAt = receivedAt; }
}