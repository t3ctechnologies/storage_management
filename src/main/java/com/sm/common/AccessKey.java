package com.sm.common;

public class AccessKey {
	private static final String ACCESS_KEY = "AKIAICTRXDOYFYRUG53A";
	private static final String SECRET_KEY = "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";
	private static final String BUCKET_NAME = "weapp-storage";

	public static String getSecretKey() {
		return SECRET_KEY;
	}

	public static String getBucketName() {
		return BUCKET_NAME;
	}

	public static String getAccessKey() {
		return ACCESS_KEY;
	}

}
