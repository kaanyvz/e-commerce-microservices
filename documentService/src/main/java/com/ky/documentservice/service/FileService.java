package com.ky.documentservice.service;

import com.ky.documentservice.model.File;
import com.ky.documentservice.repository.FileRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private String FOLDER_PATH;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    //PP METHODS
    protected File findFileById(String id){
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not found by id.."));
    }

    @PostConstruct
    public void init() {
        String currentWorkingDirectory = System.getProperty("user.dir");

        FOLDER_PATH = currentWorkingDirectory + "/file-storage/src/main/resources/attachments";

        java.io.File targetFolder = new java.io.File(FOLDER_PATH);

        if (!targetFolder.exists()) {
            boolean directoriesCreated = targetFolder.mkdirs();
            if (!directoriesCreated) {
                throw new RuntimeException("Error");
            }
        }
    }

    public String uploadImgToTheSystem(MultipartFile file){
        String uuid = UUID.randomUUID().toString();
        String filePath = FOLDER_PATH + "/" + uuid;
        System.out.println(filePath);

        try {
            file.transferTo(new java.io.File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("ERROR!!!" + e.getMessage());
        }

        File uploadedFile = File.builder()
                .id(uuid)
                .type(file.getContentType())
                .filePath(filePath)
                .build();

        fileRepository.save(uploadedFile);

        return filePath;
    }

    public byte[] downloadImageFromFileSystem(String id) {
        try {
            return Files.readAllBytes(new java.io.File(findFileById(id)
                    .getFilePath()).toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error while downloading file...");
        }
    }

    public String deleteImage(String id){
        File file = findFileById(id);
        if(file == null){
            throw new RuntimeException("File could not found by id: " + id);
        }
        try {
            Files.deleteIfExists(Paths.get(file.getFilePath()));
        }catch (IOException exception){
            throw new RuntimeException("Error while deleting the file: " + exception.getMessage());
        }
        return "File deleted successfully!";
    }

}
