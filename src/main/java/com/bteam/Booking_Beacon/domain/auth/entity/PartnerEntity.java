package com.bteam.Booking_Beacon.domain.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity()
@Table(name = "partner")
public class PartnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "partner_id")
    private Long partnerId;

    @Column(nullable = false, length = 100, name = "partner_name")
    private String partnerName;

    @Column(nullable = false, length = 50, name = "user_name")
    private  String userName;

    @Column(nullable = false, length = 50, name = "email")
    private String email;

    @Column(nullable = false, length = 100, name = "password")
    @JsonIgnore
    private String password;

    @Column(nullable = false, length = 10, name = "ein")
    @Pattern(regexp = "\\d+", message = "Must contain only numeric characters")
    private String ein;

    @Column(nullable = false, length = 11, name = "phone_number")
    @Pattern(regexp = "\\d+", message = "Must contain only numeric characters")
    private String phoneNumber;

    @Column(nullable = false, name = "created_date")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdDate;

    @Builder
    public PartnerEntity(String partnerName, String userName, String email, String password, String ein, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.partnerName = partnerName;
        this.userName = userName;
        this.ein = ein;
        this.phoneNumber = phoneNumber;
    }
}
