package com.example.checkcheck.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository {

    private static Map<Long, SseEmitter> userEmitterMap = new ConcurrentHashMap<>();
    //save 자체가 되지 않고 null값이 뜨고 있음, Cache에 저장하고 불러와야함
    public void addOrReplaceEmitter(Long id, SseEmitter emitter) {
        userEmitterMap.put(id, emitter);
    }

    public void remove(Long id) {
        if (userEmitterMap != null) {
            userEmitterMap.remove(id);
        }
    }

    public Optional<SseEmitter> get(Long id) {
        return Optional.ofNullable(userEmitterMap.get(id));
    }
}
