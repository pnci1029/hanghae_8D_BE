package com.example.checkcheck.dto.requestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class MailRequestDto {
    private String address;
    private String title;
    private String message;
    private MultipartFile file; // 클라이언트에서 img 처리할 시 필요

    @Builder
    public MailRequestDto(String address, String title, String message) {
        this.address = address;
        this.title = title;
        this.message = message;

    }
}
