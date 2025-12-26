package com.study.study_spring.docs;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.study.study_spring.data.dto.v1.UploadFileResponseDTO;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name="File Endpoint")
public interface FileControllerDocs {

    UploadFileResponseDTO uploadFile(MultipartFile file);
    List<UploadFileResponseDTO> uploadMultipleFile(MultipartFile[] files);
    ResponseEntity<Resource> downloadFile(String filename, HttpServletRequest request);

}
