package com.example.checkcheck.service.mail;

import com.example.checkcheck.dto.requestDto.MailRequestDto;
import com.example.checkcheck.exception.CustomException;
import com.example.checkcheck.exception.ErrorCode;
import com.example.checkcheck.model.Member;
import com.example.checkcheck.repository.MemberRepository;
import com.example.checkcheck.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MailService {
    private JavaMailSender javaMailSender;

    private MemberRepository memberRepository;
    private static final String FROM_ADDRESS = "chackcheck99@gmail.com";

    @Async
    public void mailSend(MailRequestDto mailRequestDto){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailRequestDto.getAddress());
        message.setSubject(mailRequestDto.getTitle());
        message.setFrom(MailService.FROM_ADDRESS);
        message.setText(mailRequestDto.getDetailMessage());

        log.info("mail 비동기처리 확인 : {}", mailRequestDto.getAddress());

        javaMailSender.send(message);
    }

    @Transactional
    public void setEmailApproved(Long memberId){
        Optional<Member> members = memberRepository.findById(memberId);
        Member member = members.orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        member.setEmailAgreement();
    }

    @Transactional
    public void setEmailOpposed(Long memberId){
        Optional<Member> members = memberRepository.findById(memberId);
        Member member = members.orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        member.setEmailOpposition();
    }
}

