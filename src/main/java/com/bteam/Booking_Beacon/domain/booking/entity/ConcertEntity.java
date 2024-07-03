package com.bteam.Booking_Beacon.domain.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

@Entity()
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
@Table(name = "concert")
public class ConcertEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "concert_id")
    private Long concertId;

    @Column(nullable = false, length = 50, name = "title")
    private String title;

    @Column(nullable = false, length = 1000, name = "description")
    private String description;

    @Column(nullable = false, name = "capacity")
    private Integer capacity;

    @Column(nullable = false, name = "created_date")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdDate;

    @Column(nullable = false, name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp endDate;

    @Builder
    public ConcertEntity(String title, String description, Integer capacity, Timestamp endDate) {
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.endDate = endDate;
    }
}
