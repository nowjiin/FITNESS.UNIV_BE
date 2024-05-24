package com.hecto.fitnessuniv.chat;

import java.time.LocalDateTime;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatRepository chatRepository;

    @CrossOrigin
    @GetMapping(
            value = "/sender/{sender}/receiver/{receiver}",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE) // sse타입 데이터가 안끊기고 송신됨
    public Flux<Chat> getMsg(
            @PathVariable String sender, @PathVariable String receiver) { // Flux타입으로 주입유
        return chatRepository
                .mFindBySender(sender, receiver)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @CrossOrigin
    @GetMapping(
            value = "/chat/roomNum/{roomNum}",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE) // sse타입 데이터가 안끊기고 송신됨
    public Flux<Chat> findByRoomNum(@PathVariable Integer roomNum) { // Flux타입으로 주입유
        return chatRepository.mFindByRoomNum(roomNum).subscribeOn(Schedulers.boundedElastic());
    }

    @CrossOrigin
    @PostMapping("/chat")
    public Mono<Chat> setMsg(@RequestBody Chat chat) {
        chat.setCreatedAt(LocalDateTime.now());
        return chatRepository.save(chat);
    }
}
