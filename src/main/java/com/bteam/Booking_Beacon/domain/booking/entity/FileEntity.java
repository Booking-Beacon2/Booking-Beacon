package com.bteam.Booking_Beacon.domain.booking.entity;

import com.bteam.Booking_Beacon.domain.booking.common.FileTypeEnum;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;
import java.util.Locale;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
@Table(name = "file")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "file_id")
    private Long fileId;

    @Column(nullable = false, name = "s3_file_name")
    private UUID s3FileName;

    @Column(nullable = false, name = "file_name")
    private String fileName;

    @Column(nullable = false, name = "extension")
    private String extension;

    @Column(nullable = false, name = "size")
    private Long size;

    @Column(nullable = false, name = "type")
    @ColumnDefault("musical")
    @Enumerated(EnumType.STRING)
    private FileTypeEnum type;

    @Column(nullable = false, name = "created_date")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdDate;

    @Builder
    public FileEntity(UUID s3FileName, String fileName, String extension, Long size, FileTypeEnum type) {
        this.s3FileName = s3FileName;
        this.fileName = fileName;
        this.extension = extension;
        this.size = size;
        this.type = type;
    }

}
