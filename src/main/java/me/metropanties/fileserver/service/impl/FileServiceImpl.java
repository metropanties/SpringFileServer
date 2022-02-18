package me.metropanties.fileserver.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import me.metropanties.fileserver.service.FileService;
import me.metropanties.fileserver.util.EnvUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private static final String BUCKET_NAME = EnvUtils.get("BUCKET_NAME");

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    private final AmazonS3 s3;

    @Override
    public void uploadFile(String key, MultipartFile file) {
        try {
            if (fileExists(key))
                return;

            ObjectMetadata metadata = new ObjectMetadata();
            Map<String, String> fileMetadata = Map.of(
                    "Content-Type", Objects.requireNonNull(file.getContentType()),
                    "Content-Length", String.valueOf(file.getSize())
            );
            fileMetadata.forEach(metadata::addUserMetadata);

            s3.putObject(BUCKET_NAME, key, file.getInputStream(), metadata);
            LOGGER.info("Successfully uploaded {} file!", key);
        } catch (AmazonServiceException | IOException e) {
            LOGGER.error("Failed saving file to S3!", e);
        }
    }

    @Override
    public void deleteFile(String name) {
        try {
            if (!fileExists(name)) {
                LOGGER.warn("Tried deleted a non-existing file!");
                return;
            }

            s3.deleteObject(BUCKET_NAME, name);
            LOGGER.info("{} file deleted!", name);
        } catch (AmazonServiceException e) {
            LOGGER.error("Error occurred when deleting file!", e);
        }
    }

    @Override
    public byte[] getFile(String name) {
        try {
            if (!fileExists(name)) {
                LOGGER.warn("Couldn't find file for the key of {}!", name);
                return null;
            }

            S3Object object = s3.getObject(BUCKET_NAME, name);
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (AmazonServiceException | IOException e) {
            LOGGER.error("Error occurred when retrieving file!", e);
            return new byte[0];
        }
    }

    @Override
    public boolean fileExists(String name) {
        return s3.doesObjectExist(BUCKET_NAME, name);
    }

}
