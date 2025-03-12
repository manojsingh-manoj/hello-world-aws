package com.test.aws.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;

@RestController
public class HelloWorldController {
	
	@GetMapping("/")
	public String hello() {
		return "Hello World!";
	}

	@GetMapping("/welcome")
	public String welcome(@RequestParam(value = "name", defaultValue = "in Spring Boot World") String name) {
		return String.format("Welcome to Azure pipeline%s!", name);
	}

	@GetMapping("/listbuckets")
	public ResponseEntity<?> getS3Buckets() {

		ResponseEntity<?> responseEntity = null;

		Regions clientRegion = Regions.US_EAST_2;

		AmazonS3 s3Client = null;

		System.out.println("Listing S3 Buckets---------->>");
		try {
			AWSCredentialsProvider provider = new AWSCredentialsProviderChain(
					new InstanceProfileCredentialsProvider(true), new ProfileCredentialsProvider());

			s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).withCredentials(provider).build();
			List<Bucket> buckets = s3Client.listBuckets();
			
			for(Bucket bucket : buckets) {
			    System.out.println(bucket.getName());
			}

			responseEntity = new ResponseEntity<List<Bucket>>(s3Client.listBuckets(), HttpStatus.OK);

		} catch (Exception ex) {
			ex.printStackTrace();
			responseEntity = new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;
	}
}
