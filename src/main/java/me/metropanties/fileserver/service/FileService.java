package me.metropanties.fileserver.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void uploadFile(String key, MultipartFile file);

    void deleteFile(String name);

    byte[] getFile(String name);

    boolean fileExists(String name);

}
