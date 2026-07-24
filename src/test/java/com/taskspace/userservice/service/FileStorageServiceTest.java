package com.taskspace.userservice.service;

import com.taskspace.userservice.config.minio.props.MinioProperties;
import io.minio.MinioClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FileStorageServiceTest {

    @Test
    void presignedUrlUsesPublicEndpoint() {
        String internalEndpoint = "http://minio:9000";
        String publicEndpoint = "http://157.22.198.33:9000";
        String accessKey = "minioadmin";
        String secretKey = "minioadmin";
        MinioProperties properties = new MinioProperties(
                internalEndpoint,
                publicEndpoint,
                accessKey,
                secretKey,
                "user-files",
                "us-east-1",
                false
        );
        MinioClient internalClient = client(internalEndpoint, accessKey, secretKey);
        MinioClient publicClient = client(publicEndpoint, accessKey, secretKey);
        FileStorageService service = new FileStorageService(
                internalClient,
                publicClient,
                properties
        );

        String url = service.getPresignedUrl("avatar");

        assertTrue(url.startsWith(publicEndpoint + "/user-files/avatar?"));
    }

    private MinioClient client(String endpoint, String accessKey, String secretKey) {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .region("us-east-1")
                .build();
    }
}
