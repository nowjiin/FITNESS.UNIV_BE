package com.hecto.fitnessuniv.chat;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "chat")
public class Chat {
    @Id private String id;
    private String msg;
    private String sender;
    private String userId;
    private String receiver;
    private String roomNum;

    private LocalDateTime createdAt;
}
