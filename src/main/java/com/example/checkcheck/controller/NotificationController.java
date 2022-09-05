package com.example.checkcheck.controller;

import com.example.checkcheck.dto.responseDto.NotificationResponseDto;
import com.example.checkcheck.service.notification.EmitterService;
import com.example.checkcheck.service.notification.NotificationService;
import com.example.checkcheck.util.LoadUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final EmitterService emitterService;
    private final NotificationService notificationService;


    @ApiOperation(value = "알림 목록 조회")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationResponseDto>> getNotification() {
        LoadUser.loginAndEmailCheck();
        List<NotificationResponseDto> responseDtoList = notificationService.getNotification(LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);

    }

    @ApiOperation(value = "알림 전체 읽음 처리")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @GetMapping("/notifications/read")
    public ResponseEntity<String> readOk() {
        LoadUser.loginAndEmailCheck();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(notificationService.readOk(LoadUser.getEmail()));
    }

    @ApiOperation(value = "알림 전체 삭제")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @DeleteMapping("/notifications")
    public ResponseEntity<String> deleteNotification() {
        LoadUser.loginAndEmailCheck();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(notificationService.deleteNotification(LoadUser.getEmail()));
    }

    @GetMapping("/subscribe/{id}")
    public SseEmitter subscribe(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
                                @PathVariable Long id) {
        return emitterService.createEmitter(id);
    }
}
