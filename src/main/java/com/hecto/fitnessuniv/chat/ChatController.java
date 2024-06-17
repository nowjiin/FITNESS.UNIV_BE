package com.hecto.fitnessuniv.chat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.hecto.fitnessuniv.provider.JwtProvider;
import com.hecto.fitnessuniv.repository.MentorProfileRepository;
import com.hecto.fitnessuniv.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final MentorProfileRepository mentorProfileRepository;

    @CrossOrigin
    @GetMapping(
            value = "/sender/{sender}/receiver/{receiver}",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> getMsg(@PathVariable String sender, @PathVariable String receiver) {
        return chatRepository
                .mFindBySender(sender, receiver)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @CrossOrigin
    @GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> findByRoomNum(@PathVariable String roomNum) {
        return chatRepository.mFindByRoomNum(roomNum).subscribeOn(Schedulers.boundedElastic());
    }

    @CrossOrigin
    @PostMapping("/chat")
    public Mono<Chat> setMsg(@RequestBody Chat chat) {
        chat.setCreatedAt(LocalDateTime.now());
        System.out.println("Received message: " + chat); // 로그 추가
        return chatRepository.save(chat);
    }

    @CrossOrigin
    @GetMapping("/auth/user")
    public Mono<Map<String, String>> getUserInfo(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7); // "Bearer " 부분 제거
        String userId = jwtProvider.getUserIdFromToken(jwt);

        return userRepository
                .findByUserId(userId)
                .map(
                        userEntity -> {
                            Map<String, String> userInfo = new HashMap<>();
                            userInfo.put("userId", userEntity.getUserId());
                            userInfo.put("username", userEntity.getUserName());
                            return userInfo;
                        })
                .map(Mono::just)
                .orElseGet(() -> Mono.error(new IllegalArgumentException("Invalid token")));
    }

    @CrossOrigin
    @PostMapping("/chat/createRoom")
    public Mono<Map<String, String>> createRoom(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7); // "Bearer " 부분 제거
        String userId = jwtProvider.getUserIdFromToken(jwt);

        return userRepository
                .findByUserId(userId)
                .flatMap(user -> mentorProfileRepository.findByUser(user))
                .map(
                        mentorProfile -> {
                            Map<String, String> response = new HashMap<>();
                            response.put("roomNum", mentorProfile.getId().toString());
                            return response;
                        })
                .map(Mono::just)
                .orElseGet(
                        () -> Mono.error(new IllegalArgumentException("Mentor profile not found")));
    }
}
