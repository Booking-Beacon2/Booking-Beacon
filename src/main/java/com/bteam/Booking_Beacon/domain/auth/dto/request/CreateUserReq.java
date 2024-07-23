package com.bteam.Booking_Beacon.domain.auth.dto.request;

import com.bteam.Booking_Beacon.domain.auth.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter // 실제 입력 필드 값을 매핑해주는 역할을 한다
@AllArgsConstructor
@Setter
@Builder
public class CreateUserReq {
    @NotNull
    private String userName;

    @NotNull
    private String password;

    @Email
    @NotNull
    private String email;

    @NotNull
    @Length(max = 11)
    private String phoneNumber;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .email(email)
                .password(password)
                .userName(userName)
                .phoneNumber(phoneNumber)
                .build();
    }

}
