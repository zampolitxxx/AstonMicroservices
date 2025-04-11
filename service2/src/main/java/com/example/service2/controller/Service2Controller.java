package com.example.service2.controller;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@RestController
public class Service2Controller {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "service2-data";

    public Service2Controller(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/api/service2")
    public String getService2Data() throws ExecutionException, InterruptedException {
        // Создаем и отправляем сообщение в Kafka
        String message = "Data from Service2 at " + System.currentTimeMillis();
        kafkaTemplate.send(TOPIC, "request-key", message).get();

        // Читаем сообщение из Kafka
        try (Consumer<String, String> consumer = createConsumer()) {
            return pollMessage(consumer, TOPIC);
        }
    }

    @KafkaListener(topics = TOPIC, groupId = "service2-group")
    public void listen(String message) {
        // Реальная обработка сообщения (можно сохранять в БД и т.д.)
        System.out.println("Received message: " + message);
    }

    private Consumer<String, String> createConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "service2-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC));
        return consumer;
    }

    private String pollMessage(Consumer<String, String> consumer, String topic) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));

        for (ConsumerRecord<String, String> record : records) {
            if (topic.equals(record.topic())) {
                consumer.commitSync();
                return "Kafka message: " + record.value() + " (partition: " + record.partition() + ")";
            }
        }
        return "No messages found in Kafka topic: " + topic;
    }
}