//package com.example.checkcheck.repository;
//
//import com.example.checkcheck.model.AlarmType;
//import com.example.checkcheck.model.Member;
//import com.example.checkcheck.model.Notification;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.util.Map;
//
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class EmitterRepositoryImplTest {
//
//    private final EmitterRepository emitterRepository = new EmitterRepositoryImpl();
//    private final Long DEFAULT_TIMEOUT = 60L * 1000L * 60L;
//
//    @Test
//    @DisplayName("새로운 emitter 추가했을 때")
//    void save() throws Exception{
//        // given
//        Long memberId = 1L;
//        String emitterId = memberId + "_" + System.currentTimeMillis();
//        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
//
//        //when - > then
//        Assertions.assertDoesNotThrow(()-> emitterRepository.save(emitterId, sseEmitter));
//        Assertions.assertEquals(memberId, 1L);
//    }
//
//    @Test
//    @DisplayName("수신된 이벤트를 캐시에 저장할 때")
//    void saveEventCache() throws Exception{
//        // given
//        Long memberId = 1L;
//        String eventCacheId = memberId + "_"+ System.currentTimeMillis();
//        Notification notification = new Notification(
//                                   AlarmType.comment,
//                "댓글 알림이 도착했습니다",
//                true,
//                "http://localhost:8080",
//                new Member()
//        );
//        //when, then
//        Assertions.assertDoesNotThrow(()-> emitterRepository.saveEventCache(eventCacheId,notification));
//    }
//
//    @Test
//    @DisplayName("수신한 이벤트 캐시에 저장되는지 확인")
//    void findAllEventCacheStartWithByUserId() throws InterruptedException {
//        //given
//        Long memberId = 1L;
//        String eventCacheId1 =  memberId + "_" + System.currentTimeMillis();
//        Notification notification1 = new Notification(AlarmType.COMMENT, "댓글 알람이 도착했어요.", true, "http://localhost:8080",new Member());
//        emitterRepository.saveEventCache(eventCacheId1, notification1);
//
//        Thread.sleep(100);
//        String eventCacheId2 =  memberId + "_" + System.currentTimeMillis();
//        Notification notification2 = new Notification(AlarmType.CHOICE, "댓글이 채택되었습니다.", true, "http://localhost:8080",new Member());
//        emitterRepository.saveEventCache(eventCacheId2, notification2);
//
//
//        //when
//        Map<String, Object> ActualResult = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(memberId));
//
//        //then
//        Assertions.assertEquals(2, ActualResult.size());
//    }
//
//    @Test
//    @DisplayName("회원이 접속한 모든 Emitter를 찾아보자")
//    void findAllEmitterStartWithByUserId() throws InterruptedException {
//        //given
//
//        Long memberId = 1L;
//        String emitterId1 = memberId + "_" + System.currentTimeMillis();
//        emitterRepository.save(emitterId1,new SseEmitter(DEFAULT_TIMEOUT));
//
//        Thread.sleep(100);
//        String emitterId2 = memberId + "_" + System.currentTimeMillis();
//        emitterRepository.save(emitterId2, new SseEmitter(DEFAULT_TIMEOUT));
//
//        Thread.sleep(100);
//        String emitterId3 = memberId + "_" + System.currentTimeMillis();
//        emitterRepository.save(emitterId3, new SseEmitter(DEFAULT_TIMEOUT));
//
//        //when
//        Map<String, SseEmitter> ActualResult = emitterRepository.findAllEmitterStartWithByUserId(String.valueOf(memberId));
//
//        //then
//        Assertions.assertEquals(3, ActualResult.size());
//    }
//}