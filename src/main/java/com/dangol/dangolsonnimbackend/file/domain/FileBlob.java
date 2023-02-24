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
@Table(name = "tb_file_blob")
public class FileBlob {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Integer byteSize;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public FileBlob(SaveFileDTO dto) {
        this.fileUrl = dto.getFileUrl();
        this.contentType = dto.getContentType();
        this.byteSize = dto.getByteSize();
    }
}
