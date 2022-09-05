package com.example.checkcheck.service.notification;

import com.example.checkcheck.dto.requestDto.NotificationRequestDto;
import com.example.checkcheck.dto.responseDto.NotificationResponseDto;
import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.Notification;
import com.example.checkcheck.repository.EmitterRepository;
import com.example.checkcheck.repository.MemberRepository;
import com.example.checkcheck.repository.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    //TODO : 나중에 ERROR CODE Customizing 필요
    public void sendNotification(Long id, NotificationRequestDto requestDto) {
        if (requestDto == null) {
            return;
        }
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다")

        );
        Map result = objectMapper.convertValue(requestDto, Map.class);
        System.out.println(result);
        SseEmitter.SseEventBuilder sseEvent = SseEmitter.event()
                .id(id.toString())
                .name("sse")
                .data(result);

        emitterRepository.get(id).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(sseEvent);
            } catch (IOException | IllegalStateException e) {
                emitterRepository.remove(id);
            }
        }, () -> log.info("sendNotification Error"));

        notificationRepository.save(new Notification(requestDto, member));
    }

    public List<NotificationResponseDto> getNotification(String userEmail) {
        Member member = memberRepository.findByUserEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다")
        );
        List<NotificationResponseDto> responseDtoList = new ArrayList<>();
        List<Notification> notificationList = notificationRepository.findByMemberOrderByCreatedAtDesc(member);
        for (Notification n : notificationList) {
            responseDtoList.add(new NotificationResponseDto(n));
        }
        return responseDtoList;
    }

    @Transactional
    public String readOk(String userEmail){
        Member member = memberRepository.findByUserEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다")
        );
        List<Notification> notificationList = notificationRepository.findByMemberOrderByCreatedAtDesc(member);
        for (Notification n : notificationList) {
            n.changeState();
        }

        return "읽음 처리 완료";
    }


    @Transactional
    public String deleteNotification(String userEmail){
        Member member = memberRepository.findByUserEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다")
        );

        notificationRepository.deleteByMember(member);

        return "알림 삭제 완료";
    }

}