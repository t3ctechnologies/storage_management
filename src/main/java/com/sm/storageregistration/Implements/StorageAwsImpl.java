package com.sm.storageregistration.Implements;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.sm.common.AccessKey;
import com.sm.dto.StorageAccessDTO;

public class StorageAwsImpl extends StorageAdapterClass {

	// private final static String G_DEFAULT_BUCKET_NAME = "weapp-storage";
	// private final static String G_DEFAULT_FOLDER_NAME = "in";
	private static final Logger logger = LoggerFactory.getLogger(StorageAwsImpl.class);

	public StorageAccessDTO sendFile(String filePath, String folderName, Long key) {
		logger.debug("file : {}, folder name :{}, key :{} received to store in amazone s3 Bucket.", filePath,
				folderName, key);
		StorageAccessDTO storagetypeDTO = null;

		if (filePath != null) {
			filePath = filePath.replaceAll("/", "\\");
			storagetypeDTO = new StorageAccessDTO();
			storagetypeDTO.setFile(filePath);
			storagetypeDTO.setFolder(folderName);
			storagetypeDTO.setKey(key);

			String insertedFileName = new StorageAwsImpl().add(storagetypeDTO);
			storagetypeDTO.setUrl(insertedFileName);
			if (!((insertedFileName) == null)) {
				AccessClass ac = new AccessClass();
				try {
					String filePath1 = filePath.substring(filePath.indexOf('_') + 1);
					String filePath2 = filePath1.substring(filePath1.indexOf('_') + 1);
					ac.insert(filePath2, key, insertedFileName);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			logger.debug("Inseted File is :" + insertedFileName);
			System.err.println("Inseted File is :" + insertedFileName);

		} else {
			logger.debug("File is Null");
		}
		return storagetypeDTO;

	}

	public void DeleteFile(String bucketName, String folderName) {
		credentials = new BasicAWSCredentials(AccessKey.getAccessKey(), AccessKey.getSecretKey());
		s3client = new AmazonS3Client(credentials);
		java.util.List<S3ObjectSummary> fileList = s3client.listObjects(bucketName, folderName).getObjectSummaries();
		for (S3ObjectSummary file : fileList) {
			s3client.deleteObject(bucketName, file.getKey());
		}
		s3client.deleteObject(bucketName, folderName);
		logger.debug("Deleted Successfully");
	}

	@Override
	public void GetAll(String bucketName, String folderName) {
		credentials = new BasicAWSCredentials(AccessKey.getAccessKey(), AccessKey.getSecretKey());
		s3client = new AmazonS3Client(credentials);
		logger.debug("Listing objects");
		ObjectListing objectListing = s3client
				.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix("Some_Folder"));
		List<S3ObjectSummary> objList = null;
		if (objectListing != null) {
			objList = objectListing.getObjectSummaries();
		}
		int i = 1;
		for (S3ObjectSummary objectSummary : objList) {
			logger.debug(
					"id: {}, bucketName :{}, key :{}, eTag :{}, size :{}, lastModified :{}, storageClass :{}, owner :{}",
					i, objectSummary.getBucketName(), objectSummary.getKey(), objectSummary.getETag(),
					objectSummary.getSize(), objectSummary.getLastModified(), objectSummary.getStorageClass(),
					objectSummary.getOwner());
			i++;
		}

	}

	/*
	 * public static void GetFile(String strFileName, String strFolderName){
	 * GetById(G_DEFAULT_BUCKET_NAME, G_DEFAULT_FOLDER_NAME, strFileName,
	 * strFolderName + "//" + strFileName); }
	 */

	public void GetById(String bucketName, String folderName, String key, String targetFile) {
		logger.debug("getting object details for folderName :{}, key :{}", folderName, key);
		credentials = new BasicAWSCredentials(AccessKey.getAccessKey(), AccessKey.getSecretKey());
		s3client = new AmazonS3Client(credentials);

		S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, folderName + "/" + key));
		if (s3object != null) {
			InputStream reader = new BufferedInputStream(s3object.getObjectContent());
			File tFile = new File(targetFile);
			OutputStream writer = null;
			try {
				writer = new BufferedOutputStream(new FileOutputStream(tFile));

				int read = -1;
				while ((read = reader.read()) != -1) {
					writer.write(read);
				}
				writer.flush();
				writer.close();
				reader.close();
			} catch (IOException e) {
				logger.error("Exception while processing: {}", e);
				e.printStackTrace();
			}

			logger.debug("file info :{}", s3object.getObjectMetadata().getContentType());
			// file = new File(s3object.getObjectContent());
		} else {
			logger.debug("Oops! file not found!");
		}
	}

	public static void main(String[] args) {
		// StorageAwsImpl aws = new StorageAwsImpl();
		// aws.sendFile("E:\\Images\\12816325466738.jpg", "in",
		// "12816325466738.jpg");
		// aws.GetById("weapp-storage", "in", "12816325466738.jpg",
		// "E:\\receiveFile\\12816325466738.jpg");
		/*
		 * System.out.println("Vinay");
		 * System.out.println("Testing Done Here**sdf***");
		 * System.out.println("Testing Done Here***sdf****");
		 * System.out.println("Testing Done Here*sdf***1");
		 * System.out.println("ZZZZ");
		 */
	}

	@Override
	public StorageAccessDTO sendFile(String filePath, String folderName, String key) throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
}
