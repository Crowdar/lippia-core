package com.crowdar.core;

import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.auth.AWSCredentialsProvider;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.auth.BasicAWSCredentials;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;

public class S3Client {
    public AWSCredentialsProvider credentialsProvider(String accessKey, String secretKey) {
        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(accessKey, secretKey)
        );
    }

    public AwsClientBuilder.EndpointConfiguration endpointConfiguration(String endpoint, String region) {
        return new AwsClientBuilder.EndpointConfiguration(endpoint, region);
    }

    public AmazonS3 amazonS3(AWSCredentialsProvider credentialsProvider,
                             AwsClientBuilder.EndpointConfiguration endpointConfiguration) {

        ClientConfiguration clientConfig = new ClientConfiguration()
                .withRequestTimeout(5000)
                .withTcpKeepAlive(true);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withEndpointConfiguration(endpointConfiguration)
                .withPathStyleAccessEnabled(true)
                .withChunkedEncodingDisabled(true)
                .withClientConfiguration(clientConfig)
                .build();
    }
}
