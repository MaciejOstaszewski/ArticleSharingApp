package com.ostaszewski.article_sharing_app.service;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {

    private final Path rootLocation = Paths.get("src/main/resources/images/");


    public void store(MultipartFile file) {
        try {

            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));

        } catch (Exception e) {
            throw new RuntimeException("Fail!");
        }
    }

    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Fail");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Fail");
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

}
