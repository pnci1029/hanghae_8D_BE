package com.example.checkcheck.service.s3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;


@Component
@Slf4j
public class ImgScalrS3Uploader {


    private final AmazonS3Client amazonS3Client;

    public ImgScalrS3Uploader(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름


    public String uploadImage(MultipartFile multipartFile) throws IOException {

        String fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();
        String fileFormatName = Objects.requireNonNull(multipartFile.getContentType()).substring(multipartFile.getContentType().lastIndexOf("/") + 1);
        File newFile = resizeImage(multipartFile, fileName, fileFormatName);
        String uploadImageUrl = putS3(newFile, fileName);
        removeNewFile(newFile);     // File 생성시 로컬에 저장되는 파일 삭제

        return uploadImageUrl;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

//    Scalr 라이브러리로 Resizing

    private File resizeImage(MultipartFile originalImage, String fileName, String fileFormatName) throws IOException {

        // 이미지의 너비와 높이
        int demandWidth = 1000, demandHeight = 1000;
        // 요청 받은 파일로 부터 BufferedImage 객체 생성
        BufferedImage srcImg = ImageIO.read(originalImage.getInputStream());

        // resize 된 이미지 생성
        BufferedImage destImg = Scalr.resize(srcImg, demandWidth, demandHeight);

        // 이미지를 저장
        File resizedImage = new File(fileName);

        ImageIO.write(destImg, fileFormatName.toUpperCase(), resizedImage);
        return resizedImage;
    }


}

// 이미지 crop시 사용
//        // 원본 이미지의 너비와 높이 입니다.
//        int originWidth = srcImg.getWidth();
//        int originHeight = srcImg.getHeight();
//
//        // 원본 너비를 기준으로 하여 썸네일의 비율로 높이를 계산합니다.
//        int newWidth = originWidth;
//        int newHeight = (originWidth * demandHeight) / demandWidth;

//        // 계산된 높이가 원본보다 높다면 crop 이 안되므로
//        // 원본 높이를 기준으로 썸네일의 비율로 너비를 계산합니다.
//        if (newHeight > originHeight) {
//            newWidth = (originHeight * demandWidth) / demandHeight;
//            newHeight = originHeight;
//        }

//        // 계산된 크기로 원본이미지를 가운데에서 crop 합니다.
//        BufferedImage cropImg = Scalr.crop(srcImg, (originWidth - newWidth) / 2, (originHeight - newHeight) / 2, newWidth, newHeight);
