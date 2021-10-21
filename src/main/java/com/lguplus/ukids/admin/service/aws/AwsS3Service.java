package com.lguplus.ukids.admin.service.aws;

import java.io.InputStream;
import java.util.Date;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.lguplus.ukids.admin.constants.CommonConstants;
import com.lguplus.ukids.admin.constants.FileS3Constants;
import com.lguplus.ukids.admin.constants.StatusCodeConstants;
import com.lguplus.ukids.admin.dto.FileS3Dto;
import com.lguplus.ukids.admin.dto.FileS3UploadRequestDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("awsS3Service")
public class AwsS3Service {

    @Value("${cloud.aws.region}")
    private String region;

    @Value("${cloud.aws.s3.session-time}")
    private long sessionTime;

    @Value("${cloud.aws.s3.private-bucket}")
    private String privateBucket;

    @Value("${cloud.aws.s3.public-bucket}")
    private String publicBucket;

    @Value("${spring.profiles}")
    private String profiles;

    private AWSCredentialsProvider credentialsProvider;

    public String getUploadPresignedUrl(final FileS3Dto param) throws Exception {
        return getPresignedUrl(param.getKey(), getBucketName(param.getBucketTypeCode()), HttpMethod.PUT);
    }

    public String getDownloadPresignedUrl(final FileS3Dto param) throws Exception {
        return getPresignedUrl(param.getKey(), getBucketName(param.getBucketTypeCode()), HttpMethod.GET);
    }

    public String deleteFileFromS3(final FileS3Dto param) throws Exception {
        if (param.getKey().startsWith("/")) {
            param.setKey(param.getKey().substring(1));
        }

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(region)
                .withCredentials(getCredentialsProvider()).build();

        s3Client.deleteObject(new DeleteObjectRequest(getBucketName(param.getBucketTypeCode()), param.getKey()));

        return StatusCodeConstants.SUCCESS;
    }

    public String putFileToS3(FileS3UploadRequestDto param) throws Exception{
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(region)
                .withCredentials(getCredentialsProvider()).build();

        String bucketName = getBucketName(param.getBucketTypeCode());
        String key =  param.getUploadTypeCode() + "/" + param.getFileGroupSeq() + "/" + param.getMultipartFile().getOriginalFilename();
        InputStream stream = param.getMultipartFile().getInputStream();

        s3Client.putObject(new PutObjectRequest(bucketName, key, stream, null).withCannedAcl(CannedAccessControlList.PublicRead));

        return s3Client.getUrl(bucketName, key).toString();
    }

    private String getPresignedUrl(final String key, final String bucketName, final HttpMethod httpMethod) throws Exception {

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(region)
                .withCredentials(getCredentialsProvider()).build();

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key)
                .withMethod(httpMethod).withExpiration(getExpirationInfo());

        return s3Client.generatePresignedUrl(generatePresignedUrlRequest).toExternalForm();
    }

    private Date getExpirationInfo() {
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + sessionTime);

        return expiration;
    }

    private AWSCredentialsProvider getCredentialsProvider() {
        if (credentialsProvider == null) {
            if (CommonConstants.ENV_CODE_DEFAULT.equals(profiles)) {
                credentialsProvider = new ProfileCredentialsProvider(); // 로컬환경인 경우
            } else {
                credentialsProvider = new WebIdentityTokenCredentialsProvider();
            }
        }
        return credentialsProvider;
    }

    private String getBucketName(final String bucketType) {
        return (FileS3Constants.BUCKET_TYPE_PRIVATE.equals(bucketType) ? privateBucket : publicBucket);
    }
}
