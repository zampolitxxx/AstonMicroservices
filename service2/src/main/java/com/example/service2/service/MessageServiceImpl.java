package com.example.service2.service;

import com.example.service2.model.KafkaMessage;
import com.example.service2.repository.KafkaMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private KafkaMessageRepository KafkaMessageRepo;

    @Override
    public KafkaMessage findLastByOrderById() {
        return KafkaMessageRepo.findLastByOrderById();
    }

    @Override
    public KafkaMessage findFirstByOrderById() {
        return KafkaMessageRepo.findFirstByOrderById();
    }

    @Override
    public KafkaMessage getLastMessageWithNative() {
        return KafkaMessageRepo.findLastOrderNative();
    }
}
