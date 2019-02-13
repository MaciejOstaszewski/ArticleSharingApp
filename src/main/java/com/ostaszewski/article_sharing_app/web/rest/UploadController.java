package com.ostaszewski.article_sharing_app.web.rest;


import com.codahale.metrics.annotation.Timed;
import com.google.common.net.HttpHeaders;
import com.ostaszewski.article_sharing_app.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UploadController {

    private final StorageService storageService;

    private List<String> files = new ArrayList<>();

    @PostMapping("/storefile")
    @Timed
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String message;
        try {
            storageService.store(file);
            files.add(file.getOriginalFilename());

            message = "You successfully uploaded " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            e.printStackTrace();
            message = "Fail to upload " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @PostMapping("/renameimages")
    @Timed
    public ResponseEntity<List<String>> changeArticleImages(@RequestBody String [] imagesNames) {
        List<String> images = Arrays.stream(imagesNames).map(image -> MvcUriComponentsBuilder
            .fromMethodName(UploadController.class, "getFile", image).build().toString())
            .collect(Collectors.toList());
        return ResponseEntity.ok().body(images);
    }

    @GetMapping("/getallfiles")
    public ResponseEntity<List<String>> getListFiles() {
        List<String> fileNames = files
            .stream().map(fileName -> MvcUriComponentsBuilder
                .fromMethodName(UploadController.class, "getFile", fileName).build().toString())
            .collect(Collectors.toList());
        return ResponseEntity.ok().body(fileNames);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.loadFile(filename);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);

    }

    @GetMapping("/getimage/{image}")
    public ResponseEntity<String> getImage(@PathVariable String image) {
        return ResponseEntity.ok().body(
            MvcUriComponentsBuilder.fromMethodName(UploadController.class, "getFile", image).build().toString());
    }


}
