package com.lguplus.ukids.admin.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class FileS3UploadRequestDto {
    private Integer userseq;
    private String uploadTypeCode;
    private String bucketTypeCode;
    private Integer fileGroupSeq;
    private MultipartFile multipartFile;
}
