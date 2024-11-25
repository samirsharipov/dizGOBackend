package uz.pdp.springsecurity.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmitterService {

    private final Map<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter getOrCreateEmitter(UUID branchId) {
        SseEmitter emitter = emitters.get(branchId);  // Avval mavjud emitterni tekshirib ko'ramiz

        if (emitter == null) { // Agar mavjud bo'lmasa, yangi yaratamiz
            emitter = new SseEmitter(Long.MAX_VALUE);
            emitters.put(branchId, emitter);
            System.out.println("Emitter created for branchId: " + branchId);
        } else {
            System.out.println("Emitter retrieved for branchId: " + branchId);
        }

        emitter.onCompletion(() -> {
            System.out.println("Emitter completed for branchId: " + branchId);
            emitters.remove(branchId);
        });
        emitter.onTimeout(() -> {
            System.out.println("Emitter timed out for branchId: " + branchId);
            emitters.remove(branchId);
        });
        return emitter;
    }

    public void sendProgress(UUID branchId, int progress, int productsUploaded) {
        SseEmitter emitter = emitters.get(branchId);
        if (emitter != null) {
            try {

                Map<String, Object> data = new HashMap<>();
                data.put("progress", progress);
                data.put("productsUploaded", productsUploaded);

                // Jackson yordamida JSON stringga aylantirish
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonData = objectMapper.writeValueAsString(data);

                emitter.send(SseEmitter.event()
                        .name("progress")
                        .data(jsonData));
            } catch (Exception e) {
                emitters.remove(branchId);
                emitter.completeWithError(e);
            }
        }
    }

    public void sendError(SseEmitter emitter, String errorMessage) {
        try {
            emitter.send(SseEmitter.event().name("error").data(errorMessage));
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
    }


    public void sendCompletion(SseEmitter emitter, String message) {
        try {
            emitter.send(SseEmitter.event().name("completed").data(message));
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
    }

    public SseEmitter getEmitter(UUID branchId) {
        return emitters.computeIfAbsent(branchId, id -> new SseEmitter(Long.MAX_VALUE));
    }
}
