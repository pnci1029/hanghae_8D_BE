package com.example.checkcheck.service.mail;

import com.example.checkcheck.dto.requestDto.MailRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailService {
    private JavaMailSender javaMailSender;
    private static final String FROM_ADDRESS = "chackcheck99@gmail.com";

    @Async
    public void mailSend(MailRequestDto mailRequestDto){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailRequestDto.getAddress());
        message.setSubject(mailRequestDto.getTitle());
        message.setFrom(MailService.FROM_ADDRESS);
        message.setText(mailRequestDto.getMessage());
        javaMailSender.send(message);    }
}

