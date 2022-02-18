package me.metropanties.fileserver.controller;

import lombok.RequiredArgsConstructor;
import me.metropanties.fileserver.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam String name, @RequestParam MultipartFile file) {
        if (file == null) {
            Map<String, Object> response_body = Map.of(
                    "message", "Can't upload a null file!",
                    "code", HttpStatus.BAD_REQUEST.value()
            );
            return new ResponseEntity<>(response_body, HttpStatus.BAD_REQUEST);
        }

        fileService.uploadFile(name, file);
        Map<String, Object> response_body = Map.of(
                "message", "Successfully uploaded file!",
                "code", HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response_body, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteFile(@RequestParam String name) {
        if (!fileService.fileExists(name)) {
            Map<String, Object> response_body = Map.of(
                    "message", "Can't find file with name " + name + "!",
                    "code", HttpStatus.BAD_REQUEST.value()
            );
            return new ResponseEntity<>(response_body, HttpStatus.BAD_REQUEST);
        }

        fileService.deleteFile(name);
        Map<String, Object> response_body = Map.of(
                "message", "Successfully deleted " + name + "!",
                "code", HttpStatus.OK.value()
        );
        return new ResponseEntity<>(response_body, HttpStatus.OK);
    }

    @GetMapping(value = "/")
    public ResponseEntity<Object> getFile(@RequestParam String name) {
        if (!fileService.fileExists(name)) {
            Map<String, Object> response_body = Map.of(
                    "message", "Can't find file with name " + name + "!",
                    "code", HttpStatus.BAD_REQUEST.value()
            );
            return new ResponseEntity<>(response_body, HttpStatus.BAD_REQUEST);
        }

        byte[] file = fileService.getFile(name);
        Map<String, Object> response_body = Map.of(
                "file", file,
                "code", HttpStatus.OK.value()
        );
        return new ResponseEntity<>(response_body, HttpStatus.OK);
    }

}
