package com.dangol.dangolsonnimbackend.file.domain;

import com.dangol.dangolsonnimbackend.file.dto.SaveFileDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_file_attachment")
public class FileAttachment {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long recordId;

    @Column(nullable = false)
    private String recordType;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "blob_id", referencedColumnName = "id")
    private FileBlob fileBlob;

    public FileAttachment(SaveFileDTO dto) {
        this.recordId = dto.getRecordId();
        this.recordType = dto.getRecordType();
    }
}
