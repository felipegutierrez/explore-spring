package com.github.felipegutierrez.explore.spring.cloud.aws;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.adobe.testing.s3mock.junit5.S3MockExtension;
import com.adobe.testing.s3mock.util.HashUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * Tests and demonstrates the usage of the {@link S3MockExtension}
 * for the SDK v2.
 * */
class S3MockExtensionProgrammaticTest {

    @RegisterExtension
    static final S3MockExtension S3_MOCK = S3MockExtension.builder().silent().withSecureConnection(false).build();

    private static final String BUCKET_NAME = "mydemotestbucket";
    private static final String UPLOAD_FILE_NAME = "src/test/resources/sampleFile.txt";

    private final S3Client s3Client = S3_MOCK.createS3ClientV2();

    /**
     * Creates a bucket, stores a file, downloads the file again and compares checksums.
     *
     * @throws Exception if FileStreams can not be read
     */
    @Test
    void shouldUploadAndDownloadObject() throws Exception {
        final File uploadFile = new File(UPLOAD_FILE_NAME);

        s3Client.createBucket(CreateBucketRequest.builder().bucket(BUCKET_NAME).build());
        s3Client.putObject(
                PutObjectRequest.builder().bucket(BUCKET_NAME).key(uploadFile.getName()).build(),
                RequestBody.fromFile(uploadFile));

        final ResponseInputStream<GetObjectResponse> response =
                s3Client.getObject(
                        GetObjectRequest.builder().bucket(BUCKET_NAME).key(uploadFile.getName()).build());

        final InputStream uploadFileIs = new FileInputStream(uploadFile);
        final String uploadHash = HashUtil.getDigest(uploadFileIs);
        final String downloadedHash = HashUtil.getDigest(response);
        uploadFileIs.close();
        response.close();

        assertEquals(uploadHash, downloadedHash, "Up- and downloaded Files should have equal Hashes");
    }
}
