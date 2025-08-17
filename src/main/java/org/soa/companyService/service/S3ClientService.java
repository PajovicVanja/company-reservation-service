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

    // NOTE: property name kept as-is to match existing env var; if your env uses MINIO_SECRET_KEY, update here too.
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
        String objectName = "companies/"+ idCompany +"/pictures/profilePicture_" + idCompany + "_" + file.getOriginalFilename();
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket(this.bucketName)
                .object(objectName)
                .filename(tempFile.getAbsolutePath())
                .contentType(file.getContentType())
                .build();
        this.getClient().uploadObject(uploadObjectArgs);
        return this.endpoint + this.bucketName + "/" + objectName;
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
        String objectName = "companies/" + idCompany
                + "/services/pictures/service_picture_" + idService + "_" + file.getOriginalFilename();
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket(this.bucketName)
                .object(objectName)
                .filename(tempFile.getAbsolutePath())
                .contentType(file.getContentType())
                .build();
        this.getClient().uploadObject(uploadObjectArgs);
        return this.endpoint + this.bucketName + "/" + objectName;
    }
}
