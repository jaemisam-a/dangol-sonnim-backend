package com.dangol.dangolsonnimbackend.file.domain;

import com.dangol.dangolsonnimbackend.file.dto.SaveFileDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileTest {

    @Test
    void testSaveFileDTOToFileAttachmentAndFileBlobConversion() {
        SaveFileDTO dto = new SaveFileDTO();
        dto.setRecordId(1L);
        dto.setRecordType("STORE");
        dto.setFileUrl("https://test.url");
        dto.setContentType("image/jpeg");
        dto.setByteSize(123456);

        FileAttachment fileAttachment = new FileAttachment(dto);
        FileBlob fileBlob = new FileBlob(dto);

        assertEquals(fileAttachment.getRecordId(), dto.getRecordId());
        assertEquals(fileBlob.getContentType(), dto.getContentType());
    }
}
