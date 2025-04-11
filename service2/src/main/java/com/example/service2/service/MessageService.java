package com.example.service2.service;

import com.example.service2.model.KafkaMessage;


public interface MessageService {
    KafkaMessage findLastByOrderById();
    KafkaMessage findFirstByOrderById();
    KafkaMessage getLastMessageWithNative();
}
