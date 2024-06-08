package com.hecto.fitnessuniv.chat;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;

import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {

    @Tailable
    @Query("{sender: ?0,receiver: ?1}")
    Flux<Chat> mFindBySender(
            String sender, String receiver); // Flux(흐름) response를 유지하면서 데이터를 계속 흐려보냄

    @Tailable
    @Query("{roomNum: ?0}")
    Flux<Chat> mFindByRoomNum(String roomNum);
}
