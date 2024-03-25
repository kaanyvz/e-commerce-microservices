package com.ky.documentservice.controller;

import com.ky.documentservice.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/file-storage")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImageToFIleSystem(@RequestPart("image") MultipartFile file) {
        return ResponseEntity.ok().body(fileService.uploadImgToTheSystem(file));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String id) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("image/png"))
                .body(fileService.downloadImageFromFileSystem(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteImageFromFileSystem(@PathVariable String id) {
        fileService.deleteImage(id);
        return ResponseEntity.ok().build();
    }
}