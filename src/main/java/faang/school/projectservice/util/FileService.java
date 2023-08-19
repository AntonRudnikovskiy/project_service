package faang.school.projectservice.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import faang.school.projectservice.exceptions.FileUploadException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    @Setter
    @Value("${aws.bucket-name}")
    private String bucketName;
    private final AmazonS3 amazonS3;

    public String upload(MultipartFile multipartFile, long projectId) {
        String fileName = multipartFile.getOriginalFilename();
        long size = multipartFile.getSize();
        String key = String.format("p%d_%s_%s", projectId, size, fileName);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        try {
            InputStream inputStream = multipartFile.getInputStream();
            amazonS3.putObject(bucketName, key, inputStream, metadata);
        } catch (IOException ioe) {
            log.error("An error occurred while reading the file's input stream", ioe);
            throw new FileUploadException(ioe.getMessage());
        } catch (AmazonServiceException ase) {
            log.error("Amazon S3 couldn't process operation", ase);
            throw ase;
        } catch (SdkClientException sce) {
            log.error("Amazon S3 couldn't be contacted for a response", sce);
            throw sce;
        }

        return key;
    }

    public void delete(String objectKey) {
        try {
            amazonS3.deleteObject(bucketName, objectKey);
        } catch (AmazonServiceException ase) {
            log.error("Amazon S3 couldn't process operation", ase);
            throw ase;
        } catch (SdkClientException sce) {
            log.error("Amazon S3 couldn't be contacted for a response", sce);
            throw sce;
        }
    }
}