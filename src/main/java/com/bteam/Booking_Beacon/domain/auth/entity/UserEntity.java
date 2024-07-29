package com.bteam.Booking_Beacon.domain.auth.entity;

import com.bteam.Booking_Beacon.global.auth.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

//@ToString
@Getter
@NoArgsConstructor // JPA는 public, protected의 기본 생성자가 필수
@DynamicUpdate // 입력된 값만 업데이트 함.
@Entity()
@Setter
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long userId;

    @Column(nullable = false, length = 50, name = "user_name")
    private  String userName;

    @Column(nullable = false, length = 50, name = "email")
    private String email;

    @Column(nullable = false, length = 100, name = "password")
    @JsonIgnore
    private String password;

    @Column(nullable = false, length = 11, name = "phone_number")
    @Pattern(regexp = "\\d+", message = "Must contain only numeric characters")
    private String phoneNumber;

    @Column(nullable = false, name = "created_date")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdDate;

    @Column(nullable = false, name = "role")
    @ColumnDefault("USER")
    private String role;

    @Builder
    public UserEntity(String email, String password, String userName, String phoneNumber) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = Role.USER.toString();
    }

}
