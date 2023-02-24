package com.dangol.dangolsonnimbackend.file.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SaveFileDTO {

    @NotNull
    private Long recordId;

    @NotNull
    private String recordType;

    @NotNull
    private String fileUrl;

    @NotNull
    private String contentType;

    @NotNull
    private Integer byteSize;
}
