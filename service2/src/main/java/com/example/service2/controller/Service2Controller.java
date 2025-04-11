package com.example.service2.controller;

import com.example.service2.service.MessageService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.service2.model.KafkaMessage;
import com.example.service2.repository.KafkaMessageRepository;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@RestController
public class Service2Controller {

    @Autowired
    private MessageService messageService;

    private final KafkaMessageRepository messageRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "service2-data";

    public Service2Controller(KafkaMessageRepository messageRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.messageRepository = messageRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/api/service2")
    public String getService2Data() throws Exception {
        String message = "Data from Service2 at " + System.currentTimeMillis();
        kafkaTemplate.send(TOPIC, message).get();

        try (Consumer<String, String> consumer = createConsumer()) {
            String result = pollMessage(consumer, TOPIC);
            return result;
        }
    }

    @KafkaListener(topics = TOPIC, groupId = "service2-group")
    public void listen(String message) {
        //Save the message to DB
        KafkaMessage kafkaMessage = new KafkaMessage(message);
        messageRepository.save(kafkaMessage);
        System.out.println("Saved to DB: " + message);
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

        KafkaMessage lastDataFromDB = messageService.findFirstByOrderById();
        String messFromDB = lastDataFromDB.getContent();

        for (ConsumerRecord<String, String> record : records) {
            if (topic.equals(record.topic())) {
                consumer.commitSync();
                return "Kafka message: " + record.value() + " (partition: " + record.partition() + ")\n"
                        + "First message from DB: " + messFromDB + "\n";
            }
        }
        return "No messages found in Kafka topic: " + topic;
    }
}
