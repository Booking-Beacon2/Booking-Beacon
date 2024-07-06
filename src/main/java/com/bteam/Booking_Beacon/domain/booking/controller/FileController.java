package com.bteam.Booking_Beacon.domain.booking.controller;


import com.bteam.Booking_Beacon.domain.booking.common.FileTypeEnum;
import com.bteam.Booking_Beacon.domain.booking.dto.UploadFileRes;
import com.bteam.Booking_Beacon.domain.booking.service.FileService;
import com.bteam.Booking_Beacon.global.exception.RestApiException;
import com.bteam.Booking_Beacon.global.exception.UnHandledUserException;
import com.bteam.Booking_Beacon.global.format.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.bteam.Booking_Beacon.global.exception.CommonErrorCode;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("file")
@AllArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "파일 업로드")
    public ResponseEntity<UploadFileRes> uploadFile(@RequestParam("files") List<MultipartFile> files, @RequestParam("type") FileTypeEnum fileTypeEnum) {
        return this.fileService.uploadFile(files, fileTypeEnum);
    }

    @GetMapping(value = "files", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "파일 다운로드")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("fileId") Long fileId) {
        return this.fileService.downloadFile(fileId);
    }
}
