package com.example.checkcheck.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class S3Uploader {


	private final AmazonS3 amazonS3;

	public S3Uploader(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}

	@Value("${cloud.aws.s3.bucket}")
	public String bucket;  // S3 버킷 이름

	public String upload(MultipartFile multipartFile) throws IOException {
		File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
				.orElseThrow(() -> new IllegalArgumentException("MultipartFile -> 파일 변환 실패"));

		return uploadToS3(uploadFile);
	}

	// S3로 파일 업로드하기
	@Transactional
	public String uploadToS3(File uploadFile) throws IOException {

		String fileName = UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
		String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
		removeNewFile(uploadFile);


		return uploadImageUrl;
	}

	// S3로 업로드
	private String putS3(File uploadFile, String fileName) {
		amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
		return amazonS3.getUrl(bucket, fileName).toString();
	}

	// 로컬에 저장된 이미지 지우기
	private void removeNewFile(File targetFile) {
		if (targetFile.delete()) {
			log.info("File delete success");
			return;
		}
		log.info("File delete fail");
	}

	// 로컬에 파일 업로드 하기
	private Optional<File> convert(MultipartFile file) throws IOException {
		File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());     // 현재 프로젝트 절대경로
		if (convertFile.createNewFile()) {
			try (FileOutputStream fos = new FileOutputStream(convertFile)) {
				fos.write(file.getBytes());
				return Optional.of(convertFile);
			}

		}
		return null;
	}


}
