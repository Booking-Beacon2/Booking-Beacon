package com.bteam.Booking_Beacon.domain.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false, length = 50, name = "user_name")
    private  String userName;

    @Column(nullable = false, length = 50, name = "user_email")
    private String userEmail;

    @Column(nullable = false, length = 100, name = "password")
    @JsonIgnore
    private String password;

    @Column(nullable = false, name = "created_date")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdDate;

    @Builder
    public UserEntity(String userEmail, String password, String userName) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
    }

}
