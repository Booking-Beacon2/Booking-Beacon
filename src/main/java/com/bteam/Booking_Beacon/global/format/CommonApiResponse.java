package com.bteam.Booking_Beacon.global.format;

import com.amazonaws.Response;
import com.bteam.Booking_Beacon.global.exception.CommonErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

/**
 *
 * @param <T>
 * @description 정형화된 데이터를 반환하기 위한 response format -> 필요성을 느끼지 못해 사용하지 않는 중
 * 사용하려면 service 단에서 CommonApiResponse.toEntity(ResponseEntity.ok()) 와 같이 사용하면 됨.
 */
@RequiredArgsConstructor
@Builder
@Getter
public class CommonApiResponse<T> {
    private final HttpStatusCode status;
    private final String message; // success or fail
    private final T data;

    public static <T> CommonApiResponse<T> toEntity(ResponseEntity<T> responseEntity) {
        String message;
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            message = "SUCCESS";
        }else {
            message = "FAIL";
        }
        return CommonApiResponse.<T>builder()
                .data(responseEntity.getBody())
                .status(responseEntity.getStatusCode())
                .message(message)
                .build();
    }
}
