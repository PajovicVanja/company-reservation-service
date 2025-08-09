package org.soa.companyService.service;

import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class S3ClientService {

    @Value("${MINIO_ENDPOINT}")
    private String endpoint;

    @Value("${MINIO_PUBLIC_BUCKET}")
    private String bucketName;

    @Value("${MINIO_ACCESS_KEY}")
    private String accessKey;

    @Value("${MINIO_SECRETE_KEY}")
    private String secretKey;

    private MinioClient minioClient;

    private MinioClient getClient() {
        if (this.minioClient == null) {
            this.minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
        }

        return this.minioClient;
    }

    public String uploadPicture (MultipartFile file, Long idCompany) throws
            ServerException,
            InsufficientDataException,
            ErrorResponseException,
            IOException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            InvalidResponseException,
            XmlParserException,
            InternalException {
        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile);
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket(this.bucketName)
                .object("companies/"+ idCompany +"/pictures/profilePicture_" + idCompany + "_" + file.getOriginalFilename()) // Define the object name
                .filename(tempFile.getAbsolutePath())
                .contentType(file.getContentType()) // Use the original filename or generate a unique one
                .build();
        this.getClient().uploadObject(uploadObjectArgs);
        return this.endpoint + this.bucketName + "/companies/"+ idCompany +"/pictures/profilePicture_" + idCompany + "_" + file.getOriginalFilename();
    }

    public String uploadPicture (MultipartFile file, Long idService, Long idCompany) throws
            IOException,
            ServerException,
            InsufficientDataException,
            ErrorResponseException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            InvalidResponseException,
            XmlParserException,
            InternalException {
        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile);
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket(this.bucketName)
                .object("companies/" + idCompany
                        + "/services/pictures/service_picture_" + idCompany + "_" + file.getOriginalFilename())
                .filename(tempFile.getAbsolutePath())
                .contentType(file.getContentType())
                .build();
        this.getClient().uploadObject(uploadObjectArgs);
         return this.endpoint + this.bucketName
                         + "/companies/" + idCompany
                         + "/services/pictures/service_picture_" + idService + "_" + file.getOriginalFilename();
    }
}
