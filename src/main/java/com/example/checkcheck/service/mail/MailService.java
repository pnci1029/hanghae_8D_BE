package com.example.checkcheck.service.mail;

import com.example.checkcheck.dto.requestDto.MailRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MailService {
    private JavaMailSender javaMailSender;
    private static final String FROM_ADDRESS = "checkcheckhanghae99@gmail.com";

    @Async
    public void mailSend(MailRequestDto mailRequestDto){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailRequestDto.getAddress());
        message.setSubject(mailRequestDto.getTitle());
        message.setFrom(MailService.FROM_ADDRESS);
        message.setText(mailRequestDto.getMessage());

        log.info("mail 비동기처리 확인 : {}", mailRequestDto.getAddress());

        javaMailSender.send(message);    }
}
// try {
//     MailHandler mailHandler = new MailHandler(javaMailSender);
//     mailHandler.setTo(mailDto.getAddress());
//     mailHandler.setSubject("인증메일입니다.");
//     String htmlContent = "<p>" + mailDto.getMessage() +"<p> <img src='cid:sample-img'>";
//     mailHandler.setText(htmlContent, true);
//     mailHandler.setAttach(mailDto.getFile().getOriginalFilename(), mailDto.getFile());
//     mailHandler.setInline("sample-img", mailDto.getFile());
//     mailHandler.send();
// }    catch (Exception e){
//     e.printStackTrace();
// }
//}
