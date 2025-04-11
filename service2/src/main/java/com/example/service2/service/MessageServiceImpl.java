package com.example.service2.service;

import com.example.service2.model.KafkaMessage;
import com.example.service2.repository.KafkaMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private KafkaMessageRepository kafkaMessageRepo;

    @Override
    public KafkaMessage findLastByOrderById() {
        return kafkaMessageRepo.findLastByOrderById();
    }

    @Override
    public KafkaMessage findFirstByOrderById() {
        return kafkaMessageRepo.findFirstByOrderById();
    }

    @Override
    public KafkaMessage getLastMessageWithNative() {
        return kafkaMessageRepo.findLastOrderNative();
    }
}
