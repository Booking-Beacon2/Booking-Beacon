package com.bteam.Booking_Beacon.domain.booking.service;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.bteam.Booking_Beacon.domain.booking.common.FileTypeEnum;
import com.bteam.Booking_Beacon.domain.booking.entity.FileEntity;
import com.bteam.Booking_Beacon.domain.booking.repository.FileRepository;
import com.bteam.Booking_Beacon.global.exception.CommonErrorCode;
import com.bteam.Booking_Beacon.global.exception.RestApiException;
import com.bteam.Booking_Beacon.global.exception.UnHandledUserException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;
    private final FileRepository fileRepository;

    public void uploadFile(List<MultipartFile> files, FileTypeEnum fileTypeEnum) {

        files.forEach(file -> {
            if (!Objects.requireNonNull(file.getContentType()).startsWith("image")) {
                log.error("File is not an image");
                throw new RestApiException(CommonErrorCode.BB_FILE_NOT_IMAGE);
            }
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            String ext = fileName.substring(fileName.lastIndexOf(".")).split("\\.")[1];
            UUID s3FileName = UUID.randomUUID();

            log.info("ext : {}", ext);
            log.info("fileName : {}", fileName);
            log.info("s3FileName : {}", s3FileName);

            String S3URL = "s3://" + bucket + "/" + s3FileName;

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());

            try {
                PutObjectResult result = amazonS3Client.putObject(bucket, s3FileName.toString(), file.getInputStream(), objectMetadata);
                log.info("result : {}", result.getETag());
                this.saveFile(file, s3FileName, fileTypeEnum);
            } catch (IOException e) {
                log.error(e.getMessage());
            }

        });

    }

    /**
     *
     * @param file 저장하려는 파일
     * @param s3FileName 저장하려는 파일의 s3 이름
     * @description 파일 디비 단일 save
     */
    private void saveFile(MultipartFile file, UUID s3FileName, FileTypeEnum fileTypeEnum) {
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String ext = fileName.substring(fileName.lastIndexOf(".")).split("\\.")[1];
        FileEntity fileEntity =
                FileEntity.builder()
                        .fileName(fileName)
                        .size(file.getSize())
                        .s3FileName(s3FileName)
                        .extension(ext)
                        .type(fileTypeEnum)
                        .build();
        this.fileRepository.save(fileEntity);
    }

    /**
     *
     * @param files 저장할 여러개의 파일
     * @description 여러개의 파일 bulk save
     */
    private void saveFiles(List<SaveFileInput> files) {
        List<FileEntity> fileEntities = new ArrayList<>();
        files.forEach(file -> {
            MultipartFile multipartFile = file.getFile();
            String fileName = multipartFile.getOriginalFilename();
            assert fileName != null;
            String ext = fileName.substring(fileName.lastIndexOf(".")).split("\\.")[1];
            FileTypeEnum fileTypeEnum = file.getFileTypeEnum();
            FileEntity fileEntity =
                    FileEntity.builder()
                            .fileName(fileName)
                            .size(multipartFile.getSize())
                            .s3FileName(file.getS3FileName())
                            .extension(ext)
                            .type(fileTypeEnum)
                            .build();
            fileEntities.add(fileEntity);
        });

        this.fileRepository.saveAll(fileEntities);
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    private static class SaveFileInput {
        private MultipartFile file;
        private UUID s3FileName;
        private FileTypeEnum fileTypeEnum;

    }

    @Transactional
    public ResponseEntity<ByteArrayResource> downloadFile(Long fileId) {
        try{
            Optional<FileEntity> fileEntity =  this.fileRepository.findById(fileId);
            if (fileEntity.isEmpty()) {
                throw new RestApiException(CommonErrorCode.BB_FILE_NOT_FOUND);
            }
            UUID s3FileName = fileEntity.get().getS3FileName();
            byte[] fileStream = this.readFile(s3FileName);

            String fileName = URLEncoder.encode(String.valueOf(s3FileName), "UTF-8").replaceAll("\\+", "%20");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentLength(fileStream.length);
            httpHeaders.setContentDispositionFormData("attachment", fileName);

            return ResponseEntity.ok().headers(httpHeaders).body(new ByteArrayResource(fileStream));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RestApiException(CommonErrorCode.BB_FILE_DOWNLOAD_FAIL);
        }

    }

    protected byte[] readFile(UUID s3FileName) throws IOException {
        S3Object s3Object = this.amazonS3Client.getObject(bucket, s3FileName.toString());
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(objectInputStream);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new UnHandledUserException(e.getMessage());
        }
    }
}
