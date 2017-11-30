package com.sm.storageregistration.Implements;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccessClass {
	private static String folderName;
	private static String SpecialKey;
	// private static String FileId;
	private static String ParentId;
	private static long specialId;
	private static String key;
	private static String bucketName;
	private static String fileId;

	public void insert(String fileName, long specialKey, String s3url) throws SQLException {

		String Key = key;
		String url = "jdbc:mysql://localhost:3306/firstwaarp";
		String user = "root";
		String password = "root";
		try {
			Connection conn = (Connection) DriverManager.getConnection(url, user, password);
			String query = " insert into sftp_storagebucketmapping ( specialKey, fileName, s3fileurl)"
					+ " values (?, ?, ?)";

			PreparedStatement preparedStmt = conn.prepareStatement(query);

			
			preparedStmt.setLong(1, specialKey);
			preparedStmt.setString(2, fileName);
			preparedStmt.setString(3, s3url);
			

			preparedStmt.execute();

			conn.close();
		} 
			
		catch (SQLException e) {
			e.printStackTrace();
		}

	}	
		public void TakeSpecialId(Long spcl_Id)
		{
			String Key1 = key;
			String url1 = "jdbc:mysql://localhost:3306/waarp";
			String user1 = "root";
			String password1 = "root";
			try {
				Connection conn = (Connection) DriverManager.getConnection(url1, user1, password1);
				String query = " select folderName,bucketName,fileName from sftp_storagebucketmapping "+ "where specialKey ="+ spcl_Id;

				PreparedStatement preparedStmt = conn.prepareStatement(query);

				// preparedStmt.setString(1, key);
//				preparedStmt.setString(1, spcl_Id);
//				preparedStmt.execute();
				ResultSet rs = preparedStmt.executeQuery(query);
				System.out.println(rs.getString(1));
				System.out.println(rs.getString(2));
				System.out.println(rs.getString(3));
				
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		
	}


